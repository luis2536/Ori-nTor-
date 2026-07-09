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
            0 -> NodesAdminView(nodes)
            1 -> MarketingView()
            2 -> PartnerAuditView(nodes)
            3 -> CerebroLogsView(systemLogs)
        }
    }
}

@Composable
fun NodesAdminView(nodes: MutableList<NodeInfo>) {
    val totalHashrate = nodes.sumOf { it.hashRate }
    val activeNodes = nodes.count { it.status == "ACTIVE" }

    Column {
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
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = { 
                // Disconnect logic
                nodes.replaceAll { it.copy(status = "OFFLINE", hashRate = 0) }
            },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = RedAlert),
            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(Icons.Default.PowerOff, contentDescription = null, tint = DarkBackground)
            Spacer(modifier = Modifier.width(8.dp))
            Text("DESCONEXIÓN MASIVA (KILL SWITCH)", color = DarkBackground, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxHeight()) {
            items(nodes) { node ->
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(node.id, color = TextPrimary, fontWeight = FontWeight.Bold)
                            Text("${node.ip} • ${node.region}", color = TextSecondary, fontSize = 11.sp)
                            Text(
                                text = if (node.status == "ACTIVE") "Conectado | ${node.hashRate} H/s" else "Desconectado",
                                color = if (node.status == "ACTIVE") NeonGreen else RedAlert,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                        Row {
                            IconButton(onClick = { /* Approve */ }) {
                                Icon(Icons.Default.Link, contentDescription = "Conectar", tint = NeonGreen)
                            }
                            IconButton(onClick = { /* Block */ }) {
                                Icon(Icons.Default.LinkOff, contentDescription = "Desconectar", tint = RedAlert)
                            }
                        }
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
    
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.SettingsApi, contentDescription = null, tint = TechCyan)
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
