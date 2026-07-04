package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.TechCyan
import com.example.ui.theme.RedAlert
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun OracleAnimatedCore(
    modifier: Modifier = Modifier,
    size: Dp = 150.dp,
    isActive: Boolean = true
) {
    val infiniteTransition = rememberInfiniteTransition(label = "oracle_rotation")
    
    // Rotation angle
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    // Inner reverse rotation angle
    val innerRotationAngle by infiniteTransition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "inner_rotation"
    )

    // Pulse effect
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    // Glow intensity
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseInOutQuad),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val width = size.toPx()
            val height = size.toPx()
            val center = Offset(width / 2, height / 2)
            val baseRadius = width * 0.35f * pulseScale

            // Draw center glowing core (the Oracle eye)
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        if (isActive) NeonGreen.copy(alpha = glowAlpha) else TechCyan.copy(alpha = glowAlpha),
                        Color.Transparent
                    ),
                    center = center,
                    radius = baseRadius * 0.7f
                ),
                radius = baseRadius * 0.7f,
                center = center
            )

            // Inner solid core
            drawCircle(
                color = if (isActive) NeonGreen else TechCyan,
                radius = baseRadius * 0.15f,
                center = center
            )

            // 1st 3D Angled Ring (Scaled on Y-axis to simulate 3D projection)
            withTransform({
                translate(center.x, center.y)
                scale(scaleX = 1f, scaleY = 0.3f) // Tilt effect for 3D (apply AFTER rotation to keep ellipse static)
                rotate(rotationAngle)
                translate(-center.x, -center.y)
            }) {
                drawCircle(
                    color = if (isActive) NeonGreen.copy(alpha = 0.8f) else TechCyan.copy(alpha = 0.8f),
                    radius = baseRadius * 1.1f,
                    center = center,
                    style = Stroke(width = 3.dp.toPx())
                )
                
                // Outer orbital particle on the ring (fixed geometry placement)
                drawCircle(
                    color = if (isActive) RedAlert else NeonGreen,
                    radius = 8.dp.toPx(),
                    center = Offset(center.x + baseRadius * 1.1f, center.y)
                )
            }

            // 2nd 3D Angled Ring (Inverted tilt and reverse rotation)
            withTransform({
                translate(center.x, center.y)
                scale(scaleX = 0.3f, scaleY = 1.0f) // Vertical tilt effect
                rotate(innerRotationAngle)
                translate(-center.x, -center.y)
            }) {
                drawCircle(
                    color = if (isActive) TechCyan.copy(alpha = 0.6f) else NeonGreen.copy(alpha = 0.6f),
                    radius = baseRadius * 1.3f,
                    center = center,
                    style = Stroke(width = 2.dp.toPx())
                )
                
                // Inner orbital particle (fixed geometry placement)
                drawCircle(
                    color = if (isActive) TechCyan else NeonGreen,
                    radius = 6.dp.toPx(),
                    center = Offset(center.x, center.y - baseRadius * 1.3f)
                )
            }

            // Tech indicators / Concentric ticks (Rotating outer ring)
            rotate(rotationAngle) {
                val numTicks = 24
                val tickLength = 12.dp.toPx()
                val startRadius = baseRadius * 1.4f
                val endRadius = startRadius + tickLength

                for (i in 0 until numTicks) {
                    val angle = (i * 360f / numTicks)
                    val rad = Math.toRadians(angle.toDouble())
                    val startX = center.x + startRadius * cos(rad).toFloat()
                    val startY = center.y + startRadius * sin(rad).toFloat()
                    val endX = center.x + endRadius * cos(rad).toFloat()
                    val endY = center.y + endRadius * sin(rad).toFloat()

                    drawLine(
                        color = (if (isActive) NeonGreen else TechCyan).copy(alpha = 0.4f),
                        start = Offset(startX, startY),
                        end = Offset(endX, endY),
                        strokeWidth = 2.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }
            }

            // Cyberpunk Corner Bracket Decorations
            val bracketOffset = baseRadius * 1.5f
            val bracketLength = 15.dp.toPx()
            val corners = listOf(
                Offset(center.x - bracketOffset, center.y - bracketOffset),
                Offset(center.x + bracketOffset, center.y - bracketOffset),
                Offset(center.x + bracketOffset, center.y + bracketOffset),
                Offset(center.x - bracketOffset, center.y + bracketOffset)
            )

            corners.forEachIndexed { index, corner ->
                val signX = if (index == 1 || index == 2) -1f else 1f
                val signY = if (index == 2 || index == 3) -1f else 1f

                // Draw horizontal line of bracket
                drawLine(
                    color = TechCyan.copy(alpha = 0.5f),
                    start = corner,
                    end = Offset(corner.x + bracketLength * signX, corner.y),
                    strokeWidth = 1.5.dp.toPx()
                )
                // Draw vertical line of bracket
                drawLine(
                    color = TechCyan.copy(alpha = 0.5f),
                    start = corner,
                    end = Offset(corner.x, corner.y + bracketLength * signY),
                    strokeWidth = 1.5.dp.toPx()
                )
            }
        }
    }
}
