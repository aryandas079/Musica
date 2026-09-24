package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.StormBlackBg
import com.example.ui.theme.WhiteSmoke
import com.example.ui.theme.WhiteSmokeMuted
import com.example.ui.theme.WhiteSmokeSoft
import kotlinx.coroutines.delay

/**
 * Splash Screen in pure Storm Black and White Smoke monochrome aesthetic.
 */
@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scale = remember { Animatable(0.85f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        alpha.animateTo(1f, animationSpec = tween(600, easing = FastOutSlowInEasing))
        scale.animateTo(1f, animationSpec = tween(600, easing = FastOutSlowInEasing))
        delay(1200)
        onSplashFinished()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(StormBlackBg)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onSplashFinished
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Brand Logo in Storm Black and White Smoke
            Canvas(modifier = Modifier.size(160.dp)) {
                val center = Offset(size.width / 2f, size.height / 2f)
                val ringRadius = size.width * 0.28f
                val dotRadius = size.width * 0.07f
                val strokeW = size.width * 0.055f

                // Diagonal White Smoke line passing through
                val lineStart = Offset(size.width * 0.12f, size.height * 0.18f)
                val lineEnd = Offset(size.width * 0.88f, size.height * 0.82f)

                drawLine(
                    color = WhiteSmokeSoft,
                    start = lineStart,
                    end = lineEnd,
                    strokeWidth = 4.dp.toPx(),
                    cap = StrokeCap.Round
                )

                // Circular node at line start
                drawCircle(
                    color = WhiteSmokeSoft.copy(alpha = 0.3f),
                    radius = 14.dp.toPx(),
                    center = lineStart
                )
                drawCircle(
                    color = WhiteSmoke,
                    radius = 7.dp.toPx(),
                    center = lineStart
                )

                // Circular node at line end
                drawCircle(
                    color = WhiteSmokeSoft.copy(alpha = 0.3f),
                    radius = 14.dp.toPx(),
                    center = lineEnd
                )
                drawCircle(
                    color = WhiteSmoke,
                    radius = 7.dp.toPx(),
                    center = lineEnd
                )

                // Outer circular ring in White Smoke
                drawCircle(
                    color = WhiteSmoke,
                    radius = ringRadius,
                    center = center,
                    style = Stroke(width = strokeW)
                )

                // Center solid White Smoke dot
                drawCircle(
                    color = WhiteSmoke,
                    radius = dotRadius,
                    center = center
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Caption
            Text(
                text = "musica by @aryandas",
                fontSize = 17.sp,
                fontWeight = FontWeight.Normal,
                color = WhiteSmokeMuted,
                letterSpacing = 0.5.sp
            )
        }
    }
}
