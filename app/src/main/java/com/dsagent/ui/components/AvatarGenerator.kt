package com.dsagent.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dsagent.ui.theme.*

@Composable
fun AvatarGenerator(name: String, size: Dp = 48.dp) {
    val initials = remember(name) { name.split(" ").take(2).mapNotNull { it.firstOrNull()?.toString() }.joinToString("").uppercase().ifEmpty { "?" } }
    val color = remember(name) { listOf(LightBlue, Color(0xFF81C784), Color(0xFFFFB74D), Color(0xFFE57373), Color(0xFFBA68C8))[name.hashCode().absoluteValue % 5] }
    Box(Modifier.size(size).background(color, CircleShape), contentAlignment = Alignment.Center) {
        Text(initials, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = White)
    }
}
