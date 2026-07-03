package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.model.Session
import com.example.ui.components.GlassCard
import com.example.ui.theme.*

@Composable
fun RoleSelectionScreen(navController: NavController) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(1.dp, TechCyan.copy(alpha = alpha), RoundedCornerShape(16.dp))
                .padding(16.dp)
        )

        GlassCard(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight()
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                com.example.ui.components.OracleAnimatedCore(
                    modifier = Modifier.padding(bottom = 24.dp),
                    size = 100.dp,
                    isActive = true
                )
                
                Text(
                    text = "SYNTROPY LAB",
                    color = NeonGreen,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 3.sp
                )
                Text(
                    text = "SELECCIONE MODO DE OPERACIÓN",
                    color = TechCyan,
                    fontSize = 12.sp,
                    letterSpacing = 1.sp
                )
                
                Spacer(modifier = Modifier.height(40.dp))

                Button(
                    onClick = {
                        Session.isAdmin = false
                        navController.navigate("terms")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GlassPanel),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NeonGreen)
                ) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = NeonGreen)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("MODO CLIENTE (NODO)", color = NeonGreen, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        navController.navigate("login") // Go to login to enter admin password
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = RedAlert.copy(alpha = 0.2f)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, RedAlert)
                ) {
                    Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = RedAlert)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("MODO ADMIN (POOL C&C)", color = RedAlert, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
