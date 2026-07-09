package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassCard
import com.example.ui.theme.*

data class SocialMessage(val id: String, val sender: String, val text: String, val isMe: Boolean)

@Composable
fun SocialScreen() {
    var input by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf(
        SocialMessage("1", "XMR_MINER_88", "¿Alguien más con problemas en el pool?", false),
        SocialMessage("2", "NODE_ALPHA", "Todo bien por aquí, 4200 H/s estables.", false),
        SocialMessage("3", "Tú", "El ping está algo alto, pero minando sin problemas.", true)
    ) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "RED DE MINEROS (GLOBAL)",
            color = TechCyan,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))

        // TikTok Community Trends Highlights Card
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                            contentDescription = "Trending",
                            tint = NeonGreen,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "TENDENCIAS VIRALES (TIKTOK)",
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            letterSpacing = 1.sp
                        )
                    }
                    Text(
                        text = "LIVE",
                        color = RedAlert,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "El hashtag #OrionThorMining supera las 1.8M de vistas. Cientos de usuarios están sincronizando sus dispositivos Tecno Spark y Termux mediante nuestro WebSocket local.",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column {
                        Text("VISTAS", color = TechCyan, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        Text("1.8M+", color = NeonGreen, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                    Column {
                        Text("CONEXIONES", color = TechCyan, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        Text("12,420/s", color = NeonGreen, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                    Column {
                        Text("TUTORIALES", color = TechCyan, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        Text("340 videos", color = NeonGreen, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        GlassCard(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(messages) { msg ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (msg.isMe) Arrangement.End else Arrangement.Start
                    ) {
                        if (!msg.isMe) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = TechCyan, modifier = Modifier.size(24.dp).padding(top = 4.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        Column(horizontalAlignment = if (msg.isMe) Alignment.End else Alignment.Start) {
                            if (!msg.isMe) {
                                Text(msg.sender, color = TechCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                            GlassCard(modifier = Modifier.padding(top = 4.dp)) {
                                Text(
                                    text = msg.text,
                                    color = TextPrimary,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                        }
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
                placeholder = { Text("Mensaje a la red...", color = TextSecondary) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonGreen,
                    unfocusedBorderColor = GlassBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    cursorColor = NeonGreen
                ),
                singleLine = true
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = {
                    if (input.isNotBlank()) {
                        messages.add(SocialMessage(System.currentTimeMillis().toString(), "Tú", input, true))
                        input = ""
                    }
                }
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = TechCyan)
            }
        }
        Spacer(modifier = Modifier.height(80.dp))
    }
}
