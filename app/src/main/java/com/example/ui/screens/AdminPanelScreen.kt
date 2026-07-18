package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
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

data class NodeInfo(val id: String, val ip: String, val status: String, val hashRate: Int, val region: String)
data class LogEntry(val time: String, val message: String, val isError: Boolean = false)
data class PartnerAudit(val totalMined: Double, val myShare: Double, val activeMiners: Int)

@Composable
fun AdminPanelScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    
    // Core Engine State (Local-First Simulation)
    val nodes = remember { mutableStateListOf(
        NodeInfo("NODE_ALPHA", "192.168.1.10", "ACTIVE", 3200, "US-EAST"),
        NodeInfo("NODE_BETA", "192.168.1.15", "ACTIVE", 2850, "EU-WEST"),
        NodeInfo("NODE_GAMMA", "192.168.1.22", "OFFLINE", 0, "SA-EAST"),
        NodeInfo("NODE_DELTA", "192.168.1.105", "ACTIVE", 4100, "AP-NORTHEAST")
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
                    text = "EDGE POOL | IA | MARKETING",
                    color = TechCyan,
                    fontSize = 12.sp,
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
                "NODOS" to Icons.Default.Hub,
                "CEREBRO ENGINE" to Icons.Default.Dns,
                "MARKETING" to Icons.Default.Campaign,
                "SOCIA (50%)" to Icons.Default.VerifiedUser,
                "LOGS AI" to Icons.Default.Terminal
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
                    Text("TOTAL HASHRATE", color = TextSecondary, fontSize = 9.sp)
                    Text("${totalHashrate / 1000.0} KH/s", color = NeonGreen, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
            GlassCard(modifier = Modifier.weight(1f)) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("NODOS ACTIVOS", color = TextSecondary, fontSize = 9.sp)
                    Text("$activeNodes / ${nodes.size}", color = TechCyan, fontWeight = FontWeight.Bold, fontSize = 16.sp)
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
                modifier = Modifier.weight(1f).height(36.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GlassPanel),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("PING ALL", color = TechCyan, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = {
                    val time = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault()).format(Date())
                    systemLogs.add(LogEntry(time, "[CMD] Overclocking Pool. Multiplicador de Hashrate fijado en 2.5x", false))
                    // Increase hashrate of active nodes
                    nodes.replaceAll { if (it.status == "ACTIVE") it.copy(hashRate = (it.hashRate * 1.5).toInt()) else it }
                },
                modifier = Modifier.weight(1f).height(36.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GlassPanel),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("OVERCLOCK ⚡", color = NeonGreen, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = {
                    val randId = Random.nextInt(100, 999)
                    val randIp = "192.168.1.${Random.nextInt(2, 254)}"
                    val newN = NodeInfo("NODE_NET_$randId", randIp, "ACTIVE", Random.nextInt(1500, 4500), "REG-LATAM")
                    nodes.add(newN)
                    val time = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault()).format(Date())
                    systemLogs.add(LogEntry(time, "[NET] Cliente conectado desde $randIp. Sincronizando con base de datos local."))
                },
                modifier = Modifier.weight(1f).height(36.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GlassPanel),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("+ CONECTAR", color = NeonGreen, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Decoupled Nodes List
        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxSize()) {
                items(nodes) { node ->
                    GlassCard(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(node.id, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text("${node.ip} • ${node.region}", color = TextSecondary, fontSize = 10.sp)
                                Text(
                                    text = if (node.status == "ACTIVE") "ONLINE | ${node.hashRate} H/s" else "OFFLINE",
                                    color = if (node.status == "ACTIVE") NeonGreen else RedAlert,
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                            Row {
                                if (node.status == "ACTIVE") {
                                    IconButton(
                                        onClick = {
                                            nodes.replaceAll { if (it.id == node.id) it.copy(status = "OFFLINE", hashRate = 0) else it }
                                            val time = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault()).format(Date())
                                            systemLogs.add(LogEntry(time, "[CMD] Desconectado forzosamente nodo: ${node.id}", true))
                                        },
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Icon(Icons.Default.LinkOff, contentDescription = "Desconectar", tint = RedAlert, modifier = Modifier.size(18.dp))
                                    }
                                } else {
                                    IconButton(
                                        onClick = {
                                            nodes.replaceAll { if (it.id == node.id) it.copy(status = "ACTIVE", hashRate = Random.nextInt(2000, 4000)) else it }
                                            val time = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault()).format(Date())
                                            systemLogs.add(LogEntry(time, "[CMD] Reconectado y autorizado nodo: ${node.id}"))
                                        },
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Icon(Icons.Default.Link, contentDescription = "Conectar", tint = NeonGreen, modifier = Modifier.size(18.dp))
                                    }
                                }
                            }
                        }
                    }
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
                Text(">", color = NeonGreen, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = commandInput,
                    onValueChange = { commandInput = it },
                    placeholder = { Text("CMD Command...", color = TextSecondary, fontSize = 11.sp, fontFamily = FontFamily.Monospace) },
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
                                    nodes.replaceAll { it.copy(status = "OFFLINE", hashRate = 0) }
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
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send CMD", tint = TechCyan, modifier = Modifier.size(18.dp))
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
    
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Settings, contentDescription = null, tint = TechCyan)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("CONFIGURACIÓN DE APIS", color = TechCyan, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = apiKey,
                    onValueChange = { apiKey = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("WhatsApp API Key (Twilio/Meta)", color = TextSecondary) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonGreen,
                        unfocusedBorderColor = GlassBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.ChatBubble, contentDescription = null, tint = NeonGreen)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("WHATSAPP API MASIVO", color = NeonGreen, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                    placeholder = { Text("Mensaje promocional para red de mineros...", color = TextSecondary) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonGreen,
                        unfocusedBorderColor = GlassBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { status = "Enviando a 12,400 contactos mediante clúster Node.js..." },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = TechCyan)
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, tint = DarkBackground)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("EJECUTAR CAMPAÑA MASIVA", color = DarkBackground, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(status, color = NeonGreen, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
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
    
    Column {
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
                        Text("TOTAL MINADO (POOL)", color = TextSecondary, fontSize = 10.sp)
                        Text("$totalMined BTC", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("PARTICIPACIÓN MÍUKER (50%)", color = TechCyan, fontSize = 10.sp)
                        Text("$partnerShare BTC", color = NeonGreen, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = GlassBorder)
                Spacer(modifier = Modifier.height(16.dp))
                
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    AnimatedPieChart(
                        values = listOf(totalMined.toFloat() - partnerShare.toFloat(), partnerShare.toFloat()),
                        colors = listOf(TechCyan, NeonGreen),
                        modifier = Modifier.padding(16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = GlassBorder)
                Spacer(modifier = Modifier.height(16.dp))
                
                Text("ESTADO DE NUBE DESCENTRALIZADA", color = TextSecondary, fontSize = 10.sp)
                Text("$activeCount Nodos Activos Verificados por AI", color = TechCyan, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { /* Request withdrawal */ },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonGreen)
                ) {
                    Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = DarkBackground)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("SOLICITAR RETIRO SEGURO (SOCIA)", color = DarkBackground, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun CerebroLogsView(logs: List<LogEntry>) {
    GlassCard(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            reverseLayout = true
        ) {
            items(logs.reversed()) { log ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "[${log.time}] ",
                        color = TextSecondary,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 9.sp
                    )
                    Text(
                        text = log.message,
                        color = if (log.isError) RedAlert else TechCyan,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp
                    )
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
    
    LaunchedEffect(isServerOnline) {
        if (isServerOnline) {
            while (true) {
                delay(3000)
                cpuUsage = Random.nextInt(8, 25).toFloat()
                ramUsage = Random.nextInt(42, 60).toFloat()
                if (Random.nextFloat() > 0.7f) {
                    dbSyncCount += 1
                    val time = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault()).format(Date())
                    systemLogs.add(LogEntry(time, "[DB-SYNC] Local SQLite sharding package #${dbSyncCount} pushed to remote cluster.", false))
                }
            }
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
                            checkedTrackColor = NeonGreen.copy(alpha = 0.5f)
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
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NeonGreen,
                                unfocusedBorderColor = GlassBorder,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            singleLine = true,
                            enabled = !isServerOnline
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
                Text("CPU ENGINE: ${cpuUsage.toInt()}%", color = TextPrimary, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                LinearProgressIndicator(
                    progress = { cpuUsage / 100f },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    color = TechCyan,
                    trackColor = GlassBorder
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // RAM Usage
                Text("RAM POOL: ${ramUsage.toInt()}%", color = TextPrimary, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                LinearProgressIndicator(
                    progress = { ramUsage / 100f },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    color = NeonGreen,
                    trackColor = GlassBorder
                )
                
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
                Spacer(modifier = Modifier.height(8.dp))
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
                    Text("Valor actual: ${syncIntervalSec.toInt()}s", color = NeonGreen, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    Text("Max: 60s", color = TextSecondary, fontSize = 10.sp)
                }
            }
        }
    }
}
