package com.dsagent.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
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
    isStreaming: Boolean,
    thinkingEnabled: Boolean = true,
    onToggleThinking: () -> Unit = {},
    searchEnabled: Boolean = true,
    onToggleSearch: () -> Unit = {}
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        color = White,
        shadowElevation = 4.dp,
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = messageText,
                    onValueChange = onMessageChange,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        color = DarkText,
                        lineHeight = 22.sp
                    ),
                    cursorBrush = Brush.horizontalGradient(
                        listOf(LightBlue, LightBlueVariant)
                    ),
                    decorationBox = { innerTextField ->
                        Box {
                            if (messageText.isEmpty()) {
                                Text(
                                    text = "Escribe un mensaje...",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = GrayText
                                )
                            }
                            innerTextField()
                        }
                    },
                    maxLines = 4,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            if (messageText.isNotBlank() && !isStreaming) {
                                onSendMessage()
                            }
                        }
                    )
                )
                
                AnimatedContent(
                    targetState = isStreaming,
                    label = "send_stop"
                ) { streaming ->
                    when (streaming) {
                        false -> {
                            IconButton(
                                onClick = {
                                    if (messageText.isNotBlank()) onSendMessage()
                                },
                                modifier = Modifier
                                    .size(44.dp)
                                    .background(
                                        color = if (messageText.isNotBlank())
                                            LightBlue
                                        else
                                            GrayBorder,
                                        shape = CircleShape
                                    )
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Send,
                                    contentDescription = "Enviar mensaje",
                                    tint = if (messageText.isNotBlank())
                                        White
                                    else
                                        GrayText,
                                    modifier = Modifier
                                        .size(22.dp)
                                        .rotate(-45f)
                                )
                            }
                        }
                        true -> {
                            IconButton(
                                onClick = onStopStreaming,
                                modifier = Modifier
                                    .size(44.dp)
                                    .background(
                                        color = ErrorRed,
                                        shape = CircleShape
                                    )
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Stop,
                                    contentDescription = "Detener respuesta",
                                    tint = White,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                    }
                }
            }
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    onClick = onToggleThinking,
                    modifier = Modifier.padding(end = 8.dp),
                    shape = RoundedCornerShape(20.dp),
                    color = if (thinkingEnabled)
                        LightBlue.copy(alpha = 0.12f)
                    else
                        LightGray,
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (thinkingEnabled)
                            LightBlue.copy(alpha = 0.4f)
                        else
                            GrayBorder
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Psychology,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = if (thinkingEnabled) LightBlue else GrayText
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Pensar",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Medium,
                            color = if (thinkingEnabled) LightBlue else GrayText
                        )
                    }
                }
                
                Surface(
                    onClick = onToggleSearch,
                    shape = RoundedCornerShape(20.dp),
                    color = if (searchEnabled)
                        LightBlue.copy(alpha = 0.12f)
                    else
                        LightGray,
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (searchEnabled)
                            LightBlue.copy(alpha = 0.4f)
                        else
                            GrayBorder
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Language,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = if (searchEnabled) LightBlue else GrayText
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Buscar",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Medium,
                            color = if (searchEnabled) LightBlue else GrayText
                        )
                    }
                }
            }
        }
    }
}
