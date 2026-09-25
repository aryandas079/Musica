package com.example.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.model.AppThemeMode
import com.example.model.AppearanceMode

data class AppStyleState(
    val themeMode: AppThemeMode = AppThemeMode.DARK,
    val appearanceMode: AppearanceMode = AppearanceMode.LIQUID_GLASS
)

val LocalAppStyle = compositionLocalOf { AppStyleState() }

@Composable
fun Modifier.liquidGlassEffect(
    shape: Shape = RoundedCornerShape(16.dp),
    elevation: Dp = 0.dp,
    intensity: Float = 1.0f
): Modifier {
    val style = LocalAppStyle.current
    val colorScheme = MaterialTheme.colorScheme

    val (bgBrush, borderColor) = when (style.appearanceMode) {
        AppearanceMode.LIQUID_GLASS -> Pair(
            Brush.verticalGradient(
                listOf(
                    StormBlackCard.copy(alpha = 0.92f),
                    StormBlackSurface.copy(alpha = 0.95f)
                )
            ),
            StormSlateBorder
        )
        AppearanceMode.BLUR -> Pair(
            Brush.verticalGradient(
                listOf(
                    StormBlackElevated.copy(alpha = 0.78f),
                    StormBlackBg.copy(alpha = 0.85f)
                )
            ),
            WhiteSmoke.copy(alpha = 0.15f)
        )
        AppearanceMode.SOLID -> Pair(
            Brush.linearGradient(listOf(StormBlackCard, StormBlackCard)),
            StormSlateBorder
        )
    }

    return this
        .clip(shape)
        .background(bgBrush)
        .border(
            width = 1.dp,
            color = borderColor,
            shape = shape
        )
}

@Composable
fun Modifier.stormElevated(
    shape: Shape = RoundedCornerShape(16.dp)
): Modifier {
    val colorScheme = MaterialTheme.colorScheme
    return this
        .clip(shape)
        .background(colorScheme.surfaceVariant)
        .border(
            width = 1.dp,
            color = colorScheme.outline,
            shape = shape
        )
}

@Composable
fun Modifier.stormSurface(
    shape: Shape = RoundedCornerShape(14.dp)
): Modifier {
    val colorScheme = MaterialTheme.colorScheme
    return this
        .clip(shape)
        .background(colorScheme.surface)
        .border(
            width = 1.dp,
            color = colorScheme.outline,
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
