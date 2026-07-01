package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassCard
import com.example.ui.theme.*

data class NodeInfo(val id: String, val ip: String, val status: String, val hashRate: Int)

@Composable
fun AdminPanelScreen() {
    val nodes = remember { mutableStateListOf(
        NodeInfo("NODE_ALPHA", "192.168.1.10", "ACTIVE", 3200),
        NodeInfo("NODE_BETA", "192.168.1.15", "ACTIVE", 2850),
        NodeInfo("NODE_GAMMA", "192.168.1.22", "OFFLINE", 0),
        NodeInfo("NODE_DELTA", "192.168.1.105", "ACTIVE", 4100)
    ) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "ADMIN C&C",
            color = RedAlert,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "GLOBAL POOL HASHRATE: 10,150 H/s",
            color = TechCyan,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Kill Switch
        Button(
            onClick = { /* Simulate Kill Switch */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = RedAlert)
        ) {
            Icon(Icons.Default.Warning, contentDescription = null, tint = DarkBackground)
            Spacer(modifier = Modifier.width(8.dp))
            Text("KILL SWITCH (EMERGENCIA)", color = DarkBackground, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("NODOS CONECTADOS", color = TextSecondary, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(nodes) { node ->
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(node.id, color = TextPrimary, fontWeight = FontWeight.Bold)
                            Text(node.ip, color = TextSecondary, fontSize = 12.sp)
                            Text("${node.hashRate} H/s", color = NeonGreen, fontSize = 12.sp)
                        }
                        
                        Row {
                            IconButton(onClick = { /* Approve */ }) {
                                Icon(Icons.Default.CheckCircle, contentDescription = "Approve", tint = NeonGreen)
                            }
                            IconButton(onClick = { /* Block */ }) {
                                Icon(Icons.Default.Block, contentDescription = "Block", tint = RedAlert)
                            }
                        }
                    }
                }
            }
        }
    }
}
