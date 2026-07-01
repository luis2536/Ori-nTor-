package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
data class PaymentRequest(val id: String, val userId: String, val amountBTC: String, val address: String, var approved: Boolean)

@Composable
fun AdminPanelScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    
    val nodes = remember { mutableStateListOf(
        NodeInfo("NODE_ALPHA", "192.168.1.10", "ACTIVE", 3200),
        NodeInfo("NODE_BETA", "192.168.1.15", "ACTIVE", 2850),
        NodeInfo("NODE_GAMMA", "192.168.1.22", "OFFLINE", 0),
        NodeInfo("NODE_DELTA", "192.168.1.105", "ACTIVE", 4100)
    ) }

    val payments = remember { mutableStateListOf(
        PaymentRequest("TX1001", "NODE_BETA", "0.002", "bc1qxy2kgdygjrsqtzq2n0yrf2493p83kkfjhx0wlh", false),
        PaymentRequest("TX1002", "NODE_ALPHA", "0.015", "bc1qxw23rdygjrsqtzq2n0yrf2493p83kkfjhasdf", true)
    ) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "ADMINISTRADOR (C&C)",
            color = RedAlert,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "CONTROL MASIVO & ANALÍTICA",
            color = TechCyan,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Tabs
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = DarkBackground,
            contentColor = TechCyan,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = NeonGreen
                )
            }
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                icon = { Icon(Icons.Default.People, contentDescription = "Nodos") },
                text = { Text("NODOS") },
                selectedContentColor = NeonGreen,
                unselectedContentColor = TextSecondary
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                icon = { Icon(Icons.Default.Payment, contentDescription = "Pagos") },
                text = { Text("PAGOS") },
                selectedContentColor = NeonGreen,
                unselectedContentColor = TextSecondary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (selectedTab == 0) {
            NodesAdminView(nodes)
        } else {
            PaymentsAdminView(payments)
        }
    }
}

@Composable
fun NodesAdminView(nodes: List<NodeInfo>) {
    val totalHashrate = nodes.sumOf { it.hashRate }
    val activeNodes = nodes.count { it.status == "ACTIVE" }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            GlassCard(modifier = Modifier.weight(1f)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("HASHRATE GLOBAL", color = TextSecondary, fontSize = 10.sp)
                    Text("$totalHashrate H/s", color = NeonGreen, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            GlassCard(modifier = Modifier.weight(1f)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("NODOS ACTIVOS", color = TextSecondary, fontSize = 10.sp)
                    Text("$activeNodes / ${nodes.size}", color = TechCyan, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { /* Simulate Kill Switch */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = RedAlert)
        ) {
            Icon(Icons.Default.Warning, contentDescription = null, tint = DarkBackground)
            Spacer(modifier = Modifier.width(8.dp))
            Text("APAGADO GLOBAL (KILL SWITCH)", color = DarkBackground, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxHeight()
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
                            Text("IP: ${node.ip}", color = TextSecondary, fontSize = 12.sp)
                            Text("Estado: ${node.status} | ${node.hashRate} H/s", color = if (node.status == "ACTIVE") NeonGreen else RedAlert, fontSize = 12.sp)
                        }
                        
                        Row {
                            IconButton(onClick = { /* Approve */ }) {
                                Icon(Icons.Default.CheckCircle, contentDescription = "Activar", tint = NeonGreen)
                            }
                            IconButton(onClick = { /* Block */ }) {
                                Icon(Icons.Default.Block, contentDescription = "Bloquear", tint = RedAlert)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PaymentsAdminView(payments: MutableList<PaymentRequest>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxHeight()
    ) {
        items(payments) { request ->
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(request.id, color = TechCyan, fontWeight = FontWeight.Bold)
                        if (request.approved) {
                            Text("PAGADO", color = NeonGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        } else {
                            Text("PENDIENTE", color = RedAlert, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Usuario: ${request.userId}", color = TextPrimary, fontSize = 14.sp)
                    Text("Monto: ${request.amountBTC} BTC", color = NeonGreen, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text("Wallet: ${request.address}", color = TextSecondary, fontSize = 10.sp, maxLines = 1)
                    
                    if (!request.approved) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                val index = payments.indexOf(request)
                                if (index != -1) {
                                    payments[index] = request.copy(approved = true)
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = NeonGreen)
                        ) {
                            Text("APROBAR PAGO Y ENVIAR BTC", color = DarkBackground, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
