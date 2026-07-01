package com.example.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.model.Session
import com.example.ui.screens.AdminPanelScreen
import com.example.ui.screens.AITerminalScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.SocialScreen
import com.example.ui.screens.TermsScreen
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.GlassPanel

@Composable
fun OrionNavGraph() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute in listOf("dashboard", "terminal", "admin", "social")) {
                NavigationBar(
                    containerColor = DarkBackground,
                    contentColor = NeonGreen
                ) {
                    if (!Session.isAdmin) {
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
                            label = { Text("Node") },
                            selected = currentRoute == "dashboard",
                            onClick = {
                                navController.navigate("dashboard") {
                                    popUpTo("dashboard") { inclusive = false }
                                    launchSingleTop = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = DarkBackground,
                                selectedTextColor = NeonGreen,
                                indicatorColor = NeonGreen,
                                unselectedIconColor = TextSecondary,
                                unselectedTextColor = TextSecondary
                            )
                        )
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.People, contentDescription = "Social") },
                            label = { Text("Social") },
                            selected = currentRoute == "social",
                            onClick = {
                                navController.navigate("social") {
                                    popUpTo("dashboard")
                                    launchSingleTop = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = DarkBackground,
                                selectedTextColor = NeonGreen,
                                indicatorColor = NeonGreen,
                                unselectedIconColor = TextSecondary,
                                unselectedTextColor = TextSecondary
                            )
                        )
                    }
                    
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Terminal, contentDescription = "AI Terminal") },
                        label = { Text("Chat AI") },
                        selected = currentRoute == "terminal",
                        onClick = {
                            navController.navigate("terminal") {
                                popUpTo(if (Session.isAdmin) "admin" else "dashboard")
                                launchSingleTop = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = DarkBackground,
                            selectedTextColor = NeonGreen,
                            indicatorColor = NeonGreen,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary
                        )
                    )

                    if (Session.isAdmin) {
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.AdminPanelSettings, contentDescription = "Admin") },
                            label = { Text("Admin C&C") },
                            selected = currentRoute == "admin",
                            onClick = {
                                navController.navigate("admin") {
                                    popUpTo("admin") { inclusive = false }
                                    launchSingleTop = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = DarkBackground,
                                selectedTextColor = NeonGreen,
                                indicatorColor = NeonGreen,
                                unselectedIconColor = TextSecondary,
                                unselectedTextColor = TextSecondary
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBackground)
                .padding(innerPadding)
        ) {
            NavHost(
                navController = navController,
                startDestination = "login"
            ) {
                composable("login") { LoginScreen(navController) }
                composable("terms") { TermsScreen(navController) }
                composable("dashboard") { DashboardScreen() }
                composable("social") { SocialScreen() }
                composable("terminal") { AITerminalScreen() }
                composable("admin") { AdminPanelScreen() }
            }
        }
    }
}
