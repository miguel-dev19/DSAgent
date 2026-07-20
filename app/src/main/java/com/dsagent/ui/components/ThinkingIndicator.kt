package com.dsagent.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dsagent.ui.theme.*

@Composable
fun ThinkingIndicator(thinkingText: String = "") {
    val infiniteTransition = rememberInfiniteTransition(label = "thinking")
    
    val brainScale by infiniteTransition.animateFloat(0.9f, 1.1f, infiniteRepeatable(tween(800, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "brain")
    val rotation by infiniteTransition.animateFloat(-5f, 5f, infiniteRepeatable(tween(1200, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "rotate")
    
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 4.dp)) {
        Surface(
            modifier = Modifier.padding(bottom = 8.dp),
            shape = RoundedCornerShape(16.dp),
            color = LightBlue.copy(alpha = 0.08f),
            border = BorderStroke(1.dp, LightBlue.copy(alpha = 0.2f))
        ) {
            Row(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.Psychology, null, Modifier.size(22.dp).scale(brainScale).rotate(rotation), tint = LightBlue)
                Spacer(Modifier.width(10.dp))
                Text("Pensando...", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium, color = LightBlue)
                Spacer(Modifier.weight(1f))
                Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                    repeat(3) { index ->
                        val dotAlpha by infiniteTransition.animateFloat(0.3f, 1f, infiniteRepeatable(tween(400, delayMillis = index * 200), RepeatMode.Reverse), label = "dot$index")
                        Box(Modifier.size(5.dp).background(LightBlue.copy(alpha = dotAlpha), CircleShape))
                    }
                }
            }
        }
        
        if (thinkingText.isNotEmpty()) {
            Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), color = Color(0xFFF8FAFC), border = BorderStroke(1.dp, GrayBorder.copy(alpha = 0.5f))) {
                Text(thinkingText, Modifier.padding(12.dp), style = MaterialTheme.typography.bodySmall, color = GrayText)
            }
        }
    }
}
