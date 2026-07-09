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

    // Third ring rotation angle
    val outerRotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "outer_rotation"
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

            val primaryColor = if (isActive) NeonGreen else TechCyan
            val secondaryColor = if (isActive) TechCyan else NeonGreen

            // Draw center glowing core (the Oracle eye)
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        primaryColor.copy(alpha = glowAlpha),
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
                color = primaryColor,
                radius = baseRadius * 0.15f,
                center = center
            )

            // 1st 3D Angled Ring (Horizontal-ish)
            withTransform({
                translate(center.x, center.y)
                scale(scaleX = 1f, scaleY = 0.35f) // Tilt effect for 3D
                rotate(rotationAngle)
                translate(-center.x, -center.y)
            }) {
                drawCircle(
                    color = primaryColor.copy(alpha = 0.8f),
                    radius = baseRadius * 1.1f,
                    center = center,
                    style = Stroke(width = 3.dp.toPx(), pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(20f, 10f), 0f))
                )
                
                // Outer orbital particle on the ring
                drawCircle(
                    color = RedAlert,
                    radius = 8.dp.toPx(),
                    center = Offset(center.x + baseRadius * 1.1f, center.y)
                )
                drawCircle(
                    color = primaryColor,
                    radius = 4.dp.toPx(),
                    center = Offset(center.x - baseRadius * 1.1f, center.y)
                )
            }

            // 2nd 3D Angled Ring (Vertical-ish)
            withTransform({
                translate(center.x, center.y)
                rotate(45f) // Offset the ellipse angle itself
                scale(scaleX = 0.35f, scaleY = 1.0f) // Vertical tilt effect
                rotate(innerRotationAngle) // Counter-rotate contents
                translate(-center.x, -center.y)
            }) {
                drawCircle(
                    color = secondaryColor.copy(alpha = 0.6f),
                    radius = baseRadius * 1.3f,
                    center = center,
                    style = Stroke(width = 2.dp.toPx())
                )
                
                // Inner orbital particle
                drawCircle(
                    color = secondaryColor,
                    radius = 6.dp.toPx(),
                    center = Offset(center.x, center.y - baseRadius * 1.3f)
                )
            }
            
            // 3rd 3D Angled Ring (Opposite Vertical-ish)
            withTransform({
                translate(center.x, center.y)
                rotate(-45f) // Offset the ellipse angle itself
                scale(scaleX = 0.35f, scaleY = 1.0f) // Vertical tilt effect
                rotate(outerRotationAngle) // Forward-rotate contents
                translate(-center.x, -center.y)
            }) {
                drawCircle(
                    color = RedAlert.copy(alpha = 0.5f),
                    radius = baseRadius * 1.5f,
                    center = center,
                    style = Stroke(width = 1.dp.toPx())
                )
                
                // Outer orbital particle
                drawCircle(
                    color = RedAlert,
                    radius = 5.dp.toPx(),
                    center = Offset(center.x, center.y + baseRadius * 1.5f)
                )
            }

            // Tech indicators / Concentric ticks (Rotating outer ring)
            rotate(rotationAngle) {
                val numTicks = 36
                val tickLength = 12.dp.toPx()
                val startRadius = baseRadius * 1.6f
                val endRadius = startRadius + tickLength

                for (i in 0 until numTicks) {
                    val angle = (i * 360f / numTicks)
                    if (i % 3 == 0) continue // Leave gaps for tech look
                    val rad = Math.toRadians(angle.toDouble())
                    val startX = center.x + startRadius * cos(rad).toFloat()
                    val startY = center.y + startRadius * sin(rad).toFloat()
                    val endX = center.x + endRadius * cos(rad).toFloat()
                    val endY = center.y + endRadius * sin(rad).toFloat()

                    drawLine(
                        color = primaryColor.copy(alpha = 0.5f),
                        start = Offset(startX, startY),
                        end = Offset(endX, endY),
                        strokeWidth = (if (i % 2 == 0) 2.dp else 1.dp).toPx(),
                        cap = StrokeCap.Round
                    )
                }
            }

            // Cyberpunk Corner Bracket Decorations
            val bracketOffset = baseRadius * 1.8f
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
                    color = TechCyan.copy(alpha = 0.8f),
                    start = corner,
                    end = Offset(corner.x + bracketLength * signX, corner.y),
                    strokeWidth = 2.dp.toPx()
                )
                // Draw vertical line of bracket
                drawLine(
                    color = TechCyan.copy(alpha = 0.8f),
                    start = corner,
                    end = Offset(corner.x, corner.y + bracketLength * signY),
                    strokeWidth = 2.dp.toPx()
                )
                
                // Add a small dot to the corner
                drawCircle(
                    color = primaryColor,
                    radius = 2.dp.toPx(),
                    center = corner
                )
            }
        }
    }
}

