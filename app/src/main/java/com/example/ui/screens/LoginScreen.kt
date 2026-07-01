package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ui.components.GlassCard
import com.example.ui.theme.*

@Composable
fun LoginScreen(navController: NavController) {
    var accessKey by remember { mutableStateOf("") }
    var isAuthenticating by remember { mutableStateOf(false) }

    // Simple pulse animation for "scanning network" effect
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
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
        // Mock network scan background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(2.dp, NeonGreen.copy(alpha = alpha), RoundedCornerShape(16.dp))
                .padding(16.dp)
        )

        GlassCard(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .wrapContentHeight()
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Security",
                    tint = NeonGreen,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "ORION THOR",
                    color = NeonGreen,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
                Text(
                    text = "ACCESO RESTRINGIDO",
                    color = RedAlert,
                    fontSize = 12.sp,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(
                    value = accessKey,
                    onValueChange = { accessKey = it },
                    label = { Text("Clave de Acceso", color = TextSecondary) },
                    visualTransformation = PasswordVisualTransformation(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonGreen,
                        unfocusedBorderColor = GlassBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        cursorColor = NeonGreen
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        isAuthenticating = true
                        com.example.model.Session.isAdmin = accessKey == "admin"
                        com.example.model.Session.currentKey = accessKey
                        navController.navigate("terms")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonGreen)
                ) {
                    Text("AUTENTICAR NODO", color = DarkBackground, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
