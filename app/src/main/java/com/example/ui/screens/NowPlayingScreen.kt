package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RepeatOne
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.Song
import com.example.ui.theme.SpotifyGreen
import com.example.ui.theme.liquidGlassEffect

@Composable
fun NowPlayingScreen(
    song: Song?,
    isPlaying: Boolean,
    isBuffering: Boolean,
    currentPositionMs: Long,
    durationMs: Long,
    isShuffle: Boolean = false,
    isLooping: Boolean = false,
    isFavorite: Boolean = false,
    onTogglePlayPause: () -> Unit,
    onSeek: (Long) -> Unit,
    onSeekBy: ((Long) -> Unit)? = null,
    onNext: (() -> Unit)? = null,
    onPrevious: (() -> Unit)? = null,
    onToggleShuffle: (() -> Unit)? = null,
    onToggleLoop: (() -> Unit)? = null,
    onToggleFavorite: (() -> Unit)? = null,
    onClose: () -> Unit,
    onOpenSpotifyEmbed: () -> Unit,
    onOpenLyrics: () -> Unit,
    modifier: Modifier = Modifier,
    onArtistClick: ((String) -> Unit)? = null
) {
    if (song == null) return

    val context = LocalContext.current
    val colorScheme = MaterialTheme.colorScheme
    val effectiveDuration = if (durationMs > 0) durationMs else 30000L

    var isScrubbing by remember { mutableStateOf(false) }
    var scrubPositionRatio by remember { mutableFloatStateOf(0f) }

    val currentRatio = if (effectiveDuration > 0) {
        (currentPositionMs.toFloat() / effectiveDuration.toFloat()).coerceIn(0f, 1f)
    } else 0f

    val displayRatio = if (isScrubbing) scrubPositionRatio else currentRatio
    val displayPositionMs = if (isScrubbing) (scrubPositionRatio * effectiveDuration).toLong() else currentPositionMs

    Surface(
        modifier = modifier.fillMaxSize(),
        color = colorScheme.background
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(bottom = 40.dp)
        ) {
            // Top Bar with Back Arrow, Header Title, and Favorite Button
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onClose,
                        modifier = Modifier
                            .size(42.dp)
                            .liquidGlassEffect(shape = CircleShape, elevation = 2.dp)
                            .testTag("now_playing_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = colorScheme.onSurface,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "NOW PLAYING",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp,
                            color = colorScheme.primary
                        )
                        Text(
                            text = "30s HD Audio Preview",
                            fontSize = 11.sp,
                            color = colorScheme.onSurfaceVariant
                        )
                    }

                    IconButton(
                        onClick = { onToggleFavorite?.invoke() },
                        modifier = Modifier
                            .size(42.dp)
                            .liquidGlassEffect(shape = CircleShape, elevation = 2.dp)
                            .testTag("now_playing_favorite_button")
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = if (isFavorite) "Remove Favorite" else "Add Favorite",
                            tint = if (isFavorite) Color(0xFFFF4081) else colorScheme.onSurface,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            // Large Rounded Album Art with Liquid Glass Glow
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .size(270.dp)
                        .liquidGlassEffect(shape = RoundedCornerShape(28.dp), elevation = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = song.artworkUrl,
                        contentDescription = song.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(28.dp))
                    )

                    // Audio playing equalizer badge overlay
                    if (isPlaying) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(12.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.Black.copy(alpha = 0.65f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.GraphicEq,
                                    contentDescription = null,
                                    tint = SpotifyGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "PLAYING",
                                    color = SpotifyGreen,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            // Song Title & Artist
            item {
                Text(
                    text = song.title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "${song.artist} • ${song.album}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = colorScheme.primary,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.clickable(enabled = onArtistClick != null) {
                        onArtistClick?.invoke(song.artist)
                    }
                )
                Spacer(modifier = Modifier.height(20.dp))
            }

            // ==========================================
            // Dedicated Audio Player UI (Seek Bar + Controls)
            // ==========================================
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .liquidGlassEffect(shape = RoundedCornerShape(24.dp), elevation = 8.dp)
                        .padding(18.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Header info inside Player Card
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Preview Scrubber",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = colorScheme.onSurfaceVariant
                            )

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(colorScheme.primary.copy(alpha = 0.15f))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "30 SECONDS",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = colorScheme.primary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Interactive Seek Bar (Slider)
                        Slider(
                            value = displayRatio,
                            onValueChange = { frac ->
                                isScrubbing = true
                                scrubPositionRatio = frac
                            },
                            onValueChangeFinished = {
                                isScrubbing = false
                                onSeek((scrubPositionRatio * effectiveDuration).toLong())
                            },
                            colors = SliderDefaults.colors(
                                thumbColor = colorScheme.primary,
                                activeTrackColor = colorScheme.primary,
                                inactiveTrackColor = colorScheme.onSurface.copy(alpha = 0.16f)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("audio_player_seek_bar")
                        )

                        // Time Labels Row (0:XX / 0:30)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = formatTime(displayPositionMs),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (isScrubbing) colorScheme.primary else colorScheme.onSurfaceVariant
                            )

                            Text(
                                text = formatTime(effectiveDuration),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Quick Seek Preset Chips (-5s, +5s, Start, Midpoint)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
                        ) {
                            QuickSeekChip(
                                label = "-5s",
                                onClick = { onSeekBy?.invoke(-5000L) ?: onSeek((currentPositionMs - 5000L).coerceAtLeast(0L)) }
                            )
                            QuickSeekChip(
                                label = "Start 0:00",
                                onClick = { onSeek(0L) }
                            )
                            QuickSeekChip(
                                label = "Mid 0:15",
                                onClick = { onSeek(15000L) }
                            )
                            QuickSeekChip(
                                label = "+5s",
                                onClick = { onSeekBy?.invoke(5000L) ?: onSeek((currentPositionMs + 5000L).coerceAtMost(effectiveDuration)) }
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Main Audio Player Control Buttons Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Shuffle Toggle
                            IconButton(
                                onClick = { onToggleShuffle?.invoke() },
                                modifier = Modifier
                                    .size(44.dp)
                                    .testTag("player_shuffle_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Shuffle,
                                    contentDescription = "Shuffle",
                                    tint = if (isShuffle) colorScheme.primary else colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                    modifier = Modifier.size(22.dp)
                                )
                            }

                            // Previous / Rewind Track
                            IconButton(
                                onClick = { onPrevious?.invoke() },
                                modifier = Modifier
                                    .size(48.dp)
                                    .testTag("player_prev_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SkipPrevious,
                                    contentDescription = "Previous Song",
                                    tint = colorScheme.onSurface,
                                    modifier = Modifier.size(30.dp)
                                )
                            }

                            // Center Primary Play / Pause Button
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.radialGradient(
                                            listOf(
                                                colorScheme.primary,
                                                colorScheme.primary.copy(alpha = 0.85f)
                                            )
                                        )
                                    )
                                    .border(2.dp, Color.White.copy(alpha = 0.3f), CircleShape)
                                    .clickable { onTogglePlayPause() }
                                    .testTag("player_play_pause_button"),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isBuffering) {
                                    CircularProgressIndicator(
                                        color = colorScheme.onPrimary,
                                        strokeWidth = 3.dp,
                                        modifier = Modifier.size(28.dp)
                                    )
                                } else {
                                    Icon(
                                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                        contentDescription = if (isPlaying) "Pause" else "Play",
                                        tint = colorScheme.onPrimary,
                                        modifier = Modifier.size(36.dp)
                                    )
                                }
                            }

                            // Next Track
                            IconButton(
                                onClick = { onNext?.invoke() },
                                modifier = Modifier
                                    .size(48.dp)
                                    .testTag("player_next_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SkipNext,
                                    contentDescription = "Next Song",
                                    tint = colorScheme.onSurface,
                                    modifier = Modifier.size(30.dp)
                                )
                            }

                            // Repeat / Loop Toggle
                            IconButton(
                                onClick = { onToggleLoop?.invoke() },
                                modifier = Modifier
                                    .size(44.dp)
                                    .testTag("player_repeat_button")
                            ) {
                                Icon(
                                    imageVector = if (isLooping) Icons.Default.RepeatOne else Icons.Default.Repeat,
                                    contentDescription = "Repeat",
                                    tint = if (isLooping) colorScheme.primary else colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(18.dp))
            }

            // "Show Synced Lyrics" Button
            item {
                Button(
                    onClick = onOpenLyrics,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("show_lyrics_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorScheme.primary,
                        contentColor = colorScheme.onPrimary
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MusicNote,
                        contentDescription = null,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Show Synced Karaoke Lyrics",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(18.dp))
            }

            // Metadata Stats Row: Release | Producers | Views
            item {
                val producers = when {
                    song.title.contains("Cruel Summer", ignoreCase = true) -> "Jack Antonoff, St. Vincent"
                    song.title.contains("Shape", ignoreCase = true) -> "Steve Mac, Ed Sheeran"
                    song.title.contains("Blinding", ignoreCase = true) -> "Max Martin, Oscar Holter"
                    song.title.contains("Birds of a Feather", ignoreCase = true) -> "FINNEAS"
                    song.title.contains("Espresso", ignoreCase = true) -> "Julian Bunetta"
                    song.title.contains("Monaco", ignoreCase = true) -> "Tainy, MAG"
                    song.title.contains("As It Was", ignoreCase = true) -> "Kid Harpoon, Tyler Johnson"
                    song.title.contains("Die With A Smile", ignoreCase = true) -> "Bruno Mars, Andrew Watt"
                    song.title.contains("Tabun", ignoreCase = true) || song.artist.contains("YOASOBI", ignoreCase = true) -> "Ayase"
                    song.title.contains("Starboy", ignoreCase = true) -> "Daft Punk, Doc McKinney"
                    song.artist.contains("Drake", ignoreCase = true) -> "Noah '40' Shebib"
                    song.artist.contains("Taylor", ignoreCase = true) -> "Jack Antonoff, Aaron Dessner"
                    song.artist.contains("Billie", ignoreCase = true) -> "FINNEAS"
                    else -> "${song.artist.split(" ").firstOrNull() ?: "Producer"}, Hitmaker"
                }

                val views = when {
                    song.title.contains("Cruel Summer", ignoreCase = true) -> "2.4B"
                    song.title.contains("Shape", ignoreCase = true) -> "3.6B"
                    song.title.contains("Blinding", ignoreCase = true) -> "4.5B"
                    song.title.contains("Birds of a Feather", ignoreCase = true) -> "1.8B"
                    song.title.contains("Espresso", ignoreCase = true) -> "1.7B"
                    song.title.contains("Monaco", ignoreCase = true) -> "890M"
                    song.title.contains("As It Was", ignoreCase = true) -> "3.1B"
                    song.title.contains("Die With A Smile", ignoreCase = true) -> "1.3B"
                    song.title.contains("One Dance", ignoreCase = true) -> "3.2B"
                    song.title.contains("God's Plan", ignoreCase = true) -> "2.5B"
                    song.title.contains("Tabun", ignoreCase = true) -> "420M"
                    song.title.contains("Starboy", ignoreCase = true) -> "2.9B"
                    else -> "850M"
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .liquidGlassEffect(shape = RoundedCornerShape(16.dp), elevation = 4.dp)
                        .padding(vertical = 14.dp, horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Release", fontSize = 11.sp, color = colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = song.releaseYear, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = colorScheme.onSurface)
                    }

                    Box(modifier = Modifier.width(1.dp).height(24.dp).background(colorScheme.onSurface.copy(alpha = 0.12f)))

                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f, fill = false)) {
                        Text(text = "Producers", fontSize = 11.sp, color = colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = producers,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Box(modifier = Modifier.width(1.dp).height(24.dp).background(colorScheme.onSurface.copy(alpha = 0.12f)))

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Streams", fontSize = 11.sp, color = colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = views, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = colorScheme.onSurface)
                    }
                }
                Spacer(modifier = Modifier.height(18.dp))
            }

            // Spotify Embed & External Platform Links Row
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(colorScheme.surfaceVariant.copy(alpha = 0.6f))
                        .border(1.dp, colorScheme.outline, RoundedCornerShape(16.dp))
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = onOpenSpotifyEmbed),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.MusicNote,
                                contentDescription = null,
                                tint = SpotifyGreen,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Full Song Web Player",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = colorScheme.onSurface
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.OpenInNew,
                            contentDescription = "Open Web Player",
                            tint = colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        PlatformPill(
                            name = "Spotify",
                            onClick = { openUrl(context, song.spotifyUrl) },
                            modifier = Modifier.weight(1f)
                        )
                        PlatformPill(
                            name = "Apple Music",
                            onClick = { openUrl(context, song.appleMusicUrl) },
                            modifier = Modifier.weight(1f)
                        )
                        PlatformPill(
                            name = "YouTube",
                            onClick = { openUrl(context, song.youtubeMusicUrl) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickSeekChip(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(colorScheme.surface)
            .border(1.dp, colorScheme.outline, RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = colorScheme.onSurface
        )
    }
}

@Composable
private fun PlatformPill(
    name: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(colorScheme.surface)
            .border(1.dp, colorScheme.outline, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

private fun openUrl(context: Context, url: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

private fun formatTime(ms: Long): String {
    val totalSeconds = (ms / 1000).coerceAtLeast(0)
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%d:%02d", minutes, seconds)
}
