package com.dsagent.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import com.dsagent.ui.theme.*

@Composable
fun TypingIndicator() {
    val infiniteTransition = rememberInfiniteTransition(label = "typing")
    Surface(Modifier.padding(vertical = 8.dp), LightGray, RoundedCornerShape(12.dp)) {
        Row(Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            repeat(3) { i ->
                val scale by infiniteTransition.animateFloat(0.4f, 1.2f, infiniteRepeatable(tween(600, delayMillis = i * 200), RepeatMode.Reverse), label = "dot$i")
                Box(Modifier.size(8.dp).scale(scale).background(GrayText, CircleShape))
                if (i < 2) Spacer(Modifier.width(4.dp))
            }
        }
    }
}
