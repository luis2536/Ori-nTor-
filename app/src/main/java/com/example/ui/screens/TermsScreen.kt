package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ui.components.GlassCard
import com.example.ui.theme.*

@Composable
fun TermsScreen(navController: NavController) {
    var accepted by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "TÉRMINOS Y CONDICIONES",
            color = TechCyan,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        GlassCard(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Text(
                    text = """
                        ALERTA DE SEGURIDAD (ORION THOR)
                        
                        1. Uso Exclusivo en Laboratorio Controlado: Esta aplicación está diseñada estrictamente para pruebas de concepto, auditoría de ciberseguridad y análisis de rendimiento dentro de un entorno aislado.
                        2. Autorización: El usuario declara contar con la autorización explícita para la orquestación de nodos y la ejecución de cargas de trabajo de cómputo (minería simulada/modelos IA) en los dispositivos conectados.
                        3. Responsabilidad: El desarrollador no se hace responsable por el uso indebido de las herramientas de C&C fuera del entorno de laboratorio académico/doctoral.
                        4. Gestión de Recursos: La aplicación solicitará uso intensivo de CPU/RAM para las pruebas de concepto (XMRig/Ollama).
                        
                        Al continuar, confirma que comprende los términos y que esta es una herramienta de simulación y orquestación autorizada para fines de investigación académica.
                    """.trimIndent(),
                    color = TextSecondary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Checkbox(
                checked = accepted,
                onCheckedChange = { accepted = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = NeonGreen,
                    uncheckedColor = GlassBorder,
                    checkmarkColor = DarkBackground
                )
            )
            Text(
                text = "He leído y acepto los términos de uso en laboratorio.",
                color = TextPrimary,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val route = if (com.example.model.Session.isAdmin) "admin" else "dashboard"
                navController.navigate(route) { popUpTo("login") { inclusive = true } }
            },
            enabled = accepted,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = NeonGreen,
                disabledContainerColor = GlassPanel
            )
        ) {
            Text("ACEPTAR Y ENTRAR", color = if (accepted) DarkBackground else TextSecondary, fontWeight = FontWeight.Bold)
        }
    }
}
