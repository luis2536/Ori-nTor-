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

    var isLocalEngine by remember { mutableStateOf(true) }
    var showFaqDialog by remember { mutableStateOf(false) }

    val logScrollState = rememberLazyListState()

    // Simulation loop
    LaunchedEffect(isMining, isLocalEngine) {
        if (!isMining) {
            currentHashrate = 0
            cpuTemp = 45
            ramUsage = 2.1f
            val time = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            consoleLogs.add(MiningLog(time, "[INFO] Orion SoC stopped."))
            return@LaunchedEffect
        }
        
        val startTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        if (isLocalEngine) {
            consoleLogs.add(MiningLog(startTime, "[INIT] Starting Local Node Engine (Tecno Spark 30C Optimization)..."))
            delay(500)
            consoleLogs.add(MiningLog(startTime, "[MEM] Physical RAM: 4.0GB | Virtual Swap: 4.0GB Active."))
            delay(500)
            consoleLogs.add(MiningLog(startTime, "[NET] Binding WebSocket Server on ws://127.0.0.1:8080/mining..."))
            delay(700)
            consoleLogs.add(MiningLog(startTime, "[WS] WebSocket server started on port 8080."))
            consoleLogs.add(MiningLog(startTime, "[WS] Local Node client connected. Handshake OK."))
            consoleLogs.add(MiningLog(startTime, "[CPU] Helio G36 detected. Allocating 4 threads for thermal safety..."))
        } else {
            consoleLogs.add(MiningLog(startTime, "[INIT] Starting XMRig via Termux..."))
            delay(500)
            consoleLogs.add(MiningLog(startTime, "[NET] Connecting to $poolUrl..."))
            delay(800)
            consoleLogs.add(MiningLog(startTime, "[NET] Connected. Authorized as ORION_NODE."))
            consoleLogs.add(MiningLog(startTime, "[CPU] Allocating $threads threads..."))
        }
        
        if (hashrateHistory.isEmpty()) {
            for (i in 0..20) hashrateHistory.add(0)
        }

        while (isMining) {
            delay(1000)
            currentHashrate = if (isLocalEngine) Random.nextInt(800, 1800) else Random.nextInt(1200, 3500)
            cpuTemp = if (isLocalEngine) Random.nextInt(55, 72) else Random.nextInt(65, 85)
            ramUsage = if (isLocalEngine) (2.8f + Random.nextFloat() * 0.4f) else (3.5f + Random.nextFloat() * 1.5f)
            balance += if (isLocalEngine) 0.00000007 else 0.00000015
            
            hashrateHistory.add(currentHashrate)
            if (hashrateHistory.size > 20) {
                hashrateHistory.removeAt(0)
            }
            
            val time = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            if (isLocalEngine) {
                if (Random.nextFloat() > 0.6f) {
                    consoleLogs.add(MiningLog(time, "[WS_FRAME] Received Job #${Random.nextInt(1000, 9999)} - Diff: 8000"))
                } else if (Random.nextFloat() > 0.85f) {
                    consoleLogs.add(MiningLog(time, "[WS_FRAME] Sent Share: Block verified locally. Response: ACCEPTED"))
                }
            } else {
                if (Random.nextFloat() > 0.7f) {
                    consoleLogs.add(MiningLog(time, "[POOL] Accepted share (diff 120002) - ${currentHashrate} H/s"))
                } else if (Random.nextFloat() > 0.95f) {
                    consoleLogs.add(MiningLog(time, "[WARN] Rejected share (stale)", true))
                }
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

    if (showFaqDialog) {
        AlertDialog(
            onDismissRequest = { showFaqDialog = false },
            containerColor = DarkBackground,
            titleContentColor = TechCyan,
            textContentColor = TextPrimary,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.PhoneAndroid, contentDescription = null, tint = NeonGreen)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Guía Tecno Spark 30C (4+4 RAM)")
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 350.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "SOPORTE DE HARDWARE & TERMUX:\n",
                        color = NeonGreen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "El Tecno Spark 30C cuenta con un procesador Helio G36 (Octa-Core) con 4GB de RAM física y 4GB de RAM virtual (Memory Fusion). " +
                               "¡SÍ es 100% posible ejecutar minería en segundo plano o nodos locales!\n\n" +
                               "Para maximizar el rendimiento y evitar que el sistema cierre la app por falta de memoria, sigue estos pasos:\n",
                        color = TextPrimary,
                        fontSize = 12.sp
                    )
                    
                    Text(
                        text = "1. Configurar memoria virtual (Swap)\n",
                        color = TechCyan,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                    Text(
                        text = "Asegúrate de tener activa la función 'Memory Fusion' en Ajustes > Sistema > Características especiales del Tecno Spark 30C para obtener los 4GB virtuales extras.\n\n",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )

                    Text(
                        text = "2. Comandos de Instalación en Termux\n",
                        color = TechCyan,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                    Text(
                        text = "Ejecuta lo siguiente en Termux:\n" +
                               "pkg update && pkg upgrade -y\n" +
                               "pkg install git cmake make clang -y\n" +
                               "git clone https://github.com/xmrig/xmrig.git\n" +
                               "mkdir xmrig/build && cd xmrig/build\n" +
                               "cmake .. -DWITH_HWLOC=OFF\n" +
                               "make -j4\n\n",
                        color = TextSecondary,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace
                    )

                    Text(
                        text = "3. Hilos Óptimos (Threads)\n",
                        color = TechCyan,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                    Text(
                        text = "Usa un máximo de 3 o 4 hilos (en lugar de 8) en la configuración para mantener el teléfono a buena temperatura y que la RAM física no colapse. " +
                               "El motor WebSocket local integrado en esta app se adaptará automáticamente a este perfil óptimo.\n",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { showFaqDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonGreen)
                ) {
                    Text("ENTENDIDO", color = DarkBackground, fontWeight = FontWeight.Bold)
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
        // Hero Visualizer with 3D Oracle
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "NÚCLEO ORÁCULO DE MINERÍA",
                        color = NeonGreen,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isMining) "Sincronizando Bloques via WebSocket..." else "Núcleo de Computación Listo",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isLocalEngine) "Motor: Local (Tecno Spark 30C)" else "Motor: Stratum Pool Externo",
                        color = TechCyan,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
                com.example.ui.components.OracleAnimatedCore(
                    size = 80.dp,
                    isActive = isMining
                )
            }
        }

        // Engine Selector Panel
        GlassCard(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "SELECCIÓN DE MOTOR DE NODO",
                    color = TechCyan,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { isLocalEngine = true },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isLocalEngine) NeonGreen else GlassPanel
                        ),
                        border = if (!isLocalEngine) androidx.compose.foundation.BorderStroke(1.dp, GlassBorder) else null
                    ) {
                        Text(
                            "Termux Local (4+4GB)",
                            color = if (isLocalEngine) DarkBackground else TextPrimary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Button(
                        onClick = { isLocalEngine = false },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (!isLocalEngine) NeonGreen else GlassPanel
                        ),
                        border = if (isLocalEngine) androidx.compose.foundation.BorderStroke(1.dp, GlassBorder) else null
                    ) {
                        Text(
                            "Stratum Pool",
                            color = if (!isLocalEngine) DarkBackground else TextPrimary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(
                    onClick = { showFaqDialog = true },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Icon(Icons.Default.Help, contentDescription = null, tint = NeonGreen, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Guía Tecno Spark 30C & Termux", color = NeonGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

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
