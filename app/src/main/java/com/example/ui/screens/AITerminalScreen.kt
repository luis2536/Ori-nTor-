package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

data class TerminalMessage(val text: String, val isUser: Boolean, val isError: Boolean = false)

@Composable
fun AITerminalScreen() {
    var input by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf<TerminalMessage>(
        TerminalMessage("[SYS] Init Syntropy Edge Engine v2.4...", false),
        TerminalMessage("[SYS] Mounting Local-First Virtual Swap...", false)
    ) }

    var isAiTyping by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // Real-time log simulator (Background thread simulation)
    LaunchedEffect(Unit) {
        withContext(Dispatchers.Default) {
            val logs = listOf(
                "[NET] WebSocket (ws://127.0.0.1:8080) Established.",
                "[CORE] Allocating Thread Pool...",
                "[WARN] Memory threshold optimized.",
                "[SYS] Oracle 3D Engine initialized.",
                "[AI] Inicializando Red Neuronal CEREBRO-OMNI-MAX...",
                "[AI] Conectado a clúster de supercomputadoras descentralizado.",
                "[AI] Analizando patrones criptográficos en tiempo real..."
            )
            for (log in logs) {
                delay((400..900).random().toLong())
                withContext(Dispatchers.Main) {
                    messages.add(TerminalMessage(log, false))
                }
            }
        }
    }

    suspend fun processCommandAsync(command: String) {
        isAiTyping = true
        val tempMsg = TerminalMessage("...", false)
        messages.add(tempMsg)
        
        delay((300..800).random().toLong()) // Simulate processing latency
        
        val responseMsg = try {
            when (command.uppercase().trim()) {
                "CHECK_NODE" -> TerminalMessage("Estado: NODE_ALPHA. CPU: 42°C. Memoria Libre: 1.2GB", false)
                "PING" -> TerminalMessage("Latencia Loopback (Local): 4ms.", false)
                "FORCE_ERROR" -> throw IllegalStateException("Simulated Segmentation Fault in Edge node.")
                "CLEAR" -> {
                    messages.clear()
                    TerminalMessage("[SYS] Buffer de logs purgado.", false)
                }
                "DEPLOY_AI" -> TerminalMessage("[AI] Neural Model CEREBRO-OMNI-MAX v10 cargado en memoria. Predicción de mercado y minería predictiva activa.", false)
                "ACTIVATE_POOL" -> TerminalMessage("[POOL] Minería distribuida iniciada. 12,042 nodos enlazados mediante WebSocket Seguro.", false)
                "SYNC_LEDGER" -> TerminalMessage("[LEDGER] Shards descentralizados sincronizados con éxito. Hashrate óptimo (PetaHash).", false)
                "NODE_JS_START" -> TerminalMessage("[SERVER] Clúster Node.js iniciado en modo maestro. Balanceador de carga AI Activo.", false)
                "HACK_MARKETING" -> TerminalMessage("[MARKETING] Campaña agresiva lanzada a 12,400 contactos con APIs de WhatsApp. Conversión estimada: 38%.", false)
                else -> TerminalMessage("Comando no reconocido. Pruebe: CHECK_NODE, PING, DEPLOY_AI, NODE_JS_START", false, true)
            }
        } catch (e: Exception) {
            TerminalMessage("[FATAL] ${e.message}", false, isError = true)
        }
        
        messages.remove(tempMsg)
        messages.add(responseMsg)
        isAiTyping = false
    }

    val quickCommands = listOf("CHECK_NODE", "PING", "FORCE_ERROR", "CLEAR", "DEPLOY_AI", "ACTIVATE_POOL", "SYNC_LEDGER", "NODE_JS_START", "HACK_MARKETING")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "SYNTROPY LAB (LOG VISOR)",
                    color = TechCyan,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "EDGE COMPUTING | LOCAL-FIRST",
                    color = TextSecondary,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
            // Advanced 3D Effects integration
            OracleAnimatedCore(size = 48.dp, isActive = !isAiTyping)
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        GlassCard(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                reverseLayout = true
            ) {
                items(messages.reversed()) { msg ->
                    val timestamp = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault()).format(Date())
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        if (!msg.isUser) {
                            Text(
                                text = "[$timestamp] ",
                                color = TextSecondary,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp
                            )
                        }
                        Text(
                            text = if (msg.isUser) "> ${msg.text}" else msg.text,
                            color = when {
                                msg.isUser -> NeonGreen
                                msg.isError -> RedAlert
                                else -> TextPrimary
                            },
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            quickCommands.forEach { cmd ->
                AssistChip(
                    onClick = {
                        messages.add(TerminalMessage(cmd, true))
                        coroutineScope.launch {
                            processCommandAsync(cmd)
                        }
                    },
                    label = { Text(cmd, color = TechCyan, fontSize = 10.sp, fontFamily = FontFamily.Monospace) },
                    colors = AssistChipDefaults.assistChipColors(containerColor = GlassPanel),
                    border = AssistChipDefaults.assistChipBorder(enabled = true, borderColor = GlassBorder)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Terminal, contentDescription = null, tint = TechCyan, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Ejecutar sys_command...", color = TextSecondary, fontFamily = FontFamily.Monospace, fontSize = 12.sp) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonGreen,
                    unfocusedBorderColor = GlassBorder,
                    focusedTextColor = NeonGreen,
                    unfocusedTextColor = TextPrimary,
                    cursorColor = NeonGreen
                ),
                singleLine = true,
                textStyle = androidx.compose.ui.text.TextStyle(fontFamily = FontFamily.Monospace, fontSize = 12.sp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = {
                    if (input.isNotBlank() && !isAiTyping) {
                        val cmd = input
                        messages.add(TerminalMessage(cmd, true))
                        input = ""
                        coroutineScope.launch {
                            processCommandAsync(cmd)
                        }
                    }
                },
                modifier = Modifier
                    .background(GlassPanel, shape = MaterialTheme.shapes.small)
                    .border(1.dp, GlassBorder, MaterialTheme.shapes.small)
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = TechCyan, modifier = Modifier.size(18.dp))
            }
        }
        Spacer(modifier = Modifier.height(80.dp))
    }
}

