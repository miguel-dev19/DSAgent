package com.dsagent.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Psychology
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import com.dsagent.ui.theme.*

@Composable
fun AnimatedLogo() {
    val scale = remember { Animatable(0f) }
    LaunchedEffect(Unit) { scale.animateTo(1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy)) }
    Surface(Modifier.size(120.dp).scale(scale.value), RoundedCornerShape(32.dp), LightBlue, shadowElevation = 8.dp) {
        Box(contentAlignment = Alignment.Center) { Icon(Icons.Rounded.Psychology, null, Modifier.size(64.dp), White) }
    }
}
