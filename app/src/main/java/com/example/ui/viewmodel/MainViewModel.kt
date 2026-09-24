package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.MusicaDatabase
import com.example.data.repository.MusicRepository
import com.example.model.AppThemeMode
import com.example.model.AppearanceMode
import com.example.model.Artist
import com.example.model.HistoryItem
import com.example.model.LyricsData
import com.example.model.Song
import com.example.player.AudioPlayerManager
import com.example.util.LyricsEngine
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class LyricsDisplayMode {
    ORIGINAL,
    BILINGUAL,
    TRANSLATED
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = MusicaDatabase.getInstance(application)
    val repository = MusicRepository(db.songDao())
    val playerManager = AudioPlayerManager(application)

    // Player state delegated from player manager
    val currentSong: StateFlow<Song?> = playerManager.currentSong
    val isPlaying: StateFlow<Boolean> = playerManager.isPlaying
    val currentPositionMs: StateFlow<Long> = playerManager.currentPositionMs
    val durationMs: StateFlow<Long> = playerManager.durationMs
    val isBuffering: StateFlow<Boolean> = playerManager.isBuffering
    val isShuffle: StateFlow<Boolean> = playerManager.isShuffle
    val isLooping: StateFlow<Boolean> = playerManager.isLooping

    // Favorites from Room DB
    val favoriteSongs: StateFlow<List<Song>> = repository.favoriteSongs.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // History from Room DB
    val historyItems: StateFlow<List<HistoryItem>> = repository.historyItems.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Customization & Appearance Options
    private val _themeMode = MutableStateFlow(AppThemeMode.DARK)
    val themeMode: StateFlow<AppThemeMode> = _themeMode.asStateFlow()

    private val _appearanceMode = MutableStateFlow(AppearanceMode.LIQUID_GLASS)
    val appearanceMode: StateFlow<AppearanceMode> = _appearanceMode.asStateFlow()

    private val _isCustomizationVisible = MutableStateFlow(false)
    val isCustomizationVisible: StateFlow<Boolean> = _isCustomizationVisible.asStateFlow()

    private val _isClearHistoryDialogVisible = MutableStateFlow(false)
    val isClearHistoryDialogVisible: StateFlow<Boolean> = _isClearHistoryDialogVisible.asStateFlow()

    // Home Screen State
    private val _featuredSong = MutableStateFlow<Song?>(null)
    val featuredSong: StateFlow<Song?> = _featuredSong.asStateFlow()

    private val _trendingSongs = MutableStateFlow<List<Song>>(emptyList())
    val trendingSongs: StateFlow<List<Song>> = _trendingSongs.asStateFlow()

    private val _recommendedSongs = MutableStateFlow<List<Song>>(emptyList())
    val recommendedSongs: StateFlow<List<Song>> = _recommendedSongs.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _categorySongs = MutableStateFlow<List<Song>>(emptyList())
    val categorySongs: StateFlow<List<Song>> = _categorySongs.asStateFlow()

    private val _topArtists = MutableStateFlow<List<Artist>>(emptyList())
    val topArtists: StateFlow<List<Artist>> = _topArtists.asStateFlow()

    private val _isHomescreenLoading = MutableStateFlow(true)
    val isHomescreenLoading: StateFlow<Boolean> = _isHomescreenLoading.asStateFlow()

    // Artist Detail Discography State
    private val _artistSongs = MutableStateFlow<List<Song>>(emptyList())
    val artistSongs: StateFlow<List<Song>> = _artistSongs.asStateFlow()

    private val _currentArtist = MutableStateFlow<Artist?>(null)
    val currentArtist: StateFlow<Artist?> = _currentArtist.asStateFlow()

    private val _isArtistLoading = MutableStateFlow(false)
    val isArtistLoading: StateFlow<Boolean> = _isArtistLoading.asStateFlow()

    // Search State
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Song>>(emptyList())
    val searchResults: StateFlow<List<Song>> = _searchResults.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    // Lyrics & Translation State
    private val _lyricsData = MutableStateFlow<LyricsData?>(null)
    val lyricsData: StateFlow<LyricsData?> = _lyricsData.asStateFlow()

    private val _isLyricsLoading = MutableStateFlow(false)
    val isLyricsLoading: StateFlow<Boolean> = _isLyricsLoading.asStateFlow()

    private val _selectedLanguage = MutableStateFlow("Original")
    val selectedLanguage: StateFlow<String> = _selectedLanguage.asStateFlow()

    private val _lyricsDisplayMode = MutableStateFlow(LyricsDisplayMode.BILINGUAL)
    val lyricsDisplayMode: StateFlow<LyricsDisplayMode> = _lyricsDisplayMode.asStateFlow()

    private val _isRomanizationEnabled = MutableStateFlow(true)
    val isRomanizationEnabled: StateFlow<Boolean> = _isRomanizationEnabled.asStateFlow()

    // Navigation & Modal Sheets
    private val _isNowPlayingExpanded = MutableStateFlow(false)
    val isNowPlayingExpanded: StateFlow<Boolean> = _isNowPlayingExpanded.asStateFlow()

    private val _isSpotifyEmbedVisible = MutableStateFlow(false)
    val isSpotifyEmbedVisible: StateFlow<Boolean> = _isSpotifyEmbedVisible.asStateFlow()

    private var searchJob: Job? = null

    init {
        loadHomeScreenData()

        // Automatically sync lyrics whenever playerManager changes song
        viewModelScope.launch {
            playerManager.currentSong.collect { song ->
                if (song != null) {
                    fetchLyricsForSong(song)
                }
            }
        }

        // Dynamically compute recommendations when history changes
        viewModelScope.launch {
            historyItems.collect { history ->
                val songs = history.map { it.song }
                val recs = repository.getRecommendations(songs)
                _recommendedSongs.value = recs
            }
        }
    }

    fun loadHomeScreenData() {
        viewModelScope.launch {
            _isHomescreenLoading.value = true
            _topArtists.value = repository.getTopArtists()

            val trending = repository.getTrendingHits()
            _trendingSongs.value = trending
            _categorySongs.value = trending
            _featuredSong.value = trending.firstOrNull()

            val recs = repository.getRecommendations(historyItems.value.map { it.song })
            _recommendedSongs.value = recs

            _isHomescreenLoading.value = false
        }
    }

    fun setThemeMode(mode: AppThemeMode) {
        _themeMode.value = mode
    }

    fun setAppearanceMode(mode: AppearanceMode) {
        _appearanceMode.value = mode
    }

    fun showCustomizationDialog(show: Boolean = true) {
        _isCustomizationVisible.value = show
    }

    fun showClearHistoryDialog(show: Boolean = true) {
        _isClearHistoryDialogVisible.value = show
    }

    fun removeFromHistory(historyId: Long) {
        viewModelScope.launch {
            repository.removeFromHistory(historyId)
        }
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            repository.clearHistory()
            _isClearHistoryDialogVisible.value = false
            // Refresh recommendations to trending hits
            _recommendedSongs.value = repository.getTrendingHits()
        }
    }

    fun loadArtistSongs(artistName: String) {
        viewModelScope.launch {
            _isArtistLoading.value = true
            // Load artist details with verified profile picture
            val artistDetails = repository.getArtistDetails(artistName)
            _currentArtist.value = artistDetails
            val songs = repository.getArtistSongs(artistName)
            _artistSongs.value = songs
            _isArtistLoading.value = false
        }
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
        viewModelScope.launch {
            _isHomescreenLoading.value = true
            if (category == "All") {
                _categorySongs.value = _trendingSongs.value
            } else {
                _categorySongs.value = repository.getSongsByCategory(category)
            }
            _isHomescreenLoading.value = false
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        searchJob?.cancel()
        if (query.isBlank()) {
            _searchResults.value = emptyList()
            _isSearching.value = false
            return
        }

        searchJob = viewModelScope.launch {
            delay(300) // Debounce
            _isSearching.value = true
            val results = repository.searchSongs(query)
            _searchResults.value = results
            _isSearching.value = false

            // Also check if search query matches an artist name to preload artist discography
            val topMatch = results.firstOrNull()
            if (topMatch != null) {
                // Background recommendation refresh
                val currentHist = historyItems.value.map { it.song }
                if (currentHist.isEmpty()) {
                    _recommendedSongs.value = results.take(10)
                }
            }
        }
    }

    fun playSong(song: Song, playlist: List<Song> = emptyList()) {
        playerManager.playSong(song, playlist)
        // Add to history so homescreen recommendations are dynamically updated
        viewModelScope.launch {
            repository.addToHistory(song)
        }
    }

    fun togglePlayPause() {
        playerManager.togglePlayPause()
    }

    fun playNext() {
        skipToNext()
    }

    fun playPrevious() {
        skipToPrevious()
    }

    fun skipToNext() {
        playerManager.playNext()
        val nextSong = playerManager.currentSong.value
        if (nextSong != null) {
            viewModelScope.launch {
                repository.addToHistory(nextSong)
            }
        }
    }

    fun skipToPrevious() {
        playerManager.playPrevious()
        val prevSong = playerManager.currentSong.value
        if (prevSong != null) {
            viewModelScope.launch {
                repository.addToHistory(prevSong)
            }
        }
    }

    fun seekTo(positionMs: Long) {
        playerManager.seekTo(positionMs)
    }

    fun seekBy(deltaMs: Long) {
        playerManager.seekBy(deltaMs)
    }

    fun toggleShuffle() {
        playerManager.toggleShuffle()
    }

    fun toggleLooping() {
        playerManager.toggleLoop()
    }

    fun toggleFavorite(song: Song) {
        viewModelScope.launch {
            val isFav = favoriteSongs.value.any { it.id == song.id }
            if (isFav) {
                repository.removeFavorite(song.id)
            } else {
                val cached = _lyricsData.value?.plainLyrics
                repository.toggleFavorite(song, cached)
            }
        }
    }

    fun openNowPlaying(song: Song? = null) {
        if (song != null && currentSong.value?.id != song.id) {
            playSong(song)
        }
        _isNowPlayingExpanded.value = true
    }

    fun setTargetLanguage(language: String) {
        setLanguage(language)
    }

    fun closeNowPlaying() {
        _isNowPlayingExpanded.value = false
    }

    fun toggleSpotifyEmbed(visible: Boolean? = null) {
        _isSpotifyEmbedVisible.value = visible ?: !_isSpotifyEmbedVisible.value
    }

    fun setLanguage(language: String) {
        _selectedLanguage.value = language
        applyLiveTranslationToCurrentLyrics(language)
    }

    fun setLyricsDisplayMode(mode: LyricsDisplayMode) {
        _lyricsDisplayMode.value = mode
    }

    fun toggleRomanization() {
        _isRomanizationEnabled.value = !_isRomanizationEnabled.value
    }

    private fun fetchLyricsForSong(song: Song) {
        viewModelScope.launch {
            _isLyricsLoading.value = true
            val lyrics = repository.getLyricsForSong(song)
            _lyricsData.value = lyrics
            _isLyricsLoading.value = false
            applyLiveTranslationToCurrentLyrics(_selectedLanguage.value)
        }
    }

    private fun applyLiveTranslationToCurrentLyrics(targetLang: String) {
        val current = _lyricsData.value ?: return
        if (targetLang == "Original") {
            val updatedLines = current.syncedLines.map { line ->
                line.copy(
                    translation = null,
                    romanized = if (_isRomanizationEnabled.value) LyricsEngine.romanizeIfApplicable(line.text) else null
                )
            }
            _lyricsData.value = current.copy(syncedLines = updatedLines)
            return
        }

        val cacheKey = "${current.songId}_$targetLang"
        val cached = LyricsEngine.getCachedTranslation(cacheKey)

        if (cached != null) {
            val updatedLines = current.syncedLines.map { line ->
                line.copy(
                    translation = cached[line.text],
                    romanized = if (_isRomanizationEnabled.value) LyricsEngine.romanizeIfApplicable(line.text) else null
                )
            }
            _lyricsData.value = current.copy(syncedLines = updatedLines)
            return
        }

        viewModelScope.launch {
            val translationMap = mutableMapOf<String, String>()
            val updatedLines = current.syncedLines.map { line ->
                val trans = LyricsEngine.translateLyricLine(line.text, targetLang)
                translationMap[line.text] = trans
                line.copy(
                    translation = trans,
                    romanized = if (_isRomanizationEnabled.value) LyricsEngine.romanizeIfApplicable(line.text) else null
                )
            }
            LyricsEngine.saveTranslation(cacheKey, translationMap)
            _lyricsData.value = current.copy(syncedLines = updatedLines)
        }
    }

    override fun onCleared() {
        super.onCleared()
        playerManager.release()
    }
}
