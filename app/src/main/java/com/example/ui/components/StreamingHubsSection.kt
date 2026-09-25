package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Podcasts
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Web
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Song
import com.example.ui.theme.*

data class StreamingPlatform(
    val id: String,
    val name: String,
    val brandColor: Color,
    val badge: String,
    val icon: ImageVector,
    val getUrl: (Song) -> String,
    val getAppPackage: () -> String? = { null }
)

val MAJOR_STREAMING_PLATFORMS = listOf(
    StreamingPlatform(
        id = "spotify",
        name = "Spotify",
        brandColor = WhiteSmoke,
        badge = "Stream HD",
        icon = Icons.Default.MusicNote,
        getUrl = { it.spotifyUrl },
        getAppPackage = { "com.spotify.music" }
    ),
    StreamingPlatform(
        id = "apple_music",
        name = "Apple Music",
        brandColor = WhiteSmoke,
        badge = "Lossless",
        icon = Icons.Default.Headphones,
        getUrl = { it.appleMusicUrl },
        getAppPackage = { "com.apple.android.music" }
    ),
    StreamingPlatform(
        id = "youtube_music",
        name = "YouTube Music",
        brandColor = WhiteSmoke,
        badge = "Music & MV",
        icon = Icons.Default.PlayCircle,
        getUrl = { it.youtubeMusicUrl },
        getAppPackage = { "com.google.android.apps.youtube.music" }
    ),
    StreamingPlatform(
        id = "amazon_music",
        name = "Amazon Music",
        brandColor = WhiteSmoke,
        badge = "Ultra HD",
        icon = Icons.Default.Radio,
        getUrl = { song ->
            "https://music.amazon.com/search/${Uri.encode("${song.title} ${song.artist}")}"
        },
        getAppPackage = { "com.amazon.mp3" }
    ),
    StreamingPlatform(
        id = "deezer",
        name = "Deezer",
        brandColor = WhiteSmoke,
        badge = "HiFi FLAC",
        icon = Icons.Default.Sensors,
        getUrl = { song ->
            "https://www.deezer.com/search/${Uri.encode("${song.title} ${song.artist}")}"
        },
        getAppPackage = { "deezer.android.app" }
    ),
    StreamingPlatform(
        id = "soundcloud",
        name = "SoundCloud",
        brandColor = WhiteSmoke,
        badge = "Remixes & Live",
        icon = Icons.Default.Podcasts,
        getUrl = { song ->
            "https://soundcloud.com/search?q=${Uri.encode("${song.title} ${song.artist}")}"
        },
        getAppPackage = { "com.soundcloud.android" }
    ),
    StreamingPlatform(
        id = "tidal",
        name = "TIDAL",
        brandColor = WhiteSmoke,
        badge = "Master MQA",
        icon = Icons.Default.Album,
        getUrl = { song ->
            "https://listen.tidal.com/search?q=${Uri.encode("${song.title} ${song.artist}")}"
        },
        getAppPackage = { "com.aspiro.tidal" }
    )
)

@Composable
fun StreamingHubsSection(
    song: Song,
    onOpenSpotifyEmbed: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val colorScheme = MaterialTheme.colorScheme

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("streaming_hubs_section")
    ) {
        // Section Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    StormBlackElevated,
                                    StormBlackCard
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Podcasts,
                        contentDescription = null,
                        tint = WhiteSmoke,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Streaming Hubs",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(StormBlackElevated)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "DEEP LINKS",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = WhiteSmokeSoft
                            )
                        }
                    }
                    Text(
                        text = "Listen to full track across major music platforms",
                        fontSize = 12.sp,
                        color = colorScheme.onSurfaceVariant
                    )
                }
            }

            // Share Action
            IconButton(
                onClick = { shareSongLink(context, song) },
                modifier = Modifier
                    .size(36.dp)
                    .liquidGlassEffect(shape = CircleShape, elevation = 2.dp)
                    .testTag("share_song_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share song",
                    tint = colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // In-App Spotify Interactive Web Player Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            StormBlackElevated,
                            colorScheme.surfaceVariant
                        )
                    )
                )
                .border(1.dp, StormSlateBorder, RoundedCornerShape(18.dp))
                .clickable(onClick = onOpenSpotifyEmbed)
                .padding(14.dp)
                .testTag("launch_spotify_embed_card")
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(SpotifyGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayCircle,
                            contentDescription = null,
                            tint = StormBlackBg,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "Interactive Spotify Web Player",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorScheme.onSurface
                        )
                        Text(
                            text = "Play full track directly inside Musica",
                            fontSize = 12.sp,
                            color = colorScheme.onSurfaceVariant
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(SpotifyGreen.copy(alpha = 0.25f))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Open",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = SpotifyGreen
                        )
                        Icon(
                            imageVector = Icons.Default.OpenInNew,
                            contentDescription = null,
                            tint = SpotifyGreen,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Horizontal Carousel of Major Streaming Platform Deep Links
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(vertical = 4.dp)
        ) {
            items(MAJOR_STREAMING_PLATFORMS, key = { it.id }) { platform ->
                StreamingPlatformCard(
                    platform = platform,
                    song = song,
                    onClick = { launchStreamingPlatform(context, platform, song) }
                )
            }
        }
    }
}

@Composable
private fun StreamingPlatformCard(
    platform: StreamingPlatform,
    song: Song,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme

    Box(
        modifier = modifier
            .width(150.dp)
            .liquidGlassEffect(shape = RoundedCornerShape(18.dp), elevation = 4.dp)
            .border(1.dp, StormSlateBorder, RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .padding(12.dp)
            .testTag("streaming_hub_${platform.id}")
    ) {
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            // Platform Icon & Quality Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(StormBlackElevated)
                        .border(1.dp, StormSlateBorder, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = platform.icon,
                        contentDescription = platform.name,
                        tint = WhiteSmoke,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Icon(
                    imageVector = Icons.Default.OpenInNew,
                    contentDescription = "Open ${platform.name}",
                    tint = colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    modifier = Modifier.size(15.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Platform Name
            Text(
                text = platform.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(3.dp))

            // Quality Badge (e.g. Lossless, HiFi FLAC, Stream HD)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(StormBlackElevated)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = platform.badge,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = WhiteSmokeSoft
                )
            }
        }
    }
}

/**
 * Attempts to deep-link directly into the native streaming app if installed,
 * otherwise falls back seamlessly to the official web URL.
 */
private fun launchStreamingPlatform(context: Context, platform: StreamingPlatform, song: Song) {
    val targetUrl = platform.getUrl(song)
    try {
        val appPackage = platform.getAppPackage()
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(targetUrl)).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            if (appPackage != null) {
                try {
                    val pm = context.packageManager
                    val isInstalled = pm.getLaunchIntentForPackage(appPackage) != null
                    if (isInstalled) {
                        setPackage(appPackage)
                    }
                } catch (e: Exception) {
                    // Package not installed, fallback to browser
                }
            }
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        try {
            val fallbackIntent = Intent(Intent.ACTION_VIEW, Uri.parse(targetUrl)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(fallbackIntent)
        } catch (err: Exception) {
            Toast.makeText(context, "Could not open ${platform.name}", Toast.LENGTH_SHORT).show()
        }
    }
}

private fun shareSongLink(context: Context, song: Song) {
    try {
        val shareText = "Listen to \"${song.title}\" by ${song.artist}\n\nSpotify: ${song.spotifyUrl}\nApple Music: ${song.appleMusicUrl}\nYouTube Music: ${song.youtubeMusicUrl}"
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Share ${song.title}")
        shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(shareIntent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
