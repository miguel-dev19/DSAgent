package com.dsagent.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChatBubbleOutline
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dsagent.ui.theme.DarkText
import com.dsagent.ui.theme.ErrorRed
import com.dsagent.ui.theme.GrayText
import com.dsagent.ui.theme.White

@Composable
fun ChatDrawerContent(
    userName: String,
    accountType: String,
    chatHistory: List<String> = emptyList(),
    onChatSelected: (Int) -> Unit = {}
) {
    ModalDrawerSheet(
        modifier = Modifier.width(300.dp),
        drawerContainerColor = White
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            color = Color(0xFFF8FAFC),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AvatarGenerator(userName)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = userName.ifEmpty { "Usuario" },
                        fontWeight = FontWeight.SemiBold,
                        color = DarkText
                    )
                    Text(
                        text = accountType.ifEmpty { "Free" },
                        style = MaterialTheme.typography.labelSmall,
                        color = GrayText
                    )
                }
            }
        }
        
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            color = Color(0xFFF1F5F9)
        )
        
        if (chatHistory.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Rounded.ChatBubbleOutline,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = Color(0xFFCBD5E1)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Sin conversaciones",
                        color = Color(0xFFCBD5E1)
                    )
                }
            }
        } else {
            chatHistory.forEachIndexed { index, chat ->
                TextButton(
                    onClick = { onChatSelected(index) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(chat, color = DarkText)
                }
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        HorizontalDivider(color = Color(0xFFF1F5F9))
        
        TextButton(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Icon(
                Icons.Rounded.Logout,
                contentDescription = null,
                tint = ErrorRed
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Cerrar Sesión", color = ErrorRed)
        }
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}
