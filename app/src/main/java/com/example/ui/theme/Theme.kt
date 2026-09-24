package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.example.model.AppThemeMode
import com.example.model.AppearanceMode

private val StormBlackColorScheme = darkColorScheme(
    primary = WhiteSmoke,
    onPrimary = StormBlackBg,
    secondary = WhiteSmokeSoft,
    onSecondary = StormBlackBg,
    background = StormBlackBg,
    onBackground = WhiteSmoke,
    surface = StormBlackSurface,
    onSurface = WhiteSmoke,
    surfaceVariant = StormBlackCard,
    onSurfaceVariant = WhiteSmokeMuted,
    outline = StormSlateBorder
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF1DB954),
    onPrimary = Color.White,
    secondary = Color(0xFF1ED760),
    onSecondary = Color.White,
    background = Color(0xFFF8F9FA),
    onBackground = Color(0xFF121212),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF121212),
    surfaceVariant = Color(0xFFF1F3F5),
    onSurfaceVariant = Color(0xFF495057),
    outline = Color(0xFFDEE2E6)
)

private val TintedColorScheme = darkColorScheme(
    primary = Color(0xFF1DB954),
    onPrimary = Color.Black,
    secondary = Color(0xFF1ED760),
    onSecondary = Color.Black,
    background = Color(0xFF0F172A),
    onBackground = Color(0xFFF8FAFC),
    surface = Color(0xFF1E293B),
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = Color(0xFF334155),
    onSurfaceVariant = Color(0xFF94A3B8),
    outline = Color(0xFF475569)
)

@Composable
fun MyApplicationTheme(
    themeMode: AppThemeMode = AppThemeMode.DARK,
    appearanceMode: AppearanceMode = AppearanceMode.SOLID,
    content: @Composable () -> Unit
) {
    val colorScheme = when (themeMode) {
        AppThemeMode.LIGHT -> LightColorScheme
        AppThemeMode.DARK -> StormBlackColorScheme
        AppThemeMode.TINTED -> TintedColorScheme
    }

    val styleState = AppStyleState(
        themeMode = themeMode,
        appearanceMode = appearanceMode
    )

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            val isLight = themeMode == AppThemeMode.LIGHT
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = isLight
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = isLight
        }
    }

    CompositionLocalProvider(LocalAppStyle provides styleState) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
