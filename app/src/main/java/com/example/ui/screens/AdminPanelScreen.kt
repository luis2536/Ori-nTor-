package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassCard
import com.example.ui.components.OracleAnimatedCore
import com.example.ui.components.AnimatedPieChart
import com.example.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

data class NodeInfo(val id: String, val ip: String, val status: String, val hashRate: Int, val region: String, val battery: Int, val temp: Int)
data class LogEntry(val time: String, val message: String, val isError: Boolean = false)
data class PartnerAudit(val totalMined: Double, val myShare: Double, val activeMiners: Int)

@Composable
fun AdminPanelScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    
    // Core Engine State (Local-First Simulation)
    val nodes = remember { mutableStateListOf(
        NodeInfo("NODE_ALPHA", "192.168.1.10", "ACTIVE", 3200, "US-EAST", 85, 42),
        NodeInfo("NODE_BETA", "192.168.1.15", "ACTIVE", 2850, "EU-WEST", 60, 55),
        NodeInfo("NODE_GAMMA", "192.168.1.22", "OFFLINE", 0, "SA-EAST", 0, 0),
        NodeInfo("NODE_DELTA", "192.168.1.105", "ACTIVE", 4100, "AP-NORTHEAST", 92, 38)
    ) }

    val systemLogs = remember { mutableStateListOf<LogEntry>() }
    val coroutineScope = rememberCoroutineScope()

    // Real-time AI Cerebro Engine Simulation (Non-blocking)
    LaunchedEffect(Unit) {
        withContext(Dispatchers.Default) {
            val initLogs = listOf(
                "[SYS] Booting CEREBRO PRIME Node.js Controller...",
                "[NET] WebSocket Server (0.0.0.0:8080) ONLINE",
                "[AI] Heuristic analyzer engaged. Anomaly detection active.",
                "[DB] Local-First IndexedDB synced with remote shards."
            )
            initLogs.forEach { log ->
                val time = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault()).format(Date())
                withContext(Dispatchers.Main) {
                    systemLogs.add(LogEntry(time, log))
                }
                delay(300)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Hero Header with 3D Core
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "CEREBRO OMNI-CONTROL",
                    color = RedAlert,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "PANEL MAESTRO | ESTADO: ÓPTIMO",
                    color = TechCyan,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
            OracleAnimatedCore(size = 56.dp, isActive = true)
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Navigation Tabs (Minimalist)
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            containerColor = DarkBackground,
            contentColor = TechCyan,
            edgePadding = 0.dp,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = NeonGreen
                )
            }
        ) {
            val tabs = listOf(
                "RED MINEROS" to Icons.Default.Hub,
                "SISTEMA IA" to Icons.Default.Dns,
                "MARKETING" to Icons.Default.Campaign,
                "SOCIA (50%)" to Icons.Default.VerifiedUser,
                "CONSOLA" to Icons.Default.Terminal
            )
            tabs.forEachIndexed { index, (title, icon) ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    icon = { Icon(icon, contentDescription = title, modifier = Modifier.size(20.dp)) },
                    text = { Text(title, fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    selectedContentColor = NeonGreen,
                    unselectedContentColor = TextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tab Content Switching
        when (selectedTab) {
            0 -> NodesAdminView(nodes, systemLogs)
            1 -> CerebroEngineView(systemLogs)
            2 -> MarketingView()
            3 -> PartnerAuditView(nodes)
            4 -> CerebroLogsView(systemLogs)
        }
    }
}

@Composable
fun NodesAdminView(nodes: MutableList<NodeInfo>, systemLogs: MutableList<LogEntry>) {
    val totalHashrate = nodes.sumOf { it.hashRate }
    val activeNodes = nodes.count { it.status == "ACTIVE" }
    var commandInput by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            GlassCard(modifier = Modifier.weight(1f)) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Speed, contentDescription = null, tint = TechCyan, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("TOTAL HASHRATE", color = TextSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("${totalHashrate / 1000.0} kH/s", color = NeonGreen, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            }
            GlassCard(modifier = Modifier.weight(1f)) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Dns, contentDescription = null, tint = TechCyan, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("NODOS ACTIVOS", color = TextSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text("$activeNodes", color = TechCyan, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text(" / ${nodes.size}", color = TextSecondary, fontSize = 12.sp, modifier = Modifier.padding(bottom = 2.dp))
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Command Shortcuts Toolbar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Button(
                onClick = {
                    val time = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault()).format(Date())
                    systemLogs.add(LogEntry(time, "[CMD] PING enviado. Latencias: ALPHA (14ms), BETA (22ms), DELTA (18ms)"))
                },
                modifier = Modifier.weight(1f).height(40.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GlassPanel),
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(Icons.Default.NetworkPing, contentDescription = null, tint = TechCyan, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("PING ALL", color = TechCyan, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = {
                    val time = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault()).format(Date())
                    systemLogs.add(LogEntry(time, "[CMD] Overclocking Pool. Multiplicador de Hashrate fijado en 2.5x", false))
                    // Increase hashrate of active nodes
                    nodes.replaceAll { if (it.status == "ACTIVE") it.copy(hashRate = (it.hashRate * 1.5).toInt(), temp = it.temp + 15) else it }
                },
                modifier = Modifier.weight(1f).height(40.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GlassPanel),
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(Icons.Default.Bolt, contentDescription = null, tint = NeonGreen, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("OVERCLOCK", color = NeonGreen, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = {
                    val randId = Random.nextInt(100, 999)
                    val randIp = "192.168.1.${Random.nextInt(2, 254)}"
                    val newN = NodeInfo("NODE_NET_$randId", randIp, "ACTIVE", Random.nextInt(1500, 4500), "REG-LATAM", Random.nextInt(30, 100), Random.nextInt(35, 65))
                    nodes.add(newN)
                    val time = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault()).format(Date())
                    systemLogs.add(LogEntry(time, "[NET] Cliente conectado desde $randIp. Sincronizando con base de datos local."))
                },
                modifier = Modifier.weight(1f).height(40.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GlassPanel),
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = NeonGreen, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("CONECTAR", color = NeonGreen, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Decoupled Nodes List
        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxSize()) {
                items(nodes) { node ->
                    NodeCard(
                        node = node,
                        onDisconnect = {
                            nodes.replaceAll { if (it.id == node.id) it.copy(status = "OFFLINE", hashRate = 0, temp = 0) else it }
                            val time = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault()).format(Date())
                            systemLogs.add(LogEntry(time, "[CMD] Desconectado forzosamente nodo: ${node.id}", true))
                        },
                        onConnect = {
                            nodes.replaceAll { if (it.id == node.id) it.copy(status = "ACTIVE", hashRate = Random.nextInt(2000, 4000), temp = Random.nextInt(35, 60)) else it }
                            val time = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault()).format(Date())
                            systemLogs.add(LogEntry(time, "[CMD] Reconectado y autorizado nodo: ${node.id}"))
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Shell Terminal Command Line Bar
        GlassCard(modifier = Modifier.fillMaxWidth().height(56.dp)) {
            Row(
                modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("C:\\>", color = NeonGreen, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = commandInput,
                    onValueChange = { commandInput = it },
                    placeholder = { Text("root@cerebro-master ~#", color = TextSecondary.copy(alpha=0.5f), fontSize = 11.sp, fontFamily = FontFamily.Monospace) },
                    textStyle = androidx.compose.ui.text.TextStyle(color = TextPrimary, fontFamily = FontFamily.Monospace, fontSize = 12.sp),
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        disabledBorderColor = Color.Transparent
                    ),
                    singleLine = true
                )
                IconButton(
                    onClick = {
                        if (commandInput.isNotBlank()) {
                            val cleanCmd = commandInput.trim().lowercase()
                            val time = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault()).format(Date())
                            systemLogs.add(LogEntry(time, "[CMD Console] Executed: '$cleanCmd'"))
                            
                            when {
                                cleanCmd == "reboot" || cleanCmd == "/reboot" -> {
                                    systemLogs.add(LogEntry(time, "[SYS] Reiniciando WebSocket clúster Node.js en puerto 8080..."))
                                }
                                cleanCmd == "clear" || cleanCmd == "/clear" -> {
                                    systemLogs.clear()
                                }
                                cleanCmd.startsWith("kill") -> {
                                    nodes.replaceAll { it.copy(status = "OFFLINE", hashRate = 0, temp = 0) }
                                    systemLogs.add(LogEntry(time, "[SYS] KILL ALL SWITCH EJECUTADO DESDE CONSOLA", true))
                                }
                                cleanCmd == "status" -> {
                                    systemLogs.add(LogEntry(time, "[DB] SQLite local: 104 KB | 4 Tablas | 12K Registros synced"))
                                }
                                else -> {
                                    systemLogs.add(LogEntry(time, "[AI] Comandos aceptados: reboot, clear, kill, status"))
                                }
                            }
                            commandInput = ""
                        }
                    },
                    modifier = Modifier.size(36.dp).background(NeonGreen.copy(alpha=0.2f), RoundedCornerShape(8.dp))
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send CMD", tint = NeonGreen, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

@Composable
fun NodeCard(node: NodeInfo, onDisconnect: () -> Unit, onConnect: () -> Unit) {
    val isActive = node.status == "ACTIVE"
    val cardColor = if (isActive) GlassPanel else RedAlert.copy(alpha = 0.05f)
    val borderColor = if (isActive) NeonGreen.copy(alpha = 0.3f) else RedAlert.copy(alpha = 0.3f)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(androidx.compose.foundation.shape.CircleShape)
                            .background(if (isActive) NeonGreen else RedAlert)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(node.id, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
                Text(
                    text = if (isActive) "ONLINE" else "OFFLINE",
                    color = if (isActive) NeonGreen else RedAlert,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("IP / REGIÓN", color = TextSecondary, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(node.ip, color = TechCyan, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                    Text(node.region, color = TextPrimary, fontSize = 10.sp)
                }
                
                Column(modifier = Modifier.weight(1f)) {
                    Text("TELEMETRÍA", color = TextSecondary, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.BatteryFull, contentDescription = null, tint = if (node.battery > 20) NeonGreen else RedAlert, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("${node.battery}%", color = TextPrimary, fontSize = 11.sp)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Thermostat, contentDescription = null, tint = if (node.temp < 70) TechCyan else RedAlert, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("${node.temp}°C", color = TextPrimary, fontSize = 11.sp)
                    }
                }
                
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                    Text("RENDIMIENTO", color = TextSecondary, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text("${node.hashRate}", color = if (isActive) NeonGreen else TextSecondary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text("H/s", color = TextSecondary, fontSize = 10.sp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = GlassBorder.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                if (isActive) {
                    TextButton(
                        onClick = onDisconnect,
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Icon(Icons.Default.PowerSettingsNew, contentDescription = null, tint = RedAlert, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("DESCONECTAR", color = RedAlert, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                } else {
                     TextButton(
                        onClick = onConnect,
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Icon(Icons.Default.Link, contentDescription = null, tint = TechCyan, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("RE-CONECTAR", color = TechCyan, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun MarketingView() {
    var message by remember { mutableStateOf("") }
    var apiKey by remember { mutableStateOf("sk-wa-api-XXXXXXXXXXXXXXXXXXXX") }
    var status by remember { mutableStateOf("Node.js Server: ONLINE | WhatsApp API: Conectada") }
    var isCampaignRunning by remember { mutableStateOf(false) }
    var campaignProgress by remember { mutableFloatStateOf(0f) }
    val coroutineScope = rememberCoroutineScope()
    
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Settings, contentDescription = null, tint = TechCyan)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("CONFIGURACIÓN DE APIS Y NODOS DE MARKETING", color = TechCyan, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = apiKey,
                    onValueChange = { apiKey = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("WhatsApp API Key (Twilio/Meta/Baileys)", color = TextSecondary, fontSize = 11.sp) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonGreen,
                        unfocusedBorderColor = GlassBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    textStyle = androidx.compose.ui.text.TextStyle(fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.ChatBubble, contentDescription = null, tint = NeonGreen)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("WHATSAPP CAMPAÑA MASIVA", color = NeonGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                    if (isCampaignRunning) {
                        OracleAnimatedCore(size = 24.dp, isActive = true)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                     Column(modifier = Modifier.weight(1f)) {
                         Text("CONTACTOS OBJETIVO", color = TextSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                         Text("12,450", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                     }
                     Column(modifier = Modifier.weight(1f)) {
                         Text("TASA DE APERTURA ESP.", color = TextSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                         Text("68.5%", color = TechCyan, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                     }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                    placeholder = { Text("Mensaje promocional para red de prospectos/mineros...", color = TextSecondary.copy(alpha=0.6f), fontSize = 12.sp) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonGreen,
                        unfocusedBorderColor = GlassBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )
                
                if (isCampaignRunning) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("PROGRESO DE CAMPAÑA: ${(campaignProgress * 100).toInt()}%", color = TechCyan, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    LinearProgressIndicator(
                        progress = { campaignProgress },
                        modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                        color = NeonGreen,
                        trackColor = GlassBorder
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = { 
                        if (!isCampaignRunning && message.isNotBlank()) {
                            isCampaignRunning = true
                            status = "Iniciando workers de Node.js..."
                            coroutineScope.launch {
                                for (i in 1..100) {
                                    delay(50)
                                    campaignProgress = i / 100f
                                    status = "Enviando lote ${i} de 100... [${i * 124} mensajes]"
                                }
                                status = "Campaña Finalizada Exitosamente. 12,450 mensajes enviados."
                                isCampaignRunning = false
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = if (isCampaignRunning) GlassPanel else TechCyan),
                    enabled = !isCampaignRunning && message.isNotBlank()
                ) {
                    if (isCampaignRunning) {
                        CircularProgressIndicator(color = TechCyan, modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("EJECUTANDO CAMPAÑA...", color = TechCyan, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    } else {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, tint = DarkBackground, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("EJECUTAR CAMPAÑA MASIVA", color = DarkBackground, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(status, color = if (isCampaignRunning) NeonGreen else TextSecondary, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
            }
        }
    }
}

@Composable
fun PartnerAuditView(nodes: List<NodeInfo>) {
    val activeCount = nodes.count { it.status == "ACTIVE" }
    // Simulated real-time financial data
    val totalMined = 4.251
    val partnerShare = totalMined * 0.50
    val usdtValue = partnerShare * 64500.0 // Simulated BTC price
    
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Security, contentDescription = null, tint = TechCyan)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("PANEL DE SOCIA MÍUKER (AUDITORÍA 50%)", color = TextPrimary, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text("TOTAL MINADO (POOL)", color = TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("$totalMined BTC", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("PARTICIPACIÓN MÍUKER", color = TechCyan, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("$partnerShare BTC", color = NeonGreen, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text("~ $${String.format("%.2f", usdtValue)} USDT", color = TextSecondary, fontSize = 12.sp)
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Box(modifier = Modifier.fillMaxWidth().height(180.dp), contentAlignment = Alignment.Center) {
                    AnimatedPieChart(
                        values = listOf(totalMined.toFloat() - partnerShare.toFloat(), partnerShare.toFloat()),
                        colors = listOf(TechCyan.copy(alpha = 0.5f), NeonGreen),
                        modifier = Modifier.size(150.dp)
                    )
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("50%", color = NeonGreen, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                        Text("MÍUKER", color = TextSecondary, fontSize = 10.sp)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                     Column {
                        Text("ESTADO DE NUBE DESCENTRALIZADA", color = TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(8.dp).clip(androidx.compose.foundation.shape.CircleShape).background(NeonGreen))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("$activeCount Nodos Activos", color = TechCyan, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = { /* Request withdrawal */ },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = DarkBackground)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("SOLICITAR RETIRO SEGURO (USDT / BTC)", color = DarkBackground, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
fun CerebroLogsView(logs: List<LogEntry>) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Terminal, contentDescription = null, tint = TechCyan)
            Spacer(modifier = Modifier.width(8.dp))
            Text("CEREBRO AI LOGS & ANOMALY DETECTION", color = TechCyan, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
        
        GlassCard(modifier = Modifier.weight(1f).fillMaxWidth()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                reverseLayout = true
            ) {
                items(logs.reversed()) { log ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "[${log.time}] ",
                            color = TextSecondary,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp
                        )
                        Text(
                            text = log.message,
                            color = if (log.isError) RedAlert else TextPrimary,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CerebroEngineView(systemLogs: MutableList<LogEntry>) {
    var isServerOnline by remember { mutableStateOf(true) }
    var serverPort by remember { mutableStateOf("8080") }
    var syncIntervalSec by remember { mutableStateOf(10f) }
    var dbSyncCount by remember { mutableStateOf(142) }
    
    // Dynamic simulated metrics
    var cpuUsage by remember { mutableFloatStateOf(12f) }
    var ramUsage by remember { mutableFloatStateOf(48f) }
    var networkTx by remember { mutableFloatStateOf(12.5f) }
    var networkRx by remember { mutableFloatStateOf(45.2f) }
    
    LaunchedEffect(isServerOnline) {
        if (isServerOnline) {
            while (true) {
                delay(2000)
                cpuUsage = Random.nextInt(8, 35).toFloat()
                ramUsage = Random.nextInt(40, 65).toFloat()
                networkTx = Random.nextFloat() * 50f
                networkRx = Random.nextFloat() * 100f
                
                if (Random.nextFloat() > 0.6f) {
                    dbSyncCount += 1
                    val time = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault()).format(Date())
                    systemLogs.add(LogEntry(time, "[DB-SYNC] Local SQLite sharding package #${dbSyncCount} pushed to remote cluster.", false))
                }
            }
        } else {
             cpuUsage = 0f
             ramUsage = 15f
             networkTx = 0f
             networkRx = 0f
        }
    }

    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        // Status Card
        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Dns, contentDescription = null, tint = TechCyan)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("NODE.JS CORE CONTROLLER", color = TextPrimary, fontWeight = FontWeight.Bold)
                    }
                    Switch(
                        checked = isServerOnline,
                        onCheckedChange = { 
                            isServerOnline = it 
                            val time = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault()).format(Date())
                            if (it) {
                                systemLogs.add(LogEntry(time, "[SYS] Node.js Master Server started on port $serverPort."))
                            } else {
                                systemLogs.add(LogEntry(time, "[SYS] Node.js Master Server STOPPED. All client synchronization halted.", true))
                            }
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = NeonGreen,
                            checkedTrackColor = NeonGreen.copy(alpha = 0.5f),
                            uncheckedThumbColor = RedAlert,
                            uncheckedTrackColor = RedAlert.copy(alpha = 0.3f)
                        )
                    )
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("PORT", color = TextSecondary, fontSize = 10.sp)
                        OutlinedTextField(
                            value = serverPort,
                            onValueChange = { serverPort = it },
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NeonGreen,
                                unfocusedBorderColor = GlassBorder,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            singleLine = true,
                            enabled = !isServerOnline,
                            textStyle = androidx.compose.ui.text.TextStyle(fontFamily = FontFamily.Monospace, fontSize = 14.sp)
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text("WEBSOCKET STATUS", color = TextSecondary, fontSize = 10.sp)
                        Text(
                            text = if (isServerOnline) "LISTENING" else "OFFLINE",
                            color = if (isServerOnline) NeonGreen else RedAlert,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Metrics Card
        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("TELEMETRÍA EN TIEMPO REAL (NODE.JS)", color = TechCyan, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(16.dp))
                
                // CPU Usage
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("CPU ENGINE", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Text("${cpuUsage.toInt()}%", color = TextPrimary, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                }
                val animCpu by animateFloatAsState(targetValue = cpuUsage / 100f, animationSpec = tween(1000))
                LinearProgressIndicator(
                    progress = { animCpu },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp).height(8.dp).clip(RoundedCornerShape(4.dp)),
                    color = TechCyan,
                    trackColor = GlassBorder
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // RAM Usage
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("RAM POOL", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Text("${ramUsage.toInt()}%", color = TextPrimary, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                }
                val animRam by animateFloatAsState(targetValue = ramUsage / 100f, animationSpec = tween(1000))
                LinearProgressIndicator(
                    progress = { animRam },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp).height(8.dp).clip(RoundedCornerShape(4.dp)),
                    color = NeonGreen,
                    trackColor = GlassBorder
                )

                Spacer(modifier = Modifier.height(16.dp))
                
                // Network IO
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.ArrowDownward, contentDescription = null, tint = TechCyan, modifier = Modifier.size(16.dp))
                        Text("RX (Entrada)", color = TextSecondary, fontSize = 9.sp)
                        Text("${String.format("%.1f", networkRx)} KB/s", color = TechCyan, fontSize = 12.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.ArrowUpward, contentDescription = null, tint = NeonGreen, modifier = Modifier.size(16.dp))
                        Text("TX (Salida)", color = TextSecondary, fontSize = 9.sp)
                        Text("${String.format("%.1f", networkTx)} KB/s", color = NeonGreen, fontSize = 12.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = GlassBorder.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(16.dp))
                
                // SQLite Local State
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("BASE DE DATOS LOCAL", color = TextSecondary, fontSize = 10.sp)
                        Text("SQLite / Room Online", color = TechCyan, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("PAQUETES SYNCED", color = TextSecondary, fontSize = 10.sp)
                        Text("$dbSyncCount lotes", color = NeonGreen, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Sync Rate Controller
        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("TIEMPO DE MUESTREO DE CLIENTE", color = TextPrimary, fontWeight = FontWeight.Bold)
                Text("Ajusta la frecuencia con la que los mineros reportan telemetría.", color = TextSecondary, fontSize = 10.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Slider(
                    value = syncIntervalSec,
                    onValueChange = { syncIntervalSec = it },
                    valueRange = 2f..60f,
                    colors = SliderDefaults.colors(
                        thumbColor = TechCyan,
                        activeTrackColor = TechCyan,
                        inactiveTrackColor = GlassBorder
                    )
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Min: 2s", color = TextSecondary, fontSize = 10.sp)
                    Text("Valor actual: ${syncIntervalSec.toInt()}s", color = NeonGreen, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text("Max: 60s", color = TextSecondary, fontSize = 10.sp)
                }
            }
        }
        Spacer(modifier = Modifier.height(80.dp))
    }
}
