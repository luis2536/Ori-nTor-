package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CurrencyBitcoin
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassCard
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun DashboardScreen() {
    var isMining by remember { mutableStateOf(false) }
    var currentHashrate by remember { mutableStateOf(0) }
    var cpuTemp by remember { mutableStateOf(45) }
    var ramUsage by remember { mutableStateOf(2.1f) }
    val hashrateHistory = remember { mutableStateListOf<Int>() }

    // Simulation loop
    LaunchedEffect(isMining) {
        if (!isMining) {
            currentHashrate = 0
            hashrateHistory.clear()
            cpuTemp = 45
            ramUsage = 2.1f
            return@LaunchedEffect
        }
        
        // Initial setup for graph
        for (i in 0..20) hashrateHistory.add(0)

        while (isMining) {
            delay(1000)
            currentHashrate = Random.nextInt(1200, 3500)
            cpuTemp = Random.nextInt(65, 85)
            ramUsage = 3.5f + Random.nextFloat() * 1.5f
            
            hashrateHistory.add(currentHashrate)
            if (hashrateHistory.size > 20) {
                hashrateHistory.removeAt(0)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "NODO LOCAL",
            color = TechCyan,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatusCard(
                modifier = Modifier.weight(1f),
                title = "CPU TEMP",
                value = "${cpuTemp}°C",
                icon = Icons.Default.Speed,
                color = if (cpuTemp > 80) RedAlert else NeonGreen
            )
            StatusCard(
                modifier = Modifier.weight(1f),
                title = "RAM",
                value = String.format("%.1f GB", ramUsage),
                icon = Icons.Default.Memory,
                color = TechCyan
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Hashrate Chart
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("HASHRATE (H/s)", color = TextSecondary, fontSize = 12.sp)
                    Text("$currentHashrate", color = NeonGreen, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                HashrateChart(data = hashrateHistory, modifier = Modifier.fillMaxSize())
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Master Switch
        val glowAlpha by animateFloatAsState(
            targetValue = if (isMining) 1f else 0f,
            animationSpec = tween(1000)
        )

        Button(
            onClick = { isMining = !isMining },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isMining) GlassPanel else NeonGreen
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.PowerSettingsNew,
                contentDescription = null,
                tint = if (isMining) RedAlert else DarkBackground
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = if (isMining) "DETENER SOC" else "INICIAR SOC DE MINERÍA",
                color = if (isMining) RedAlert else DarkBackground,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Withdrawal Request
        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CurrencyBitcoin, contentDescription = null, tint = TechCyan)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("SALDO ACUMULADO", color = TechCyan, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("0.00421 BTC", color = TextPrimary, fontSize = 24.sp)
                Text("~ $284.50 USD", color = TextSecondary, fontSize = 12.sp)
                
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { /* Simulate withdrawal */ },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = GlassPanel),
                    border = androidx.compose.foundation.BorderStroke(1.dp, TechCyan)
                ) {
                    Text("SOLICITAR COBRO", color = TechCyan)
                }
            }
        }
    }
}

@Composable
fun StatusCard(modifier: Modifier = Modifier, title: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: androidx.compose.ui.graphics.Color) {
    GlassCard(modifier = modifier) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, color = TextSecondary, fontSize = 12.sp)
            Text(value, color = color, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }
    }
}

@Composable
fun HashrateChart(data: List<Int>, modifier: Modifier = Modifier) {
    if (data.isEmpty()) {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Text("NO DATA", color = TextSecondary)
        }
        return
    }

    val maxHash = data.maxOrNull()?.coerceAtLeast(1) ?: 1
    
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val stepX = width / (data.size - 1).coerceAtLeast(1)
        
        val path = Path()
        
        data.forEachIndexed { index, value ->
            val x = index * stepX
            val y = height - (value.toFloat() / maxHash) * height
            
            if (index == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }
        
        drawPath(
            path = path,
            color = NeonGreen,
            style = Stroke(
                width = 3.dp.toPx(),
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
    }
}
