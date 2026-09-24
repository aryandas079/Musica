package com.example.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.model.AppThemeMode
import com.example.model.AppearanceMode

data class AppStyleState(
    val themeMode: AppThemeMode = AppThemeMode.DARK,
    val appearanceMode: AppearanceMode = AppearanceMode.SOLID
)

val LocalAppStyle = compositionLocalOf { AppStyleState() }

@Composable
fun Modifier.liquidGlassEffect(
    shape: Shape = RoundedCornerShape(16.dp),
    elevation: Dp = 0.dp,
    intensity: Float = 1.0f
): Modifier {
    // Liquid glass removed: Clean, solid Storm Black card with refined Storm Slate border
    return this
        .clip(shape)
        .background(StormBlackCard)
        .border(
            width = 1.dp,
            color = StormSlateBorder,
            shape = shape
        )
}

@Composable
fun Modifier.stormElevated(
    shape: Shape = RoundedCornerShape(16.dp)
): Modifier {
    return this
        .clip(shape)
        .background(StormBlackElevated)
        .border(
            width = 1.dp,
            color = StormSlateBorder,
            shape = shape
        )
}

@Composable
fun Modifier.stormSurface(
    shape: Shape = RoundedCornerShape(14.dp)
): Modifier {
    return this
        .clip(shape)
        .background(StormBlackSurface)
        .border(
            width = 1.dp,
            color = StormSlateBorder,
            shape = shape
        )
}

@Composable
fun LiquidGlassBox(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    elevation: Dp = 0.dp,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier.liquidGlassEffect(shape = shape, elevation = elevation),
        content = content
    )
}
