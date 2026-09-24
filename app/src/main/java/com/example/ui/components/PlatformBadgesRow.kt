package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.model.Song

/**
 * Platform badges row matching Image 5:
 * Four circular icon buttons in a centered horizontal row:
 * Spotify, Apple Music, YouTube Music, Amazon Music.
 */
@Composable
fun PlatformBadgesRow(
    song: Song,
    onOpenSpotifyEmbed: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 1. Spotify
        PlatformCircleBtn(
            iconRes = R.drawable.ic_logo_spotify,
            contentDescription = "Spotify",
            onClick = onOpenSpotifyEmbed
        )

        // 2. Apple Music
        PlatformCircleBtn(
            iconRes = R.drawable.ic_logo_apple_music,
            contentDescription = "Apple Music",
            onClick = { launchUrl(context, song.appleMusicUrl) }
        )

        // 3. YouTube Music
        PlatformCircleBtn(
            iconRes = R.drawable.ic_logo_youtube,
            contentDescription = "YouTube Music",
            onClick = { launchUrl(context, song.youtubeMusicUrl) }
        )

        // 4. Amazon Music
        PlatformCircleBtn(
            iconRes = R.drawable.ic_logo_amazon_music,
            contentDescription = "Amazon Music",
            onClick = { launchUrl(context, song.amazonMusicUrl) }
        )
    }
}

@Composable
private fun PlatformCircleBtn(
    iconRes: Int,
    contentDescription: String,
    onClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    Box(
        modifier = Modifier
            .size(52.dp)
            .clip(CircleShape)
            .background(colorScheme.surfaceVariant)
            .border(1.dp, colorScheme.outline, CircleShape)
            .clickable(onClick = onClick)
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = contentDescription,
            tint = Color.Unspecified,
            modifier = Modifier.size(30.dp)
        )
    }
}

private fun launchUrl(context: Context, url: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
