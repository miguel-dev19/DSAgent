package com.dsagent.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Psychology
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dsagent.ui.theme.*

@Composable
fun WelcomeScreen(onStart: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize().background(White),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Surface(
                modifier = Modifier.size(120.dp),
                shape = RoundedCornerShape(32.dp),
                color = LightBlue
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Rounded.Psychology, null, Modifier.size(64.dp), White)
                }
            }
            Spacer(Modifier.height(32.dp))
            Text("DeepSeek Agent", style = MaterialTheme.typography.headlineLarge, color = DarkText)
            Spacer(Modifier.height(8.dp))
            Text("Tu asistente inteligente", style = MaterialTheme.typography.bodyLarge, color = GrayText)
            Spacer(Modifier.height(64.dp))
            TextButton(onClick = {}) { Text("Términos", color = LightBlue) }
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = onStart,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp).height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LightBlue),
                shape = RoundedCornerShape(16.dp)
            ) { Text("Aceptar y Continuar") }
        }
    }
}
