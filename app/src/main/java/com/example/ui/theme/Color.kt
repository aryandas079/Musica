package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// ==========================================
// STORM DARK & WHITE SMOKE MONOCHROME THEME
// Strictly the ONLY applicable colors in the app
// ==========================================

// Storm Dark Shades
val StormBlackBg = Color(0xFF0B0C0F)          // Deep Storm Dark (Canvas root)
val StormBlackSurface = Color(0xFF13141A)     // Storm Dark (Cards & Toolbars)
val StormBlackCard = Color(0xFF181A22)        // Storm Dark (Items & Tiles)
val StormBlackElevated = Color(0xFF1F222D)    // Storm Dark (Elevated Modals & Inputs)
val StormSlateBorder = Color(0xFF282C38)      // Crisp Storm Dark Border
val StormDivider = Color(0xFF1D2029)          // Subtle Storm Dark Divider

// White Smoke Shades
val WhiteSmoke = Color(0xFFF5F5F7)            // Primary White Smoke
val WhiteSmokeLight = Color(0xFFFAF9F6)       // Ultra-bright White Smoke
val WhiteSmokeSoft = Color(0xFFD4D6DE)        // Secondary Soft White Smoke
val WhiteSmokeMuted = Color(0xFF8B8F9F)       // Muted White Smoke
val WhiteSmokeDim = Color(0xFF545868)         // Dim White Smoke

// App Theme Mappings (Storm Dark & White Smoke)
val MusicaBackground = StormBlackBg
val MusicaSurface = StormBlackSurface
val MusicaCardBg = StormBlackCard
val MusicaInputBg = StormBlackElevated
val MusicaBorderLight = StormSlateBorder

val MusicaTextDark = WhiteSmoke
val MusicaTextMuted = WhiteSmokeMuted
val MusicaTextPurple = WhiteSmoke
val MusicaTextTeal = WhiteSmokeSoft

// Brand & Accent Colors - Mapped strictly to White Smoke & Storm Dark
val MusicaPurplePrimary = WhiteSmoke
val MusicaPurpleLight = WhiteSmokeSoft
val MusicaTeal = WhiteSmokeSoft

val SpotifyGreen = WhiteSmoke
val SpotifyEmbedBg = StormBlackSurface
val AppleMusicRed = WhiteSmoke
val YouTubeRed = WhiteSmoke
val AmazonMusicBlue = WhiteSmoke

// Genre Cards - Refined Monochrome Storm Dark Shades
val GenrePopBg = Color(0xFF14161E)
val GenreHipHopBg = Color(0xFF1A1C24)
val GenreRockBg = Color(0xFF161820)
val GenreLatinBg = Color(0xFF1E212A)
val GenreKPopBg = Color(0xFF181A22)
val GenreJPopBg = Color(0xFF15171F)
val GenreRnBBg = Color(0xFF1C1E26)
