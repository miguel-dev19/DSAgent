package com.dsagent.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Psychology
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dsagent.ui.theme.*

@Composable
fun WelcomeScreen(
    darkTheme: Boolean = false,
    onToggleTheme: () -> Unit = {},
    onStart: () -> Unit
) {
    val scale = remember { Animatable(0f) }
    val alpha = remember { Animatable(0f) }
    
    LaunchedEffect(Unit) {
        scale.animateTo(1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow))
        alpha.animateTo(1f, tween(800))
    }
    
    val bg = if (darkTheme) DarkBackground else White
    val textColor = if (darkTheme) DarkTextLight else DarkText
    
    Box(
        modifier = Modifier.fillMaxSize().background(bg),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = Modifier.size(120.dp).scale(scale.value),
                shape = RoundedCornerShape(32.dp),
                color = LightBlue,
                shadowElevation = 8.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Rounded.Psychology, null, Modifier.size(64.dp), White)
                }
            }
            
            Spacer(Modifier.height(32.dp))
            
            Text("DeepSeek Agent", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold, color = textColor, textAlign = TextAlign.Center)
            Spacer(Modifier.height(12.dp))
            Text("Asistente inteligente con pensamiento profundo y busqueda en tiempo real", style = MaterialTheme.typography.bodyLarge, color = GrayText, textAlign = TextAlign.Center, modifier = Modifier.padding(horizontal = 16.dp))
            Spacer(Modifier.height(48.dp))
            
            Button(
                onClick = onStart,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LightBlue),
                shape = RoundedCornerShape(16.dp)
            ) { Text("Comenzar", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold) }
            
            Spacer(Modifier.height(16.dp))
            
            TextButton(onClick = onToggleTheme) {
                Text(if (darkTheme) "Modo claro" else "Modo oscuro", color = LightBlue)
            }
        }
    }
}
