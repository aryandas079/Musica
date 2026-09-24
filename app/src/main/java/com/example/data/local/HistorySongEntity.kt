package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.model.Song

@Entity(tableName = "history_songs")
data class HistorySongEntity(
    @PrimaryKey(autoGenerate = true)
    val historyId: Long = 0,
    val songId: Long,
    val title: String,
    val artist: String,
    val album: String,
    val artworkUrl: String,
    val previewUrl: String?,
    val durationMs: Long,
    val playedAt: Long = System.currentTimeMillis()
) {
    fun toSong(): Song = Song(
        id = songId,
        title = title,
        artist = artist,
        album = album,
        artworkUrl = artworkUrl,
        previewUrl = previewUrl,
        durationMs = durationMs
    )

    companion object {
        fun fromSong(song: Song): HistorySongEntity = HistorySongEntity(
            songId = song.id,
            title = song.title,
            artist = song.artist,
            album = song.album,
            artworkUrl = song.artworkUrl,
            previewUrl = song.previewUrl,
            durationMs = song.durationMs,
            playedAt = System.currentTimeMillis()
        )
    }
}
