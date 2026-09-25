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

val StormDarkColorScheme = darkColorScheme(
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
    outline = StormSlateBorder,
    error = WhiteSmokeSoft,
    onError = StormBlackBg
)

@Composable
fun MyApplicationTheme(
    themeMode: AppThemeMode = AppThemeMode.DARK,
    appearanceMode: AppearanceMode = AppearanceMode.SOLID,
    content: @Composable () -> Unit
) {
    // Strictly Storm Dark & White Smoke exclusively
    val colorScheme = StormDarkColorScheme

    val styleState = AppStyleState(
        themeMode = AppThemeMode.DARK,
        appearanceMode = appearanceMode
    )

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            // Always dark status bars and navigation bars
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
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
