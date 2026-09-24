package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.CustomizationBottomSheet
import com.example.ui.components.MiniPlayer
import com.example.ui.components.SpotifyEmbedDialog
import com.example.ui.screens.ArtistScreen
import com.example.ui.screens.FavoritesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LyricsScreen
import com.example.ui.screens.NowPlayingScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.theme.LocalAppStyle
import com.example.ui.theme.SpotifyGreen
import com.example.ui.theme.liquidGlassEffect
import com.example.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicaApp(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    var showSplash by remember { mutableStateOf(true) }
    var selectedTab by remember { mutableIntStateOf(0) }
    var viewingArtistName by remember { mutableStateOf<String?>(null) }
    var isLyricsScreenOpen by remember { mutableStateOf(false) }

    val colorScheme = MaterialTheme.colorScheme

    val currentSong by viewModel.currentSong.collectAsStateWithLifecycle()
    val isPlaying by viewModel.isPlaying.collectAsStateWithLifecycle()
    val isBuffering by viewModel.isBuffering.collectAsStateWithLifecycle()
    val currentPositionMs by viewModel.currentPositionMs.collectAsStateWithLifecycle()
    val durationMs by viewModel.durationMs.collectAsStateWithLifecycle()
    val isShuffle by viewModel.isShuffle.collectAsStateWithLifecycle()
    val isLooping by viewModel.isLooping.collectAsStateWithLifecycle()

    val featuredSong by viewModel.featuredSong.collectAsStateWithLifecycle()
    val trendingSongs by viewModel.trendingSongs.collectAsStateWithLifecycle()
    val categorySongs by viewModel.categorySongs.collectAsStateWithLifecycle()
    val recommendedSongs by viewModel.recommendedSongs.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val topArtists by viewModel.topArtists.collectAsStateWithLifecycle()
    val isHomescreenLoading by viewModel.isHomescreenLoading.collectAsStateWithLifecycle()

    val artistSongs by viewModel.artistSongs.collectAsStateWithLifecycle()
    val isArtistLoading by viewModel.isArtistLoading.collectAsStateWithLifecycle()
    val currentArtist by viewModel.currentArtist.collectAsStateWithLifecycle()

    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val searchResults by viewModel.searchResults.collectAsStateWithLifecycle()
    val isSearching by viewModel.isSearching.collectAsStateWithLifecycle()

    val favoriteSongs by viewModel.favoriteSongs.collectAsStateWithLifecycle()
    val historyItems by viewModel.historyItems.collectAsStateWithLifecycle()

    val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
    val appearanceMode by viewModel.appearanceMode.collectAsStateWithLifecycle()
    val isCustomizationVisible by viewModel.isCustomizationVisible.collectAsStateWithLifecycle()
    val isClearHistoryDialogVisible by viewModel.isClearHistoryDialogVisible.collectAsStateWithLifecycle()

    val lyricsData by viewModel.lyricsData.collectAsStateWithLifecycle()
    val isLyricsLoading by viewModel.isLyricsLoading.collectAsStateWithLifecycle()
    val selectedLanguage by viewModel.selectedLanguage.collectAsStateWithLifecycle()

    val isNowPlayingExpanded by viewModel.isNowPlayingExpanded.collectAsStateWithLifecycle()
    val isSpotifyEmbedVisible by viewModel.isSpotifyEmbedVisible.collectAsStateWithLifecycle()

    val spotifySheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Splash Screen matching Image 1
    if (showSplash) {
        SplashScreen(onSplashFinished = { showSplash = false })
        return
    }

    Box(modifier = modifier.fillMaxSize().background(colorScheme.background)) {
        Scaffold(
            containerColor = colorScheme.background,
            bottomBar = {
                Column(modifier = Modifier.navigationBarsPadding()) {
                    // Docked MiniPlayer with Liquid Glass styling
                    if (!isNowPlayingExpanded && !isLyricsScreenOpen && currentSong != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 4.dp)
                                .liquidGlassEffect(shape = RoundedCornerShape(18.dp), elevation = 8.dp)
                        ) {
                            MiniPlayer(
                                song = currentSong,
                                isPlaying = isPlaying,
                                isBuffering = isBuffering,
                                currentPositionMs = currentPositionMs,
                                durationMs = durationMs,
                                onTogglePlayPause = { viewModel.togglePlayPause() },
                                onNext = { viewModel.playNext() },
                                onPrevious = { viewModel.playPrevious() },
                                onSeek = { viewModel.seekTo(it) },
                                onExpand = { viewModel.openNowPlaying() }
                            )
                        }
                    }

                    // Bottom Navigation Bar with Glass/Theme adaptation
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .liquidGlassEffect(
                                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                                elevation = 10.dp
                            )
                    ) {
                        NavigationBar(
                            containerColor = Color.Transparent,
                            contentColor = colorScheme.onSurface,
                            tonalElevation = 0.dp
                        ) {
                            NavigationBarItem(
                                selected = selectedTab == 0 && viewingArtistName == null,
                                onClick = {
                                    selectedTab = 0
                                    viewingArtistName = null
                                },
                                icon = {
                                    Icon(
                                        imageVector = if (selectedTab == 0) Icons.Filled.Home else Icons.Outlined.Home,
                                        contentDescription = "Discover"
                                    )
                                },
                                label = { Text("Discover", fontSize = 11.sp) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = colorScheme.primary,
                                    selectedTextColor = colorScheme.primary,
                                    indicatorColor = colorScheme.primary.copy(alpha = 0.15f),
                                    unselectedIconColor = colorScheme.onSurfaceVariant,
                                    unselectedTextColor = colorScheme.onSurfaceVariant
                                )
                            )

                            NavigationBarItem(
                                selected = selectedTab == 1 && viewingArtistName == null,
                                onClick = {
                                    selectedTab = 1
                                    viewingArtistName = null
                                },
                                icon = {
                                    Icon(
                                        imageVector = if (selectedTab == 1) Icons.Filled.Search else Icons.Outlined.Search,
                                        contentDescription = "Search"
                                    )
                                },
                                label = { Text("Search", fontSize = 11.sp) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = colorScheme.primary,
                                    selectedTextColor = colorScheme.primary,
                                    indicatorColor = colorScheme.primary.copy(alpha = 0.15f),
                                    unselectedIconColor = colorScheme.onSurfaceVariant,
                                    unselectedTextColor = colorScheme.onSurfaceVariant
                                )
                            )

                            NavigationBarItem(
                                selected = selectedTab == 2 && viewingArtistName == null,
                                onClick = {
                                    selectedTab = 2
                                    viewingArtistName = null
                                },
                                icon = {
                                    Icon(
                                        imageVector = if (selectedTab == 2) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                        contentDescription = "Library"
                                    )
                                },
                                label = { Text("Library", fontSize = 11.sp) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = colorScheme.primary,
                                    selectedTextColor = colorScheme.primary,
                                    indicatorColor = colorScheme.primary.copy(alpha = 0.15f),
                                    unselectedIconColor = colorScheme.onSurfaceVariant,
                                    unselectedTextColor = colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    }
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = paddingValues.calculateBottomPadding())
            ) {
                // If viewing Artist page (Image 7 & 8)
                if (viewingArtistName != null) {
                    ArtistScreen(
                        artistName = viewingArtistName!!,
                        topArtists = topArtists,
                        artistSongs = artistSongs,
                        isLoading = isArtistLoading,
                        favoriteSongs = favoriteSongs,
                        currentPlayingId = currentSong?.id,
                        isPlaying = isPlaying,
                        onPlaySong = { s, p -> viewModel.playSong(s, p) },
                        onOpenSongDetails = { s -> viewModel.openNowPlaying(s) },
                        onToggleFavorite = { s -> viewModel.toggleFavorite(s) },
                        onBack = { viewingArtistName = null },
                        currentArtist = currentArtist
                    )
                } else {
                    when (selectedTab) {
                        0 -> HomeScreen(
                            featuredSong = featuredSong,
                            trendingSongs = trendingSongs,
                            categorySongs = categorySongs,
                            recommendedSongs = recommendedSongs,
                            selectedCategory = selectedCategory,
                            topArtists = topArtists,
                            favoriteSongs = favoriteSongs,
                            isLoading = isHomescreenLoading,
                            isPlaying = isPlaying,
                            onSelectCategory = { viewModel.selectCategory(it) },
                            onPlaySong = { song, playlist -> viewModel.playSong(song, playlist) },
                            onOpenSongDetails = { viewModel.openNowPlaying(it) },
                            onToggleFavorite = { viewModel.toggleFavorite(it) },
                            onArtistClick = { artistName ->
                                viewingArtistName = artistName
                                viewModel.loadArtistSongs(artistName)
                            },
                            onNavigateToSearch = { selectedTab = 1 },
                            onOpenCustomization = { viewModel.showCustomizationDialog(true) },
                            historyItems = historyItems,
                            currentPlayingId = currentSong?.id
                        )

                        1 -> SearchScreen(
                            query = searchQuery,
                            searchResults = searchResults,
                            isSearching = isSearching,
                            currentPlayingId = currentSong?.id,
                            isPlaying = isPlaying,
                            favoriteSongs = favoriteSongs,
                            onQueryChanged = { viewModel.onSearchQueryChanged(it) },
                            onPlaySong = { song, playlist -> viewModel.playSong(song, playlist) },
                            onOpenSongDetails = { viewModel.openNowPlaying(it) },
                            onToggleFavorite = { viewModel.toggleFavorite(it) }
                        )

                        2 -> FavoritesScreen(
                            favoriteSongs = favoriteSongs,
                            currentPlayingId = currentSong?.id,
                            isPlaying = isPlaying,
                            onPlaySong = { song, playlist -> viewModel.playSong(song, playlist) },
                            onOpenSongDetails = { viewModel.openNowPlaying(it) },
                            onToggleFavorite = { viewModel.toggleFavorite(it) }
                        )
                    }
                }
            }
        }

        // Customization & Listening History Bottom Sheet
        if (isCustomizationVisible) {
            CustomizationBottomSheet(
                themeMode = themeMode,
                appearanceMode = appearanceMode,
                historyItems = historyItems,
                isClearDialogOpen = isClearHistoryDialogVisible,
                onSelectTheme = { viewModel.setThemeMode(it) },
                onSelectAppearance = { viewModel.setAppearanceMode(it) },
                onRemoveHistoryItem = { viewModel.removeFromHistory(it) },
                onClearHistoryClick = { viewModel.showClearHistoryDialog(true) },
                onConfirmClearHistory = { viewModel.clearAllHistory() },
                onDismissClearHistory = { viewModel.showClearHistoryDialog(false) },
                onPlaySong = { song -> viewModel.playSong(song) },
                onDismiss = { viewModel.showCustomizationDialog(false) }
            )
        }

        // Full Screen Song Details Overlay (matching Image 5)
        AnimatedVisibility(
            visible = isNowPlayingExpanded && !isLyricsScreenOpen && currentSong != null,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
        ) {
            val isCurrentFav = currentSong?.let { s -> favoriteSongs.any { it.id == s.id } } ?: false

            NowPlayingScreen(
                song = currentSong,
                isPlaying = isPlaying,
                isBuffering = isBuffering,
                currentPositionMs = currentPositionMs,
                durationMs = durationMs,
                isShuffle = isShuffle,
                isLooping = isLooping,
                isFavorite = isCurrentFav,
                onTogglePlayPause = { viewModel.togglePlayPause() },
                onSeek = { viewModel.seekTo(it) },
                onSeekBy = { viewModel.seekBy(it) },
                onNext = { viewModel.playNext() },
                onPrevious = { viewModel.playPrevious() },
                onToggleShuffle = { viewModel.toggleShuffle() },
                onToggleLoop = { viewModel.toggleLooping() },
                onToggleFavorite = { currentSong?.let { viewModel.toggleFavorite(it) } },
                onClose = { viewModel.closeNowPlaying() },
                onOpenSpotifyEmbed = { viewModel.toggleSpotifyEmbed(true) },
                onOpenLyrics = { isLyricsScreenOpen = true },
                onArtistClick = { artistName ->
                    viewModel.closeNowPlaying()
                    viewModel.loadArtistSongs(artistName)
                    viewingArtistName = artistName
                }
            )
        }

        // Dedicated Lyrics Screen Overlay (matching Image 6)
        AnimatedVisibility(
            visible = isLyricsScreenOpen && currentSong != null,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
        ) {
            currentSong?.let { song ->
                LyricsScreen(
                    song = song,
                    lyricsData = lyricsData,
                    isLyricsLoading = isLyricsLoading,
                    selectedLanguage = selectedLanguage,
                    currentPositionMs = currentPositionMs,
                    durationMs = durationMs,
                    isPlaying = isPlaying,
                    onSeek = { viewModel.seekTo(it) },
                    onTogglePlayPause = { viewModel.togglePlayPause() },
                    onNext = { viewModel.playNext() },
                    onPrevious = { viewModel.playPrevious() },
                    onSelectLanguage = { viewModel.setTargetLanguage(it) },
                    onBack = { isLyricsScreenOpen = false }
                )
            }
        }

        // Spotify Embed Modal Bottom Sheet
        if (isSpotifyEmbedVisible && currentSong != null) {
            ModalBottomSheet(
                onDismissRequest = { viewModel.toggleSpotifyEmbed(false) },
                sheetState = spotifySheetState,
                containerColor = colorScheme.surface,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            ) {
                SpotifyEmbedDialog(
                    song = currentSong!!,
                    onDismiss = { viewModel.toggleSpotifyEmbed(false) }
                )
            }
        }
    }
}
