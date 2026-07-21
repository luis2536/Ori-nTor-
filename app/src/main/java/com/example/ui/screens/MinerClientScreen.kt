package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.miner.MinerWorker
import com.example.ui.components.GlassCard
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun GlassGauge(
    progress: Float,
    label: String,
    valueLabel: String,
    primaryColor: Color,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(1500)
    )

    Box(contentAlignment = Alignment.Center, modifier = modifier.size(100.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 8.dp.toPx()
            val innerRadius = (size.minDimension - strokeWidth) / 2
            val centerOffset = Offset(size.width / 2, size.height / 2)

            // Background Track
            drawArc(
                color = GlassBorder,
                startAngle = 135f,
                sweepAngle = 270f,
                useCenter = false,
                topLeft = Offset(centerOffset.x - innerRadius, centerOffset.y - innerRadius),
                size = Size(innerRadius * 2, innerRadius * 2),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Foreground Progress
            drawArc(
                brush = Brush.sweepGradient(listOf(primaryColor.copy(alpha=0.5f), primaryColor, primaryColor)),
                startAngle = 135f,
                sweepAngle = 270f * animatedProgress,
                useCenter = false,
                topLeft = Offset(centerOffset.x - innerRadius, centerOffset.y - innerRadius),
                size = Size(innerRadius * 2, innerRadius * 2),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(valueLabel, color = primaryColor, fontSize = 16.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
            Text(label, color = TextSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun RealTimeChart(
    data: List<Float>,
    modifier: Modifier = Modifier,
    lineColor: Color = NeonGreen
) {
    Canvas(modifier = modifier) {
        if (data.isEmpty()) return@Canvas
        
        val maxVal = (data.maxOrNull() ?: 5000f).coerceAtLeast(1000f)
        val maxItems = 20
        val stepX = size.width / (maxItems - 1).coerceAtLeast(1)
        
        val path = androidx.compose.ui.graphics.Path()
        val fillPath = androidx.compose.ui.graphics.Path()
        
        val normalizedData = if (data.size < maxItems) {
            List(maxItems - data.size) { 0f } + data
        } else {
            data.takeLast(maxItems)
        }
        
        normalizedData.forEachIndexed { index, value ->
            val x = index * stepX
            val y = size.height - (value / maxVal * size.height)
            
            if (index == 0) {
                path.moveTo(x, y)
                fillPath.moveTo(x, size.height)
                fillPath.lineTo(x, y)
            } else {
                path.lineTo(x, y)
                fillPath.lineTo(x, y)
            }
            
            if (index == normalizedData.lastIndex) {
                fillPath.lineTo(x, size.height)
                fillPath.close()
            }
        }
        
        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(lineColor.copy(alpha = 0.3f), Color.Transparent),
                startY = 0f,
                endY = size.height
            )
        )
        
        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = androidx.compose.ui.graphics.StrokeJoin.Round)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MinerClientScreen(onNavigateBack: () -> Unit) {
    val context = LocalContext.current
    var isMining by remember { mutableStateOf(false) }
    var masterIp by remember { mutableStateOf("api.orionthor.io:8443") }
    var minerId by remember { mutableStateOf("SLAVE_NODE_${Random.nextInt(1000, 9999)}") }
    var personalWallet by remember { mutableStateOf("") }
    
    val miningStats by com.example.miner.MiningEngine.stats.collectAsState()
    var miningPower by remember { mutableFloatStateOf(80f) }
    
    val hashrateHistory = remember { mutableStateListOf<Float>() }

    // Synchronize power level with engine
    LaunchedEffect(miningPower) {
        com.example.miner.MiningEngine.updatePower(miningPower)
    }

    LaunchedEffect(isMining) {
        if (isMining) {
            com.example.miner.MiningEngine.startEngine(miningPower)
        } else {
            com.example.miner.MiningEngine.stopEngine()
        }
    }
    
    // Track hashrate history for the chart separately to avoid tying the chart too closely to the core loop
    LaunchedEffect(miningStats.hashrate) {
        if (isMining && miningStats.hashrate > 0) {
            hashrateHistory.add(miningStats.hashrate.toFloat())
            if (hashrateHistory.size > 20) hashrateHistory.removeAt(0)
        } else if (!isMining) {
            hashrateHistory.add(0f)
            if (hashrateHistory.size > 20) hashrateHistory.removeAt(0)
        }
    }

    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            TopAppBar(
                title = { Text("NODO ESCLAVO", color = NeonGreen, fontFamily = FontFamily.Monospace, fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = TechCyan)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Configuration Card
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.SettingsEthernet, contentDescription = null, tint = TechCyan)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("CONFIGURACIÓN DE CONEXIÓN", color = TechCyan, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = masterIp,
                        onValueChange = { masterIp = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("IP/URL Cerebro (Master)", color = TextSecondary, fontSize = 11.sp) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonGreen,
                            unfocusedBorderColor = GlassBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        textStyle = androidx.compose.ui.text.TextStyle(fontFamily = FontFamily.Monospace, fontSize = 12.sp),
                        enabled = !isMining
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = minerId,
                        onValueChange = { minerId = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("ID de Minero", color = TextSecondary, fontSize = 11.sp) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonGreen,
                            unfocusedBorderColor = GlassBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        textStyle = androidx.compose.ui.text.TextStyle(fontFamily = FontFamily.Monospace, fontSize = 12.sp),
                        enabled = !isMining
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = personalWallet,
                        onValueChange = { personalWallet = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Billetera Personal (Wallet)", color = TextSecondary, fontSize = 11.sp) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonGreen,
                            unfocusedBorderColor = GlassBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        textStyle = androidx.compose.ui.text.TextStyle(fontFamily = FontFamily.Monospace, fontSize = 12.sp),
                        enabled = !isMining
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("POTENCIA DE MINADO (${miningPower.toInt()}%)", color = TechCyan, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    Slider(
                        value = miningPower,
                        onValueChange = { miningPower = it },
                        valueRange = 10f..100f,
                        enabled = !isMining,
                        colors = SliderDefaults.colors(
                            thumbColor = NeonGreen,
                            activeTrackColor = NeonGreen,
                            inactiveTrackColor = GlassBorder
                        )
                    )
                }
            }
            
            // Status Card
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Dns, contentDescription = null, tint = NeonGreen)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("TELEMETRÍA LOCAL", color = NeonGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(androidx.compose.foundation.shape.CircleShape)
                                    .background(if (miningStats.isConnected) NeonGreen else RedAlert)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (miningStats.isConnected) "ONLINE" else "OFFLINE",
                                color = if (miningStats.isConnected) NeonGreen else RedAlert,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        GlassGauge(
                            progress = miningStats.cpuUsage / 100f,
                            label = "CPU UTIL",
                            valueLabel = "${miningStats.cpuUsage.toInt()}%",
                            primaryColor = NeonGreen
                        )
                        GlassGauge(
                            progress = miningStats.ramUsage / 100f,
                            label = "RAM UTIL",
                            valueLabel = "${miningStats.ramUsage.toInt()}%",
                            primaryColor = TechCyan
                        )
                        GlassGauge(
                            progress = miningStats.gpuUsage / 100f,
                            label = "GPU UTIL",
                            valueLabel = "${miningStats.gpuUsage.toInt()}%",
                            primaryColor = Color(0xFFFF00FF) // Neon Magenta
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    HorizontalDivider(color = GlassBorder.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("HASHRATE", color = TextSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("$miningStats.hashrate H/s", color = if (isMining) NeonGreen else TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("TEMP CPU", color = TextSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("${miningStats.cpuTemp}°C", color = if (miningStats.cpuTemp > 75) RedAlert else TechCyan, fontSize = 18.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("SHARES OK", color = TextSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("$miningStats.sharesAccepted", color = TechCyan, fontSize = 18.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    HorizontalDivider(color = GlassBorder.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = Color(0xFFFFB300))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("GANANCIA ESTIMADA", color = TextSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            Text(
                                text = String.format("%.6f XMR", miningStats.estimatedEarnings),
                                color = Color(0xFFFFB300),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Text("RENDIMIENTO EN TIEMPO REAL (HASHRATE)", color = TextSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    RealTimeChart(
                        data = hashrateHistory,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .padding(vertical = 8.dp),
                        lineColor = NeonGreen
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = {
                    if (isMining) {
                        isMining = false
                        // To cancel properly, track the UUID and call WorkManager.getInstance(context).cancelWorkById(uuid)
                        WorkManager.getInstance(context).cancelAllWork()
                    } else {
                        isMining = true
                        val workRequest = OneTimeWorkRequestBuilder<MinerWorker>().build()
                        WorkManager.getInstance(context).enqueue(workRequest)
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = if (isMining) RedAlert.copy(alpha = 0.2f) else NeonGreen.copy(alpha=0.2f)),
                border = androidx.compose.foundation.BorderStroke(1.dp, if (isMining) RedAlert else NeonGreen)
            ) {
                Icon(
                    imageVector = if (isMining) Icons.Default.Stop else Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = if (isMining) RedAlert else NeonGreen
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isMining) "DETENER MOTOR (XMRIG)" else "ARRANCAR MOTOR (XMRIG)",
                    color = if (isMining) RedAlert else NeonGreen,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}
