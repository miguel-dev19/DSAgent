package com.dsagent.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.dsagent.ui.theme.*

@Composable
fun AIMessageContent(text: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        MarkdownRenderer(text = text)
    }
}
