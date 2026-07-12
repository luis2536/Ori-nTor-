package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.GlassPanel

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    enable3DTilt: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    val finalModifier = if (enable3DTilt) {
        val infiniteTransition = rememberInfiniteTransition(label = "glass_card_tilt")
        val tiltX by infiniteTransition.animateFloat(
            initialValue = -3f,
            targetValue = 3f,
            animationSpec = infiniteRepeatable(
                animation = tween(5000, easing = EaseInOutSine),
                repeatMode = RepeatMode.Reverse
            ),
            label = "tilt_x"
        )
        val tiltY by infiniteTransition.animateFloat(
            initialValue = -4f,
            targetValue = 4f,
            animationSpec = infiniteRepeatable(
                animation = tween(7000, easing = EaseInOutSine),
                repeatMode = RepeatMode.Reverse
            ),
            label = "tilt_y"
        )
        val density = LocalDensity.current.density
        modifier.graphicsLayer {
            rotationX = tiltX
            rotationY = tiltY
            cameraDistance = 14f * density
        }
    } else {
        modifier
    }

    Box(
        modifier = finalModifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        GlassPanel.copy(alpha = 0.8f),
                        GlassPanel.copy(alpha = 0.4f)
                    )
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        GlassBorder.copy(alpha = 0.6f),
                        GlassBorder.copy(alpha = 0.1f)
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            ),
        content = content
    )
}
