package com.example.player

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.example.model.Song
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AudioPlayerManager(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null
    private val scope = CoroutineScope(Dispatchers.Main + Job())
    private var progressJob: Job? = null

    private val _currentSong = MutableStateFlow<Song?>(null)
    val currentSong: StateFlow<Song?> = _currentSong.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentPositionMs = MutableStateFlow(0L)
    val currentPositionMs: StateFlow<Long> = _currentPositionMs.asStateFlow()

    private val _durationMs = MutableStateFlow(30000L)
    val durationMs: StateFlow<Long> = _durationMs.asStateFlow()

    private val _isBuffering = MutableStateFlow(false)
    val isBuffering: StateFlow<Boolean> = _isBuffering.asStateFlow()

    private val _isShuffle = MutableStateFlow(false)
    val isShuffle: StateFlow<Boolean> = _isShuffle.asStateFlow()

    private val _isLooping = MutableStateFlow(false)
    val isLooping: StateFlow<Boolean> = _isLooping.asStateFlow()

    private var currentPlaylist = mutableListOf<Song>()
    private var currentIndex = -1

    fun playSong(song: Song, playlist: List<Song> = emptyList()) {
        if (playlist.isNotEmpty()) {
            currentPlaylist = playlist.toMutableList()
            currentIndex = currentPlaylist.indexOfFirst { it.id == song.id }
            if (currentIndex == -1) {
                currentPlaylist.add(0, song)
                currentIndex = 0
            }
        } else if (_currentSong.value?.id != song.id) {
            currentPlaylist = mutableListOf(song)
            currentIndex = 0
        }

        if (_currentSong.value?.id == song.id && mediaPlayer != null) {
            if (!_isPlaying.value) {
                mediaPlayer?.start()
                _isPlaying.value = true
                startProgressTicker()
            }
            return
        }

        _currentSong.value = song
        _currentPositionMs.value = 0L
        _isBuffering.value = true

        releaseMediaPlayer()

        val previewUrl = song.previewUrl
        if (previewUrl.isNullOrEmpty()) {
            resolveAndPlay(song)
            return
        }

        playUrl(previewUrl, song, retryWithItunes = true)
    }

    private fun playUrl(url: String, song: Song, retryWithItunes: Boolean) {
        try {
            val player = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                if (url.startsWith("http://") || url.startsWith("https://")) {
                    setDataSource(context, android.net.Uri.parse(url))
                } else {
                    setDataSource(url)
                }
                setOnPreparedListener { mp ->
                    _isBuffering.value = false
                    _durationMs.value = mp.duration.toLong().coerceAtLeast(30000L)
                    mp.start()
                    _isPlaying.value = true
                    startProgressTicker()
                }
                setOnCompletionListener {
                    if (_isLooping.value) {
                        it.seekTo(0)
                        it.start()
                    } else {
                        playNext()
                    }
                }
                setOnErrorListener { _, _, _ ->
                    if (retryWithItunes) {
                        resolveAndPlay(song)
                    } else {
                        _isBuffering.value = false
                        _durationMs.value = song.durationMs.coerceAtLeast(30000L)
                        _isPlaying.value = true
                        startProgressTicker()
                    }
                    true
                }
                prepareAsync()
            }
            mediaPlayer = player
        } catch (e: Exception) {
            e.printStackTrace()
            if (retryWithItunes) {
                resolveAndPlay(song)
            } else {
                _isBuffering.value = false
                _durationMs.value = song.durationMs.coerceAtLeast(30000L)
                _isPlaying.value = true
                startProgressTicker()
            }
        }
    }

    private fun resolveAndPlay(song: Song) {
        scope.launch(Dispatchers.IO) {
            try {
                val query = "${song.title} ${song.artist}".trim()
                val itunes = com.example.data.remote.NetworkClient.itunesApi.searchSongs(query, limit = 1)
                val itunesUrl = itunes.results.firstOrNull()?.previewUrl
                withContext(Dispatchers.Main) {
                    if (!itunesUrl.isNullOrBlank()) {
                        playUrl(itunesUrl, song, retryWithItunes = false)
                    } else {
                        // Fallback to simulated progress ticker if track has no preview available anywhere
                        _isBuffering.value = false
                        _durationMs.value = song.durationMs.coerceAtLeast(30000L)
                        _isPlaying.value = true
                        startProgressTicker()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _isBuffering.value = false
                    _durationMs.value = song.durationMs.coerceAtLeast(30000L)
                    _isPlaying.value = true
                    startProgressTicker()
                }
            }
        }
    }

    fun togglePlayPause() {
        val player = mediaPlayer
        if (player != null) {
            if (_isPlaying.value) {
                player.pause()
                _isPlaying.value = false
                stopProgressTicker()
            } else {
                player.start()
                _isPlaying.value = true
                startProgressTicker()
            }
        } else {
            _currentSong.value?.let { playSong(it, currentPlaylist) }
        }
    }

    fun seekTo(positionMs: Long) {
        val target = positionMs.coerceIn(0L, _durationMs.value.coerceAtLeast(30000L))
        _currentPositionMs.value = target
        try {
            mediaPlayer?.seekTo(target.toInt())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun seekBy(deltaMs: Long) {
        val target = (_currentPositionMs.value + deltaMs).coerceIn(0L, _durationMs.value.coerceAtLeast(30000L))
        seekTo(target)
    }

    fun playNext() {
        if (currentPlaylist.isEmpty()) return
        if (_isShuffle.value && currentPlaylist.size > 1) {
            var nextIndex = (0 until currentPlaylist.size).random()
            if (nextIndex == currentIndex) nextIndex = (nextIndex + 1) % currentPlaylist.size
            currentIndex = nextIndex
        } else {
            currentIndex = (currentIndex + 1) % currentPlaylist.size
        }
        val nextSong = currentPlaylist.getOrNull(currentIndex) ?: return
        playSong(nextSong, currentPlaylist)
    }

    fun playPrevious() {
        if (currentPlaylist.isEmpty()) return
        if (_currentPositionMs.value > 3000L) {
            seekTo(0L)
            return
        }
        currentIndex = if (currentIndex - 1 < 0) currentPlaylist.size - 1 else currentIndex - 1
        val prevSong = currentPlaylist.getOrNull(currentIndex) ?: return
        playSong(prevSong, currentPlaylist)
    }

    fun toggleShuffle() {
        _isShuffle.value = !_isShuffle.value
    }

    fun toggleLoop() {
        _isLooping.value = !_isLooping.value
        mediaPlayer?.isLooping = _isLooping.value
    }

    private fun startProgressTicker() {
        progressJob?.cancel()
        progressJob = scope.launch {
            while (isActive && _isPlaying.value) {
                val player = mediaPlayer
                if (player != null && player.isPlaying) {
                    _currentPositionMs.value = player.currentPosition.toLong()
                } else if (player == null && _isPlaying.value) {
                    val next = _currentPositionMs.value + 200L
                    if (next >= _durationMs.value) {
                        playNext()
                    } else {
                        _currentPositionMs.value = next
                    }
                }
                delay(60L)
            }
        }
    }

    private fun stopProgressTicker() {
        progressJob?.cancel()
        progressJob = null
    }

    private fun releaseMediaPlayer() {
        stopProgressTicker()
        mediaPlayer?.apply {
            try {
                if (isPlaying) stop()
                reset()
                release()
            } catch (e: Exception) {
                // Ignored
            }
        }
        mediaPlayer = null
    }

    fun release() {
        releaseMediaPlayer()
    }
}
