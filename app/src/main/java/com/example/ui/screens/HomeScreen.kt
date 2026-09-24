package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.Artist
import com.example.model.HistoryItem
import com.example.model.Song
import com.example.ui.theme.GenreHipHopBg
import com.example.ui.theme.GenreJPopBg
import com.example.ui.theme.GenreKPopBg
import com.example.ui.theme.GenreLatinBg
import com.example.ui.theme.GenrePopBg
import com.example.ui.theme.GenreRnBBg
import com.example.ui.theme.GenreRockBg
import com.example.ui.theme.SpotifyGreen
import com.example.ui.theme.StormBlackBg
import com.example.ui.theme.StormBlackCard
import com.example.ui.theme.StormSlateBorder
import com.example.ui.theme.WhiteSmoke
import com.example.ui.theme.WhiteSmokeMuted
import com.example.ui.theme.liquidGlassEffect

data class GenreItem(
    val name: String,
    val vectorColor: Color,
    val imageUrl: String
)

@Composable
fun HomeScreen(
    featuredSong: Song?,
    trendingSongs: List<Song>,
    categorySongs: List<Song>,
    recommendedSongs: List<Song>,
    selectedCategory: String,
    topArtists: List<Artist>,
    favoriteSongs: List<Song>,
    isLoading: Boolean,
    isPlaying: Boolean,
    onSelectCategory: (String) -> Unit,
    onPlaySong: (Song, List<Song>) -> Unit,
    onOpenSongDetails: (Song) -> Unit,
    onToggleFavorite: (Song) -> Unit,
    onArtistClick: (String) -> Unit,
    onNavigateToSearch: () -> Unit,
    onOpenCustomization: () -> Unit,
    modifier: Modifier = Modifier,
    historyItems: List<HistoryItem> = emptyList(),
    currentPlayingId: Long? = null
) {
    val colorScheme = MaterialTheme.colorScheme

    val genres = listOf(
        GenreItem("Pop", GenrePopBg, "https://cdn-images.dzcdn.net/images/misc/f9e070848998df8870ba65cd0d22b2b3/500x500-000000-80-0-0.jpg"),
        GenreItem("Hip-Hop", GenreHipHopBg, "https://cdn-images.dzcdn.net/images/misc/5b9750b2964e5264b387402a11b8dbbe/500x500-000000-80-0-0.jpg"),
        GenreItem("Rock", GenreRockBg, "https://cdn-images.dzcdn.net/images/misc/f14f9fde9feb38ca6d61960f00681860/500x500-000000-80-0-0.jpg"),
        GenreItem("Latin", GenreLatinBg, "https://cdn-images.dzcdn.net/images/misc/069c9888538799748960781f098b5f4b/500x500-000000-80-0-0.jpg"),
        GenreItem("K-Pop", GenreKPopBg, "https://cdn-images.dzcdn.net/images/misc/dd6d2756465b22488dff5d8663e86688/500x500-000000-80-0-0.jpg"),
        GenreItem("R&B", GenreRnBBg, "https://cdn-images.dzcdn.net/images/misc/3d5e8aab99b95bfa7ac7e9e466e7781e/500x500-000000-80-0-0.jpg")
    )

    val sortedGenres = remember(selectedCategory, historyItems) {
        val historyGenres = historyItems.map { it.song.genre }.distinct()
        genres.sortedByDescending { genre ->
            when {
                genre.name.equals(selectedCategory, ignoreCase = true) -> 100
                genre.name in historyGenres -> 50
                else -> 0
            }
        }
    }

    // Dynamic recommendations based on history & searches
    val displayRecs = recommendedSongs.ifEmpty {
        if (categorySongs.isNotEmpty()) categorySongs else trendingSongs
    }

    // Speed Dial: 4 to 6 items strictly prioritized from actual listening history, then favorites, then top trending
    val speedDialSongs: List<Song> = remember(historyItems, favoriteSongs, trendingSongs, featuredSong) {
        val historySongs = historyItems.map { it.song }
        val pool = (historySongs + favoriteSongs + listOfNotNull(featuredSong) + trendingSongs).distinctBy { it.id }
        pool.take(6)
    }

    val hasHistory = historyItems.isNotEmpty()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = colorScheme.background
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {
            // Top Bar: "Musica" + Search, History/Customization, Profile/Account
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Musica",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorScheme.onBackground,
                            letterSpacing = (-0.5).sp
                        )
                        Text(
                            text = if (hasHistory) "Welcome back • Synced Lyrics" else "Stream • Synced Lyrics • Glass UI",
                            fontSize = 11.sp,
                            color = colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onNavigateToSearch,
                            modifier = Modifier
                                .size(40.dp)
                                .liquidGlassEffect(shape = CircleShape, elevation = 2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = colorScheme.onSurface,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        IconButton(
                            onClick = onOpenCustomization,
                            modifier = Modifier
                                .size(40.dp)
                                .liquidGlassEffect(shape = CircleShape, elevation = 2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Tune,
                                contentDescription = "Customize & History",
                                tint = colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        IconButton(
                            onClick = onOpenCustomization,
                            modifier = Modifier
                                .size(40.dp)
                                .liquidGlassEffect(shape = CircleShape, elevation = 2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Account & Settings",
                                tint = colorScheme.onSurface,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            // Section 1: "Speed Dial" 2-Column Grid (based on listening history)
            if (speedDialSongs.isNotEmpty()) {
                item {
                    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Speed Dial",
                                    fontSize = 19.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (hasHistory) SpotifyGreen.copy(alpha = 0.2f) else colorScheme.primary.copy(alpha = 0.15f))
                                        .padding(horizontal = 7.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = if (hasHistory) "From History" else "Quick Play",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (hasHistory) SpotifyGreen else colorScheme.primary
                                    )
                                }
                            }

                            if (hasHistory) {
                                Text(
                                    text = "${historyItems.size} played",
                                    fontSize = 11.sp,
                                    color = colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // 2-Column Grid for Speed Dial items
                        val pairs = speedDialSongs.chunked(2)
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            pairs.forEach { pair ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    pair.forEach { song ->
                                        val isCurrent = song.id == currentPlayingId
                                        Row(
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(58.dp)
                                                .liquidGlassEffect(
                                                    shape = RoundedCornerShape(12.dp),
                                                    elevation = if (isCurrent) 6.dp else 2.dp
                                                )
                                                .clickable {
                                                    onPlaySong(song, speedDialSongs)
                                                    onOpenSongDetails(song)
                                                },
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            AsyncImage(
                                                model = song.artworkUrl,
                                                contentDescription = song.title,
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier
                                                    .size(58.dp)
                                                    .clip(RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp))
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Column(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .padding(end = 6.dp),
                                                verticalArrangement = Arrangement.Center
                                            ) {
                                                Text(
                                                    text = song.title,
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = if (isCurrent) SpotifyGreen else colorScheme.onSurface,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                                Text(
                                                    text = song.artist,
                                                    fontSize = 11.sp,
                                                    color = colorScheme.onSurfaceVariant,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                            }

                                            if (isCurrent && isPlaying) {
                                                Box(
                                                    modifier = Modifier
                                                        .padding(end = 8.dp)
                                                        .size(22.dp)
                                                        .clip(CircleShape)
                                                        .background(SpotifyGreen),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.GraphicEq,
                                                        contentDescription = "Playing",
                                                        tint = Color.Black,
                                                        modifier = Modifier.size(14.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                    // Balance row if odd count
                                    if (pair.size == 1) {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            // Section 2: "Genres" with Geometric Vector Art
            item {
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    Text(
                        text = "Genres",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.onSurface,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(sortedGenres) { genre ->
                            GenreVectorCard(
                                genre = genre,
                                isSelected = selectedCategory.equals(genre.name, ignoreCase = true),
                                onClick = { onSelectCategory(genre.name) }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            // Section 3: "Artists" Circular Avatars
            item {
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    Text(
                        text = "Artists",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.onSurface,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        items(topArtists) { artist ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .width(80.dp)
                                    .clickable { onArtistClick(artist.name) }
                            ) {
                                Box(contentAlignment = Alignment.BottomEnd) {
                                    Box(
                                        modifier = Modifier
                                            .size(76.dp)
                                            .clip(CircleShape)
                                            .border(2.dp, Color.White.copy(alpha = 0.4f), CircleShape)
                                            .liquidGlassEffect(shape = CircleShape, elevation = 4.dp)
                                    ) {
                                        AsyncImage(
                                            model = artist.imageUrl,
                                            contentDescription = artist.name,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }

                                    Box(
                                        modifier = Modifier
                                            .size(20.dp)
                                            .clip(CircleShape)
                                            .background(WhiteSmoke)
                                            .border(1.5.dp, StormBlackBg, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Verified",
                                            tint = StormBlackBg,
                                            modifier = Modifier.size(13.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = artist.name,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = colorScheme.onSurface,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            // Section 4: "Recommendations" (Dynamic based on history & searches)
            item {
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Recommendations",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = colorScheme.onSurface
                            )
                            Text(
                                text = if (recommendedSongs.isNotEmpty()) "Based on your search & listening history" else "Trending top global tracks",
                                fontSize = 11.sp,
                                color = colorScheme.onSurfaceVariant
                            )
                        }

                        IconButton(
                            onClick = onOpenCustomization,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = "History",
                                tint = colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))

                    if (isLoading && displayRecs.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = colorScheme.primary)
                        }
                    } else {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 20.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(displayRecs) { song ->
                                Column(
                                    modifier = Modifier
                                        .width(170.dp)
                                        .clickable {
                                            onPlaySong(song, displayRecs)
                                            onOpenSongDetails(song)
                                        }
                                ) {
                                    // Album Art Square Card with Liquid Glass
                                    Box(
                                        modifier = Modifier
                                            .size(170.dp)
                                            .liquidGlassEffect(shape = RoundedCornerShape(18.dp), elevation = 6.dp)
                                    ) {
                                        AsyncImage(
                                            model = song.artworkUrl,
                                            contentDescription = song.title,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )

                                        // 30s preview badge
                                        Box(
                                            modifier = Modifier
                                                .align(Alignment.BottomStart)
                                                .padding(8.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(Color.Black.copy(alpha = 0.65f))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = "30s preview",
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        }

                                        // Favorite button
                                        val isFav = favoriteSongs.any { it.id == song.id }
                                        IconButton(
                                            onClick = { onToggleFavorite(song) },
                                            modifier = Modifier
                                                .align(Alignment.TopEnd)
                                                .padding(6.dp)
                                                .size(32.dp)
                                                .clip(CircleShape)
                                                .background(Color.Black.copy(alpha = 0.4f))
                                        ) {
                                            Icon(
                                                imageVector = if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                                contentDescription = "Favorite",
                                                tint = if (isFav) WhiteSmoke else Color.White,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    // Song Title
                                    Text(
                                        text = song.title,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = colorScheme.onSurface,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    // Artist Name
                                    Text(
                                        text = song.artist,
                                        fontSize = 12.sp,
                                        color = colorScheme.onSurfaceVariant,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }
        }
    }
}

@Composable
fun GenreVectorCard(
    genre: GenreItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(140.dp)
            .height(84.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) WhiteSmoke else StormBlackCard)
            .border(
                width = 1.dp,
                color = if (isSelected) WhiteSmoke else StormSlateBorder,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = genre.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) StormBlackBg else WhiteSmoke
            )

            if (isSelected) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(StormBlackBg)
                        .padding(horizontal = 7.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "Selected",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = WhiteSmoke
                    )
                }
            } else {
                Text(
                    text = "Explore",
                    fontSize = 11.sp,
                    color = WhiteSmokeMuted
                )
            }
        }
    }
}
