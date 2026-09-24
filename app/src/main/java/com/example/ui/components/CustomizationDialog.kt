package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.AppThemeMode
import com.example.model.AppearanceMode
import com.example.model.HistoryItem
import com.example.model.Song
import com.example.ui.theme.LocalAppStyle
import com.example.ui.theme.SpotifyGreen
import com.example.ui.theme.liquidGlassEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomizationBottomSheet(
    themeMode: AppThemeMode,
    appearanceMode: AppearanceMode,
    historyItems: List<HistoryItem>,
    isClearDialogOpen: Boolean,
    onSelectTheme: (AppThemeMode) -> Unit,
    onSelectAppearance: (AppearanceMode) -> Unit,
    onRemoveHistoryItem: (Long) -> Unit,
    onClearHistoryClick: () -> Unit,
    onConfirmClearHistory: () -> Unit,
    onDismissClearHistory: () -> Unit,
    onPlaySong: (Song) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val colorScheme = MaterialTheme.colorScheme

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.Transparent,
        dragHandle = null,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.88f)
                .liquidGlassEffect(
                    shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                    elevation = 16.dp
                )
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Customization & Account",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorScheme.onSurface
                        )
                        Text(
                            text = "Theme, appearance and listening history",
                            fontSize = 12.sp,
                            color = colorScheme.onSurfaceVariant
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(colorScheme.onSurface.copy(alpha = 0.08f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = colorScheme.onSurface,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    // SECTION 1: Theme & Visual Identity
                    item {
                        Column {
                            Text(
                                text = "Theme Palette",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = colorScheme.primary,
                                letterSpacing = 0.5.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                ThemeOptionChip(
                                    title = "Storm Black",
                                    icon = Icons.Default.DarkMode,
                                    isSelected = true,
                                    onClick = { onSelectTheme(AppThemeMode.DARK) },
                                    modifier = Modifier.weight(1f)
                                )
                                ThemeOptionChip(
                                    title = "White Smoke",
                                    icon = Icons.Default.LightMode,
                                    isSelected = false,
                                    onClick = { onSelectTheme(AppThemeMode.DARK) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                    // SECTION 2: Surface Finish
                    item {
                        Column {
                            Text(
                                text = "Surface Finish",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = colorScheme.primary,
                                letterSpacing = 0.5.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                AppearanceOptionChip(
                                    title = "Solid Matte",
                                    subtitle = "Storm Obsidian",
                                    icon = Icons.Default.AutoAwesome,
                                    isSelected = true,
                                    onClick = { onSelectAppearance(AppearanceMode.SOLID) },
                                    modifier = Modifier.weight(1f)
                                )
                                AppearanceOptionChip(
                                    title = "Minimalist",
                                    subtitle = "High Contrast",
                                    icon = Icons.Default.Palette,
                                    isSelected = false,
                                    onClick = { onSelectAppearance(AppearanceMode.SOLID) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                    // SECTION 3: Listening History
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Listening History",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(colorScheme.primary.copy(alpha = 0.15f))
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "${historyItems.size}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = colorScheme.primary
                                    )
                                }
                            }

                            if (historyItems.isNotEmpty()) {
                                TextButton(
                                    onClick = onClearHistoryClick,
                                    colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFFF5252))
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DeleteOutline,
                                        contentDescription = "Clear History",
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Clear History",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }

                    if (historyItems.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(colorScheme.onSurface.copy(alpha = 0.04f))
                                    .padding(vertical = 32.dp, horizontal = 16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(32.dp)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "No listening history yet",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Songs you play or search will appear here and shape your personalized recommendations.",
                                        fontSize = 12.sp,
                                        color = colorScheme.onSurfaceVariant,
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                    )
                                }
                            }
                        }
                    } else {
                        items(historyItems, key = { it.historyId }) { item ->
                            HistoryRowItem(
                                historyItem = item,
                                onPlay = { onPlaySong(item.song) },
                                onRemove = { onRemoveHistoryItem(item.historyId) }
                            )
                        }
                    }
                }
            }
        }
    }

    // Confirmation Dialog for Clearing Entire History
    if (isClearDialogOpen) {
        AlertDialog(
            onDismissRequest = onDismissClearHistory,
            title = {
                Text(
                    text = "Clear Listening History?",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Text(
                    text = "This will remove all recently played tracks from your history. Your homescreen recommendations will revert to trending hits until new tracks are played.",
                    fontSize = 13.sp,
                    color = colorScheme.onSurfaceVariant
                )
            },
            confirmButton = {
                TextButton(
                    onClick = onConfirmClearHistory,
                    colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFFF5252))
                ) {
                    Text("Clear All", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissClearHistory) {
                    Text("Cancel")
                }
            },
            containerColor = colorScheme.surface,
            shape = RoundedCornerShape(20.dp)
        )
    }
}

@Composable
private fun ThemeOptionChip(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    val borderCol = if (isSelected) colorScheme.primary else colorScheme.onSurface.copy(alpha = 0.12f)
    val bgCol = if (isSelected) colorScheme.primary.copy(alpha = 0.18f) else colorScheme.onSurface.copy(alpha = 0.04f)

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(bgCol)
            .border(1.2.dp, borderCol, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp, horizontal = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isSelected) colorScheme.primary else colorScheme.onSurfaceVariant,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) colorScheme.primary else colorScheme.onSurface
        )
    }
}

@Composable
private fun AppearanceOptionChip(
    title: String,
    subtitle: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    val borderCol = if (isSelected) colorScheme.primary else colorScheme.onSurface.copy(alpha = 0.12f)
    val bgCol = if (isSelected) colorScheme.primary.copy(alpha = 0.18f) else colorScheme.onSurface.copy(alpha = 0.04f)

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(bgCol)
            .border(1.2.dp, borderCol, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp, horizontal = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isSelected) colorScheme.primary else colorScheme.onSurfaceVariant,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
            color = if (isSelected) colorScheme.primary else colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = subtitle,
            fontSize = 9.sp,
            color = colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun HistoryRowItem(
    historyItem: HistoryItem,
    onPlay: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    val song = historyItem.song

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(colorScheme.onSurface.copy(alpha = 0.05f))
            .clickable(onClick = onPlay)
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Thumbnail
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(RoundedCornerShape(10.dp))
        ) {
            AsyncImage(
                model = song.artworkUrl,
                contentDescription = song.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Title and Artist
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = song.title,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "${song.artist} • 30s preview",
                fontSize = 11.sp,
                color = colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // Play indicator
        IconButton(
            onClick = onPlay,
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Play",
                tint = SpotifyGreen,
                modifier = Modifier.size(20.dp)
            )
        }

        // Cross ('X') button to remove from history
        IconButton(
            onClick = onRemove,
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(colorScheme.onSurface.copy(alpha = 0.06f))
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Remove from history",
                tint = colorScheme.onSurfaceVariant,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
