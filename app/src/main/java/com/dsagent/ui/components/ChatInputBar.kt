package com.dsagent.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dsagent.ui.theme.*

@Composable
fun ChatInputBar(
    messageText: String,
    onMessageChange: (String) -> Unit,
    onSendMessage: () -> Unit,
    onStopStreaming: () -> Unit,
    isStreaming: Boolean
) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
        color = White,
        shadowElevation = 2.dp,
        shape = RoundedCornerShape(28.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 8.dp, top = 4.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = messageText,
                onValueChange = onMessageChange,
                modifier = Modifier.weight(1f).padding(vertical = 12.dp),
                textStyle = TextStyle(fontSize = 16.sp, color = DarkText),
                cursorBrush = Brush.horizontalGradient(listOf(LightBlue, LightBlueVariant)),
                decorationBox = { innerTextField ->
                    Box { if (messageText.isEmpty()) Text("Escribe un mensaje...", color = GrayText); innerTextField() }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = { if (messageText.isNotBlank() && !isStreaming) onSendMessage() })
            )
            AnimatedContent(targetState = isStreaming, label = "send") { streaming ->
                when (streaming) {
                    false -> IconButton(
                        onClick = { if (messageText.isNotBlank()) onSendMessage() },
                        modifier = Modifier.size(40.dp).background(if (messageText.isNotBlank()) LightBlue else GrayBorder, CircleShape)
                    ) { Icon(Icons.Rounded.Send, "Enviar", if (messageText.isNotBlank()) White else GrayText, Modifier.size(20.dp).rotate(-45f)) }
                    true -> IconButton(
                        onClick = onStopStreaming,
                        modifier = Modifier.size(40.dp).background(ErrorRed, CircleShape)
                    ) { Icon(Icons.Rounded.Stop, "Detener", White, Modifier.size(20.dp)) }
                }
            }
        }
    }
}
