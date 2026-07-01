package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassCard
import com.example.ui.theme.*

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class TerminalMessage(val text: String, val isUser: Boolean, val isTyping: Boolean = false)

@Composable
fun AITerminalScreen() {
    var input by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf<TerminalMessage>(
        TerminalMessage("Conectado a CodeLlama (Local Node)...", false),
        TerminalMessage("Esperando instrucciones.", false)
    ) }

    var isAiTyping by remember { mutableStateOf(false) }

    suspend fun simulateAiResponse(command: String) {
        isAiTyping = true
        val tempMsg = TerminalMessage("...", false, isTyping = true)
        messages.add(tempMsg)
        
        delay(1000)
        
        val response = when (command.uppercase()) {
            "CHECK_NODE" -> "Estado: NODE_ALPHA Activo. Hashrate: 3200 H/s. Latencia: 42ms."
            "PING_POOL" -> "Pool: pool.hashvault.pro:80. Respuesta: OK. Latencia: 18ms."
            "OPTIMIZE_CPU" -> "Optimizando... Ajustando afinidad de hilos. Eficiencia +4%."
            "SHOW_LOGS" -> "Último evento: Accepted share (diff 120002) - OK."
            else -> "Comando '$command' no reconocido. Intente: CHECK_NODE, PING_POOL."
        }
        
        messages.remove(tempMsg)
        messages.add(TerminalMessage(response, false))
        isAiTyping = false
    }

    val quickCommands = listOf("CHECK_NODE", "PING_POOL", "OPTIMIZE_CPU", "SHOW_LOGS")
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "IA POLIMORFA (TERMINAL)",
            color = TechCyan,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
        )
        Text(
            text = "AGENT: CodeLlama 7B",
            color = TextSecondary,
            fontSize = 12.sp
        )
        Spacer(modifier = Modifier.height(16.dp))

        GlassCard(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                reverseLayout = true
            ) {
                items(messages.reversed()) { msg ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (msg.isUser) Arrangement.End else Arrangement.Start
                    ) {
                        Text(
                            text = if (msg.isUser) "> ${msg.text}" else msg.text,
                            color = if (msg.isUser) NeonGreen else TextPrimary,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp,
                            modifier = Modifier.fillMaxWidth(0.85f)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        
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
                            simulateAiResponse(cmd)
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
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Comando...", color = TextSecondary, fontFamily = FontFamily.Monospace, fontSize = 12.sp) },
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
                        messages.add(TerminalMessage(input, true))
                        val cmd = input
                        input = ""
                        coroutineScope.launch {
                            simulateAiResponse(cmd)
                        }
                    }
                },
                modifier = Modifier
                    .background(GlassPanel, shape = MaterialTheme.shapes.small)
                    .border(1.dp, GlassBorder, MaterialTheme.shapes.small)
            ) {
                Icon(Icons.Default.Send, contentDescription = "Send", tint = TechCyan)
            }
        }
        Spacer(modifier = Modifier.height(80.dp))
    }
}
