package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.model.Song

@Entity(tableName = "cached_songs")
data class CachedSongEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val artist: String,
    val album: String,
    val artworkUrl: String,
    val previewUrl: String?,
    val durationMs: Long,
    val genre: String,
    val releaseYear: String,
    val artistImageUrl: String?
) {
    fun toSong(): Song = Song(
        id = id,
        title = title,
        artist = artist,
        album = album,
        artworkUrl = artworkUrl,
        previewUrl = previewUrl,
        durationMs = durationMs,
        genre = genre,
        releaseYear = releaseYear,
        artistImageUrl = artistImageUrl
    )

    companion object {
        fun fromSong(song: Song): CachedSongEntity = CachedSongEntity(
            id = song.id,
            title = song.title,
            artist = song.artist,
            album = song.album,
            artworkUrl = song.artworkUrl,
            previewUrl = song.previewUrl,
            durationMs = song.durationMs,
            genre = song.genre,
            releaseYear = song.releaseYear,
            artistImageUrl = song.artistImageUrl
        )
    }
}
