package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassCard
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

data class MiningLog(val timestamp: String, val message: String, val isError: Boolean = false)

@Composable
fun DashboardScreen() {
    var isMining by remember { mutableStateOf(false) }
    var currentHashrate by remember { mutableStateOf(0) }
    var cpuTemp by remember { mutableStateOf(45) }
    var ramUsage by remember { mutableStateOf(2.1f) }
    var balance by remember { mutableStateOf(0.00421) }
    val hashrateHistory = remember { mutableStateListOf<Int>() }
    val consoleLogs = remember { mutableStateListOf<MiningLog>() }
    
    var showPaymentDialog by remember { mutableStateOf(false) }
    var btcAddress by remember { mutableStateOf("") }
    
    var poolUrl by remember { mutableStateOf("stratum+tcp://pool.hashvault.pro:80") }
    var threads by remember { mutableStateOf("8") }

    val logScrollState = rememberLazyListState()

    // Simulation loop
    LaunchedEffect(isMining) {
        if (!isMining) {
            currentHashrate = 0
            cpuTemp = 45
            ramUsage = 2.1f
            val time = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            consoleLogs.add(MiningLog(time, "[INFO] Orion SoC stopped."))
            return@LaunchedEffect
        }
        
        val startTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        consoleLogs.add(MiningLog(startTime, "[INIT] Starting XMRig via Termux..."))
        delay(500)
        consoleLogs.add(MiningLog(startTime, "[NET] Connecting to $poolUrl..."))
        delay(800)
        consoleLogs.add(MiningLog(startTime, "[NET] Connected. Authorized as ORION_NODE."))
        consoleLogs.add(MiningLog(startTime, "[CPU] Allocating $threads threads..."))
        
        if (hashrateHistory.isEmpty()) {
            for (i in 0..20) hashrateHistory.add(0)
        }

        while (isMining) {
            delay(1000)
            currentHashrate = Random.nextInt(1200, 3500)
            cpuTemp = Random.nextInt(65, 85)
            ramUsage = 3.5f + Random.nextFloat() * 1.5f
            balance += 0.0000001
            
            hashrateHistory.add(currentHashrate)
            if (hashrateHistory.size > 20) {
                hashrateHistory.removeAt(0)
            }
            
            val time = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            if (Random.nextFloat() > 0.7f) {
                consoleLogs.add(MiningLog(time, "[POOL] Accepted share (diff 120002) - ${currentHashrate} H/s"))
            } else if (Random.nextFloat() > 0.95f) {
                consoleLogs.add(MiningLog(time, "[WARN] Rejected share (stale)", true))
            }
            
            if (consoleLogs.size > 50) {
                consoleLogs.removeAt(0)
            }
        }
    }

    LaunchedEffect(consoleLogs.size) {
        if (consoleLogs.isNotEmpty()) {
            logScrollState.animateScrollToItem(consoleLogs.size - 1)
        }
    }

    if (showPaymentDialog) {
        AlertDialog(
            onDismissRequest = { showPaymentDialog = false },
            containerColor = DarkBackground,
            titleContentColor = TechCyan,
            textContentColor = TextPrimary,
            title = { Text("Solicitar Retiro (BTC)") },
            text = {
                Column {
                    Text("Ingresa tu dirección de billetera Bitcoin. El monto mínimo es 0.001 BTC.", fontSize = 14.sp, color = TextSecondary)
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = btcAddress,
                        onValueChange = { btcAddress = it },
                        label = { Text("Billetera BTC", color = TextSecondary) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonGreen,
                            unfocusedBorderColor = GlassBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { showPaymentDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonGreen)
                ) {
                    Text("ENVIAR", color = DarkBackground, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showPaymentDialog = false }) {
                    Text("CANCELAR", color = RedAlert)
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "PANEL DE NODO",
                    color = TechCyan,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Estado: ${if (isMining) "ACTIVO" else "EN ESPERA"}",
                    color = if (isMining) NeonGreen else TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Icon(Icons.Default.CloudSync, contentDescription = null, tint = if (isMining) NeonGreen else TextSecondary, modifier = Modifier.size(32.dp))
        }
        
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
            StatusCard(
                modifier = Modifier.weight(1f),
                title = "LATENCIA",
                value = if (isMining) "${Random.nextInt(40, 120)}ms" else "--",
                icon = Icons.Default.NetworkPing,
                color = if (isMining) NeonGreen else TextSecondary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

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
                    Text("HASHRATE GLOBAL (H/s)", color = TextSecondary, fontSize = 12.sp)
                    Text("$currentHashrate", color = NeonGreen, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                HashrateChart(data = hashrateHistory, modifier = Modifier.fillMaxSize())
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        
        // Settings Section
        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Settings, contentDescription = null, tint = TechCyan, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("CONFIGURACIÓN DEL POOL", color = TechCyan, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = poolUrl,
                        onValueChange = { poolUrl = it },
                        label = { Text("Pool URL", fontSize = 12.sp) },
                        enabled = !isMining,
                        modifier = Modifier.weight(0.7f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonGreen, unfocusedBorderColor = GlassBorder,
                            disabledTextColor = TextSecondary, disabledBorderColor = GlassBorder.copy(alpha = 0.2f),
                            focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary
                        ),
                        textStyle = androidx.compose.ui.text.TextStyle(fontSize = 12.sp)
                    )
                    OutlinedTextField(
                        value = threads,
                        onValueChange = { threads = it },
                        label = { Text("Hilos", fontSize = 12.sp) },
                        enabled = !isMining,
                        modifier = Modifier.weight(0.3f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonGreen, unfocusedBorderColor = GlassBorder,
                            disabledTextColor = TextSecondary, disabledBorderColor = GlassBorder.copy(alpha = 0.2f),
                            focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary
                        ),
                        textStyle = androidx.compose.ui.text.TextStyle(fontSize = 12.sp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Console Section
        GlassCard(modifier = Modifier.fillMaxWidth().height(150.dp)) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text("TERMINAL DE MINERÍA", color = TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                LazyColumn(state = logScrollState, modifier = Modifier.fillMaxSize()) {
                    items(consoleLogs) { log ->
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text("[${log.timestamp}]", color = TextSecondary, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(log.message, color = if (log.isError) RedAlert else TextPrimary, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        val glowAlpha by animateFloatAsState(
            targetValue = if (isMining) 1f else 0f,
            animationSpec = tween(1000), label = ""
        )

        Button(
            onClick = { isMining = !isMining },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isMining) GlassPanel else NeonGreen
            ),
            shape = RoundedCornerShape(12.dp),
            border = if (isMining) androidx.compose.foundation.BorderStroke(1.dp, RedAlert) else null
        ) {
            Icon(
                imageVector = Icons.Default.PowerSettingsNew,
                contentDescription = null,
                tint = if (isMining) RedAlert else DarkBackground
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = if (isMining) "DETENER NODO DE MINERÍA" else "INICIAR NODO DE MINERÍA",
                color = if (isMining) RedAlert else DarkBackground,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CurrencyBitcoin, contentDescription = null, tint = TechCyan)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("SALDO ACUMULADO", color = TechCyan, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(String.format("%.7f BTC", balance), color = TextPrimary, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text("~ $${String.format("%.2f", balance * 65000)} USD", color = TextSecondary, fontSize = 12.sp)
                
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { showPaymentDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = GlassPanel),
                    border = androidx.compose.foundation.BorderStroke(1.dp, TechCyan)
                ) {
                    Text("SOLICITAR PAGO A BILLETERA", color = TechCyan, fontWeight = FontWeight.Bold)
                }
            }
        }
        Spacer(modifier = Modifier.height(80.dp)) // Extra padding for bottom bar
    }
}

@Composable
fun StatusCard(modifier: Modifier = Modifier, title: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: androidx.compose.ui.graphics.Color) {
    GlassCard(modifier = modifier) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, color = TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Text(value, color = color, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

@Composable
fun HashrateChart(data: List<Int>, modifier: Modifier = Modifier) {
    if (data.isEmpty() || data.all { it == 0 }) {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Text("ESPERANDO CONEXIÓN...", color = TextSecondary, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
        }
        return
    }

    val maxHash = data.maxOrNull()?.coerceAtLeast(1) ?: 1
    
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val stepX = width / (data.size - 1).coerceAtLeast(1)
        
        val path = Path()
        val fillPath = Path()
        
        data.forEachIndexed { index, value ->
            val x = index * stepX
            val y = height - (value.toFloat() / maxHash) * height
            
            if (index == 0) {
                path.moveTo(x, y)
                fillPath.moveTo(x, height)
                fillPath.lineTo(x, y)
            } else {
                path.lineTo(x, y)
                fillPath.lineTo(x, y)
            }
            
            if (index == data.size - 1) {
                fillPath.lineTo(x, height)
                fillPath.close()
            }
        }
        
        drawPath(
            path = fillPath,
            color = NeonGreen.copy(alpha = 0.1f)
        )
        
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
