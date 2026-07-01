package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

data class TerminalMessage(val text: String, val isUser: Boolean)

@Composable
fun AITerminalScreen() {
    var input by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf<TerminalMessage>(
        TerminalMessage("Conectado a CodeLlama (Local Node)...", false),
        TerminalMessage("Esperando instrucciones de pentest.", false)
    ) }

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
                            fontSize = 14.sp,
                            modifier = Modifier.fillMaxWidth(0.85f)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Comando...", color = TextSecondary, fontFamily = FontFamily.Monospace) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonGreen,
                    unfocusedBorderColor = GlassBorder,
                    focusedTextColor = NeonGreen,
                    unfocusedTextColor = TextPrimary,
                    cursorColor = NeonGreen
                ),
                singleLine = true,
                textStyle = androidx.compose.ui.text.TextStyle(fontFamily = FontFamily.Monospace)
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = {
                    if (input.isNotBlank()) {
                        messages.add(TerminalMessage(input, true))
                        val cmd = input
                        input = ""
                        // Mock AI Response
                        messages.add(TerminalMessage("Ejecutando: $cmd... [SIMULACIÓN]", false))
                    }
                },
                modifier = Modifier
                    .background(GlassPanel, shape = MaterialTheme.shapes.small)
                    .border(1.dp, GlassBorder, MaterialTheme.shapes.small)
            ) {
                Icon(Icons.Default.Send, contentDescription = "Send", tint = TechCyan)
            }
        }
    }
}
