package com.dsagent.navigation

import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dsagent.ui.screens.*
import com.dsagent.ui.theme.DSAgentTheme

@Composable
fun DSAgentNavigation() {
    val navController = rememberNavController()
    var darkTheme by remember { mutableStateOf(false) }
    
    DSAgentTheme(darkTheme = darkTheme) {
        NavHost(navController = navController, startDestination = "welcome") {
            composable("welcome") {
                WelcomeScreen(
                    darkTheme = darkTheme,
                    onToggleTheme = { darkTheme = !darkTheme },
                    onStart = { navController.navigate("home") { popUpTo("welcome") { inclusive = true } } }
                )
            }
            composable("home") {
                ChatScreen(
                    darkTheme = darkTheme,
                    onToggleTheme = { darkTheme = !darkTheme }
                )
            }
        }
    }
}
