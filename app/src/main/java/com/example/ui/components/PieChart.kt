package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.TechCyan

@Composable
fun AnimatedPieChart(
    modifier: Modifier = Modifier,
    values: List<Float>,
    colors: List<Color> = listOf(TechCyan, NeonGreen),
    animated: Boolean = true
) {
    val total = values.sum()
    if (total == 0f) return

    val sweepAngles = values.map { (it / total) * 360f }
    
    val transition = rememberInfiniteTransition(label = "pie_rotation")
    val rotation by transition.animateFloat(
        initialValue = 0f,
        targetValue = if (animated) 360f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    val entryAnimation = remember { Animatable(0f) }
    
    LaunchedEffect(Unit) {
        entryAnimation.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1500, easing = FastOutSlowInEasing)
        )
    }

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(150.dp)) {
            val thickness = 24.dp.toPx()
            val size = size.minDimension - thickness
            val topLeft = Offset(thickness / 2f, thickness / 2f)
            
            var startAngle = rotation - 90f // Start from top
            
            sweepAngles.forEachIndexed { index, sweepAngle ->
                val color = colors.getOrElse(index) { Color.Gray }
                val animatedSweep = sweepAngle * entryAnimation.value
                
                drawArc(
                    color = color.copy(alpha = 0.8f),
                    startAngle = startAngle,
                    sweepAngle = animatedSweep,
                    useCenter = false,
                    topLeft = topLeft,
                    size = Size(size, size),
                    style = Stroke(width = thickness, cap = StrokeCap.Butt)
                )
                startAngle += animatedSweep
            }
        }
    }
}
