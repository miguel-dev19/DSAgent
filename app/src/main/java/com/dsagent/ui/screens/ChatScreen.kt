package com.dsagent.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dsagent.ui.theme.*

@Composable
fun ChatScreen() {
    Box(Modifier.fillMaxSize().background(White)) {
        Column(Modifier.fillMaxSize()) {
            Text(
                "Chat Principal - En construcción",
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                color = GrayText
            )
        }
    }
}
