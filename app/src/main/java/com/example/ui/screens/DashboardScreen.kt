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
import androidx.compose.material.icons.automirrored.filled.Help
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
    var cpuTemp by remember { mutableStateOf(42) }
    var ramUsage by remember { mutableStateOf(2.0f) }
    var balance by remember { mutableStateOf(0.0018542) }
    val hashrateHistory = remember { mutableStateListOf<Int>() }
    val consoleLogs = remember { mutableStateListOf<MiningLog>() }
    
    // Google Login Simulation State
    var isGoogleLoggedIn by remember { mutableStateOf(false) }
    var loggedInEmail by remember { mutableStateOf("usuario.orion@gmail.com") }
    var loggedInName by remember { mutableStateOf("Luis Gerardo Ramos") }
    var xmrWalletAddress by remember { mutableStateOf("44AFFq5kSiGbU...Y8bS3q") }
    var showLoginDialog by remember { mutableStateOf(false) }

    // World Connection Details
    var pingSpainToCerebro by remember { mutableStateOf(42) } // Latency in ms (Spain to Cerebro Server)
    var signalStability by remember { mutableStateOf(99.4f) }
    var packetSyncCount by remember { mutableStateOf(1402) }
    var connectionRegion by remember { mutableStateOf("España (Madrid) ➡️ Cerebro Master (LATAM)") }

    // AI RandomX Optimization Parameters
    var showAutotuneDialog by remember { mutableStateOf(false) }
    var selectedCpuModel by remember { mutableStateOf("MediaTek Helio G36 (Octa-Core)") }
    var coreThreads by remember { mutableStateOf(4f) } // Default slider 4 hilos
    var virtualSwapEnabled by remember { mutableStateOf(true) } // Memory Fusion +4GB
    var overlockSafetyProfile by remember { mutableStateOf("Balanceado Óptimo") } // Safe, Extreme, Eco
    var isAutotuningInProgress by remember { mutableStateOf(false) }
    var calculatedMaxEfficiency by remember { mutableStateOf(1650) } // Optimized H/s prediction

    var showPaymentDialog by remember { mutableStateOf(false) }
    var btcAddress by remember { mutableStateOf("") }
    
    var poolUrl by remember { mutableStateOf("stratum+tcp://pool.orionthor.io:3333") }
    var showFaqDialog by remember { mutableStateOf(false) }

    val logScrollState = rememberLazyListState()

    // Real-Time Simulation Routine
    LaunchedEffect(isMining, isGoogleLoggedIn, virtualSwapEnabled, coreThreads) {
        if (!isMining) {
            currentHashrate = 0
            cpuTemp = 40
            ramUsage = if (virtualSwapEnabled) 1.9f else 2.3f
            val time = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            consoleLogs.add(MiningLog(time, "[NODO] Sistema en espera. Listo para sincronización."))
            return@LaunchedEffect
        }
        
        val startTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        consoleLogs.add(MiningLog(startTime, "[CONNECT] Estableciendo túnel seguro SSL con Cerebro Principal..."))
        delay(600)
        consoleLogs.add(MiningLog(startTime, "[SSL] Conexión establecida desde Madrid (ES) a Servidor Cerebro (LATAM). Encriptación TLS_AES_256_GCM_SHA384."))
        delay(600)
        
        if (isGoogleLoggedIn) {
            consoleLogs.add(MiningLog(startTime, "[AUTH] Login verificado como: $loggedInEmail. Hashrate vinculado a cartera central."))
        } else {
            consoleLogs.add(MiningLog(startTime, "[WARN] Ejecutando como nodo ANÓNIMO. Vincule su Gmail para reclamar balance directamente.", true))
        }

        delay(500)
        consoleLogs.add(MiningLog(startTime, "[MEM] Análisis de hardware: Virtual Swap / Memory Fusion de ${if (virtualSwapEnabled) "+4GB" else "Inactivo"}."))
        consoleLogs.add(MiningLog(startTime, "[RANDOMX] Ajustando minero XMRig a ${coreThreads.toInt()} hilos de procesamiento."))
        
        if (hashrateHistory.isEmpty()) {
            for (i in 0..20) hashrateHistory.add(0)
        }

        while (isMining) {
            delay(1000)
            
            // Calculate base hashrate depending on threads & virtual memory fusion
            val baseEfficiency = if (virtualSwapEnabled) 1400 else 900
            val threadMultiplier = (coreThreads / 4f)
            currentHashrate = (baseEfficiency * threadMultiplier * Random.nextDouble(0.95, 1.05)).toInt()
            
            // Temperature increases with thread count
            cpuTemp = (45 + (coreThreads * 7) + Random.nextInt(-2, 3)).toInt()
            ramUsage = if (virtualSwapEnabled) {
                (2.2f + (coreThreads * 0.15f) + Random.nextFloat() * 0.1f)
            } else {
                (3.4f + (coreThreads * 0.2f) + Random.nextFloat() * 0.1f)
            }
            
            // Increment balance proportional to hashrate
            balance += (currentHashrate * 0.0000000001)
            packetSyncCount += 1
            pingSpainToCerebro = Random.nextInt(38, 52)
            signalStability = Random.nextFloat() * 1.5f + 98.2f

            hashrateHistory.add(currentHashrate)
            if (hashrateHistory.size > 20) {
                hashrateHistory.removeAt(0)
            }
            
            val time = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            
            if (cpuTemp > 78) {
                consoleLogs.add(MiningLog(time, "[CRÍTICO] Alerta térmica (${cpuTemp}°C). Ejecutando desaceleración de hilos por protección.", true))
                // Lower core activity
                coreThreads = (coreThreads - 1).coerceAtLeast(1f)
                delay(2000)
            } else if (Random.nextFloat() > 0.85f) {
                consoleLogs.add(MiningLog(time, "[WS] Paquete verificado en Cerebro Central. Latencia de red: ${pingSpainToCerebro}ms."))
            } else if (Random.nextFloat() > 0.95f) {
                consoleLogs.add(MiningLog(time, "[RANDOMX] ¡Bloque verificado y subido! Share aceptado por pool central XMR."))
            }
            
            if (consoleLogs.size > 55) {
                consoleLogs.removeAt(0)
            }
        }
    }

    LaunchedEffect(consoleLogs.size) {
        if (consoleLogs.isNotEmpty()) {
            logScrollState.animateScrollToItem(consoleLogs.size - 1)
        }
    }

    // GMAIL LOGIN DIALOG
    if (showLoginDialog) {
        AlertDialog(
            onDismissRequest = { showLoginDialog = false },
            containerColor = DarkBackground,
            titleContentColor = TechCyan,
            textContentColor = TextPrimary,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AccountCircle, contentDescription = null, tint = TechCyan)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Autenticación Segura Gmail", fontSize = 16.sp)
                }
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text("Conecte su cuenta Google oficial para sincronizar el hashrate global y recibir dividendos directamente en su wallet vinculada.", fontSize = 12.sp, color = TextSecondary)
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = loggedInEmail,
                        onValueChange = { loggedInEmail = it },
                        label = { Text("Correo Electrónico Gmail", color = TextSecondary) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonGreen, unfocusedBorderColor = GlassBorder,
                            focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = loggedInName,
                        onValueChange = { loggedInName = it },
                        label = { Text("Nombre de Usuario", color = TextSecondary) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonGreen, unfocusedBorderColor = GlassBorder,
                            focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = xmrWalletAddress,
                        onValueChange = { xmrWalletAddress = it },
                        label = { Text("Dirección de Pago XMR / Wallet", color = TextSecondary) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonGreen, unfocusedBorderColor = GlassBorder,
                            focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        isGoogleLoggedIn = true
                        showLoginDialog = false
                        val time = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
                        consoleLogs.add(MiningLog(time, "[AUTH] Vinculación Gmail completa: $loggedInEmail. Wallet sincronizada."))
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonGreen)
                ) {
                    Text("VINCULAR CUENTA", color = DarkBackground, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLoginDialog = false }) {
                    Text("CANCELAR", color = RedAlert, fontSize = 11.sp)
                }
            }
        )
    }

    // AI AUTOTUNE DIALOG
    if (showAutotuneDialog) {
        AlertDialog(
            onDismissRequest = { showAutotuneDialog = false },
            containerColor = DarkBackground,
            titleContentColor = TechCyan,
            textContentColor = TextPrimary,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = NeonGreen)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Asistente de Optimización AI", fontSize = 16.sp)
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "El optimizador AI escanea los recursos internos de su teléfono para programar el kernel de RandomX de forma óptima.",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Text("PERFIL DE HARDWARE DETECTADO:", color = TechCyan, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        colors = CardDefaults.cardColors(containerColor = GlassPanel)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("CPU: $selectedCpuModel", color = TextPrimary, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                            Text("RAM Física: 4.00 GB", color = TextPrimary, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                            Text("Memory Fusion (Virtual SWAP): ${if (virtualSwapEnabled) "Activo (+4.00 GB)" else "Inactivo"}", color = NeonGreen, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("CONFIGURACIÓN DE PARÁMETROS:", color = TechCyan, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))

                    // Chipset Selector Mockup
                    Text("Modelo de Chipset:", color = TextSecondary, fontSize = 10.sp)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        listOf("Helio G36", "Snapdragon 8 Gen", "Dimensity 9000").forEach { chip ->
                            Button(
                                onClick = { 
                                    selectedCpuModel = if (chip.startsWith("Helio")) "$chip (Octa-Core) Tecno Spark" else "$chip Max-Core Premium"
                                    calculatedMaxEfficiency = if (chip.startsWith("Helio")) 1650 else 4850
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (selectedCpuModel.contains(chip)) NeonGreen else GlassPanel
                                ),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text(chip, fontSize = 9.sp, color = if (selectedCpuModel.contains(chip)) DarkBackground else TextPrimary)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Threads Slider
                    Text("Hilos de Procesamiento: ${coreThreads.toInt()}", color = TextSecondary, fontSize = 11.sp)
                    Slider(
                        value = coreThreads,
                        onValueChange = { coreThreads = it },
                        valueRange = 1f..8f,
                        steps = 6,
                        colors = SliderDefaults.colors(
                            thumbColor = NeonGreen,
                            activeTrackColor = NeonGreen
                        )
                    )

                    // Virtual Swap Memory Switch
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Memory Fusion Swap (Virtual)", color = TextPrimary, fontSize = 12.sp)
                            Text("Asigna swap file en almacenamiento flash", color = TextSecondary, fontSize = 10.sp)
                        }
                        Switch(
                            checked = virtualSwapEnabled,
                            onCheckedChange = { virtualSwapEnabled = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = NeonGreen, checkedTrackColor = NeonGreen.copy(alpha = 0.5f))
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Overclock profiles
                    Text("Perfil Energético y Térmico:", color = TextSecondary, fontSize = 10.sp)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        listOf("Eco Seguro", "Balanceado Óptimo", "Extreme Overclock").forEach { profile ->
                            Button(
                                onClick = { overlockSafetyProfile = profile },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (overlockSafetyProfile == profile) TechCyan else GlassPanel
                                ),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text(profile, fontSize = 8.sp, color = if (overlockSafetyProfile == profile) DarkBackground else TextPrimary)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        isAutotuningInProgress = true
                        showAutotuneDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonGreen)
                ) {
                    Text("OPTIMIZAR", color = DarkBackground, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAutotuneDialog = false }) {
                    Text("CERRAR", color = RedAlert, fontSize = 11.sp)
                }
            }
        )
    }

    if (isAutotuningInProgress) {
        LaunchedEffect(Unit) {
            val time = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            consoleLogs.add(MiningLog(time, "[AI] Iniciando auto-calibración de hilos de CPU..."))
            delay(1000)
            consoleLogs.add(MiningLog(time, "[AI] Testeo de ancho de banda a Cerebro Node. Latencia calculada: ${pingSpainToCerebro}ms."))
            delay(1000)
            consoleLogs.add(MiningLog(time, "[AI] Calibrando hilos óptimos a ${coreThreads.toInt()} núcleos."))
            delay(800)
            consoleLogs.add(MiningLog(time, "[AI] Optimizador completado. Hashrate objetivo fijado en ${calculatedMaxEfficiency} H/s."))
            isAutotuningInProgress = false
        }
        
        AlertDialog(
            onDismissRequest = {},
            containerColor = DarkBackground,
            title = { Text("Ejecutando Sintonización AI", color = NeonGreen) },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Text("Re-configurando hilos del kernel RandomX...", color = TextPrimary, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    CircularProgressIndicator(color = NeonGreen)
                }
            },
            confirmButton = {}
        )
    }

    // BTC WITHDRAWAL DIALOG
    if (showPaymentDialog) {
        AlertDialog(
            onDismissRequest = { showPaymentDialog = false },
            containerColor = DarkBackground,
            titleContentColor = TechCyan,
            textContentColor = TextPrimary,
            title = { Text("Solicitar Retiro de Dividendos (BTC)") },
            text = {
                Column {
                    Text("Ingresa tu dirección de billetera Bitcoin. El monto acumulado se transferirá mediante el pool central.", fontSize = 12.sp, color = TextSecondary)
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = btcAddress,
                        onValueChange = { btcAddress = it },
                        label = { Text("Billetera Bitcoin (BTC)", color = TextSecondary) },
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
                    onClick = { 
                        showPaymentDialog = false
                        val time = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
                        consoleLogs.add(MiningLog(time, "[RETIRAR] Petición de retiro registrada para la dirección: $btcAddress"))
                    },
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

    // FAQ DIALOG
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
                    Text("Guía Avanzada RandomX")
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
                        text = "ALTA CONECTIVIDAD & RENDIMIENTO:\n",
                        color = NeonGreen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "Esta aplicación cliente se conecta de forma directa con el teléfono Cerebro Master (incluso si se encuentra en España o en cualquier otra parte del mundo). Para ello se utiliza una conexión de socket continuo de baja latencia con respaldo criptográfico.\n\n" +
                               "Para maximizar el rendimiento de minería RandomX (XMR) en cualquier modelo Android:\n",
                        color = TextPrimary,
                        fontSize = 12.sp
                    )
                    
                    Text(
                        text = "1. Sintonizador de Memoria Virtual\n",
                        color = TechCyan,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                    Text(
                        text = "El motor habilita de forma virtual un archivo Swap para darle al recolector de basura de Android un colchón de 4GB extra, evitando bloqueos súbitos en segundo plano.\n\n",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )

                    Text(
                        text = "2. Gestión Inteligente de Hilos\n",
                        color = TechCyan,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                    Text(
                        text = "RandomX depende críticamente del caché L2/L3 de tu CPU. Un exceso de hilos (por ejemplo, usar 8 de 8 en Helio G36) colapsa el caché de datos provocando una caída drástica en H/s y calentando el dispositivo. Mantener la configuración entre 3 y 5 hilos es ideal.\n",
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
        
        // 1. TOP SECURE PAIRING & GMAIL LOGIN
        GlassCard(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    if (isGoogleLoggedIn) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .background(NeonGreen.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = loggedInName.take(1).uppercase(),
                                color = NeonGreen,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(loggedInName, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text(loggedInEmail, color = TextSecondary, fontSize = 11.sp)
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .background(GlassBorder),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.AccountCircle, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(24.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("Sincronización de Cliente", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("No vinculado (Modo Anónimo)", color = RedAlert, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                
                Button(
                    onClick = { showLoginDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = if (isGoogleLoggedIn) GlassBorder else NeonGreen),
                    modifier = Modifier.height(34.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp)
                ) {
                    Text(
                        text = if (isGoogleLoggedIn) "Gmail Activo" else "Vincular Gmail",
                        color = if (isGoogleLoggedIn) TextPrimary else DarkBackground,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp
                    )
                }
            }
        }

        // 2. SPAIN ➡️ CEREBRO (LATAM) CONNECTIVITY MAP CARD
        GlassCard(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CellTower, contentDescription = null, tint = TechCyan, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("TÚNEL ENCRIPTADO GLOBAL", color = TechCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isMining) NeonGreen.copy(alpha = 0.15f) else RedAlert.copy(alpha = 0.15f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (isMining) "CONECTADO" else "DESCONECTADO",
                            color = if (isMining) NeonGreen else RedAlert,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(10.dp))
                
                // Visual Map Pipeline
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.CompassCalibration, contentDescription = null, tint = TextPrimary, modifier = Modifier.size(24.dp))
                        Text("ESP (Cliente)", color = TextPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text(if (isMining) "${pingSpainToCerebro}ms" else "--", color = TechCyan, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                    }
                    
                    // Connected arrows
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.Center) {
                        Box(
                            modifier = Modifier
                                .height(2.dp)
                                .weight(1f)
                                .background(if (isMining) NeonGreen else GlassBorder)
                        )
                        Icon(Icons.Default.Language, contentDescription = null, tint = if (isMining) NeonGreen else TextSecondary, modifier = Modifier.size(16.dp))
                        Box(
                            modifier = Modifier
                                .height(2.dp)
                                .weight(1f)
                                .background(if (isMining) NeonGreen else GlassBorder)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Dns, contentDescription = null, tint = NeonGreen, modifier = Modifier.size(24.dp))
                        Text("Cerebro Pool", color = NeonGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text("Master LATAM", color = TextSecondary, fontSize = 9.sp)
                    }
                }
                
                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = GlassBorder)
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text("RED SEGURA", color = TextSecondary, fontSize = 8.sp)
                        Text(connectionRegion, color = TextPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("ESTABILIDAD (PING)", color = TextSecondary, fontSize = 8.sp)
                        Text(if (isMining) String.format("%.2f%%", signalStability) else "--", color = NeonGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // 3. AI AUTOTUNER TRIGGER CARD
        GlassCard(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "MOTOR DE MINERÍA OPTIMIZADO",
                        color = NeonGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Configuración actual: ${coreThreads.toInt()} núcleos | SWAP ${if (virtualSwapEnabled) "ON" else "OFF"}",
                        color = TextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                IconButton(
                    onClick = { showAutotuneDialog = true },
                    modifier = Modifier
                        .size(38.dp)
                        .background(NeonGreen.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = "AI Autotune", tint = NeonGreen, modifier = Modifier.size(20.dp))
                }
            }
        }

        // Header Panel Info
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "CLIENTE RADICAL XMR",
                    color = TechCyan,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Hashrate Objetivo AI: $calculatedMaxEfficiency H/s",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }
            TextButton(onClick = { showFaqDialog = true }) {
                Icon(Icons.AutoMirrored.Filled.Help, contentDescription = null, tint = NeonGreen, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Guía XMR", color = NeonGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))

        // Hardware Real-Time Monitor
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatusCard(
                modifier = Modifier.weight(1f),
                title = "TEMPERATURA",
                value = "${cpuTemp}°C",
                icon = Icons.Default.Speed,
                color = if (cpuTemp > 70) RedAlert else NeonGreen
            )
            StatusCard(
                modifier = Modifier.weight(1f),
                title = "RAM / SWAP",
                value = String.format("%.1f GB", ramUsage),
                icon = Icons.Default.Memory,
                color = TechCyan
            )
            StatusCard(
                modifier = Modifier.weight(1f),
                title = "PAQUETES",
                value = if (isMining) "#$packetSyncCount" else "--",
                icon = Icons.Default.CloudUpload,
                color = if (isMining) NeonGreen else TextSecondary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Hashrate Timeline Graph
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("VELOCIDAD RANDOM-X EN TIEMPO REAL (H/s)", color = TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text("$currentHashrate H/s", color = NeonGreen, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                HashrateChart(data = hashrateHistory, modifier = Modifier.fillMaxSize())
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Custom Mining Terminal Console
        GlassCard(modifier = Modifier.fillMaxWidth().height(140.dp)) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text("TERMINAL DE SECTOR DE MINERÍA", color = TextSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                LazyColumn(state = logScrollState, modifier = Modifier.fillMaxSize()) {
                    items(consoleLogs) { log ->
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text("[${log.timestamp}]", color = TextSecondary, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(log.message, color = if (log.isError) RedAlert else TextPrimary, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Main Mining Switch Button
        Button(
            onClick = { isMining = !isMining },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isMining) GlassPanel else NeonGreen
            ),
            shape = RoundedCornerShape(10.dp),
            border = if (isMining) androidx.compose.foundation.BorderStroke(1.dp, RedAlert) else null
        ) {
            Icon(
                imageVector = Icons.Default.PowerSettingsNew,
                contentDescription = null,
                tint = if (isMining) RedAlert else DarkBackground
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = if (isMining) "APAGAR NODO DE COMPUTACIÓN" else "ENCENDER NODO EN SEGUNDO PLANO",
                color = if (isMining) RedAlert else DarkBackground,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Accumulated Dividends Card
        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Paid, contentDescription = null, tint = TechCyan)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("REGISTRO DE SALDO VINCULADO", color = TechCyan, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(String.format("%.8f XMR", balance * 12.5f), color = TextPrimary, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text("~ $${String.format("%.2f", balance * 12.5f * 174.5f)} USD (Monto verificado por Cerebro)", color = TextSecondary, fontSize = 11.sp)
                
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = { showPaymentDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = GlassPanel),
                    border = androidx.compose.foundation.BorderStroke(1.dp, TechCyan)
                ) {
                    Text("SOLICITAR RETIRO AL POOL MAESTRO", color = TechCyan, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }
        Spacer(modifier = Modifier.height(80.dp))
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
