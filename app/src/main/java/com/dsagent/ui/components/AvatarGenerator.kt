package com.dsagent.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dsagent.ui.theme.LightBlue
import com.dsagent.ui.theme.White
import kotlin.math.absoluteValue

@Composable
fun AvatarGenerator(name: String, size: Dp = 48.dp) {
    val initials = remember(name) {
        name.split(" ")
            .take(2)
            .mapNotNull { it.firstOrNull()?.toString() }
            .joinToString("")
            .uppercase()
            .ifEmpty { "?" }
    }
    
    val colors = listOf(
        LightBlue,
        Color(0xFF81C784),
        Color(0xFFFFB74D),
        Color(0xFFE57373),
        Color(0xFFBA68C8)
    )
    
    val avatarColor = remember(name) {
        colors[name.hashCode().absoluteValue % colors.size]
    }
    
    Box(
        modifier = Modifier
            .background(avatarColor, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = White
        )
    }
}
