package com.example.model

data class Song(
    val id: Long,
    val title: String,
    val artist: String,
    val album: String,
    val artworkUrl: String,
    val previewUrl: String?,
    val durationMs: Long = 30000L,
    val genre: String = "Pop",
    val releaseYear: String = "2024",
    val spotifyTrackId: String? = null,
    val isFavorite: Boolean = false,
    val artistImageUrl: String? = null
) {
    // Generates platform-specific search/listen links
    val spotifyUrl: String
        get() = if (!spotifyTrackId.isNullOrEmpty()) {
            "https://open.spotify.com/track/$spotifyTrackId"
        } else {
            "https://open.spotify.com/search/${android.net.Uri.encode("$artist $title")}"
        }

    val spotifyEmbedUrl: String
        get() = if (!spotifyTrackId.isNullOrEmpty()) {
            "https://open.spotify.com/embed/track/$spotifyTrackId?utm_source=generator&theme=0"
        } else {
            "https://open.spotify.com/embed/search/${android.net.Uri.encode("$artist $title")}?utm_source=generator&theme=0"
        }

    val appleMusicUrl: String
        get() = "https://music.apple.com/search?term=${android.net.Uri.encode("$artist $title")}"

    val youtubeMusicUrl: String
        get() = "https://music.youtube.com/search?q=${android.net.Uri.encode("$artist $title")}"

    val amazonMusicUrl: String
        get() = "https://music.amazon.com/search/${android.net.Uri.encode("$artist $title")}"
}

data class SyncedLyricLine(
    val timeMs: Long,
    val text: String,
    var translation: String? = null,
    var romanized: String? = null
)

data class LyricsData(
    val songId: Long,
    val songTitle: String,
    val artist: String,
    val plainLyrics: String,
    val syncedLines: List<SyncedLyricLine> = emptyList(),
    val language: String = "Original",
    val isInstrumental: Boolean = false
)

data class Artist(
    val name: String,
    val imageUrl: String,
    val genre: String,
    val topHitsCount: String
)

data class HistoryItem(
    val historyId: Long,
    val song: Song,
    val playedAt: Long
)

enum class AppThemeMode {
    LIGHT,
    DARK,
    TINTED
}

enum class AppearanceMode {
    LIQUID_GLASS,
    SOLID,
    BLUR
}
