package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.model.Song

@Entity(tableName = "playlist_songs")
data class PlaylistSongEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val playlistId: Long,
    val songId: Long,
    val title: String,
    val artist: String,
    val album: String,
    val artworkUrl: String,
    val previewUrl: String?,
    val durationMs: Long,
    val genre: String,
    val releaseYear: String,
    val addedAt: Long = System.currentTimeMillis()
) {
    fun toSong(): Song = Song(
        id = songId,
        title = title,
        artist = artist,
        album = album,
        artworkUrl = artworkUrl,
        previewUrl = previewUrl,
        durationMs = durationMs,
        genre = genre,
        releaseYear = releaseYear
    )

    companion object {
        fun fromSong(playlistId: Long, song: Song): PlaylistSongEntity =
            PlaylistSongEntity(
                playlistId = playlistId,
                songId = song.id,
                title = song.title,
                artist = song.artist,
                album = song.album,
                artworkUrl = song.artworkUrl,
                previewUrl = song.previewUrl,
                durationMs = song.durationMs,
                genre = song.genre,
                releaseYear = song.releaseYear
            )
    }
}
