package com.dsagent.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dsagent.ui.theme.*

@Composable
fun UserMessageBubble(text: String) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
        Surface(
            modifier = Modifier.widthIn(max = 320.dp),
            color = LightGray,
            shape = RoundedCornerShape(16.dp, 16.dp, 16.dp, 4.dp)
        ) {
            Text(text, modifier = Modifier.padding(12.dp), style = MaterialTheme.typography.bodyMedium, color = DarkText)
        }
    }
}
