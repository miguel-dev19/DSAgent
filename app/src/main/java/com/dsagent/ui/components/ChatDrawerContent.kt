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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dsagent.ui.theme.*

@Composable
fun ChatDrawerContent(userName: String, accountType: String, chatHistory: List<String>, onChatSelected: (Int) -> Unit) {
    ModalDrawerSheet(Modifier.width(300.dp), White) {
        Surface(Modifier.fillMaxWidth().padding(16.dp), Color(0xFFF8FAFC), RoundedCornerShape(16.dp)) {
            Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                AvatarGenerator(userName)
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(userName.ifEmpty { "Usuario" }, fontWeight = FontWeight.SemiBold, color = DarkText)
                    Text(accountType.ifEmpty { "Free" }, style = MaterialTheme.typography.labelSmall, color = GrayText)
                }
            }
        }
        Divider(Modifier.padding(horizontal = 16.dp), Color(0xFFF1F5F9))
        if (chatHistory.isEmpty()) {
            Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Rounded.ChatBubbleOutline, null, Modifier.size(48.dp), Color(0xFFCBD5E1))
                    Text("Sin conversaciones", color = Color(0xFFCBD5E1))
                }
            }
        } else {
            chatHistory.forEachIndexed { i, chat ->
                TextButton(onClick = { onChatSelected(i) }, modifier = Modifier.fillMaxWidth()) { Text(chat, color = DarkText) }
            }
        }
        Spacer(Modifier.weight(1f))
        TextButton(onClick = {}, modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            Icon(Icons.Rounded.Logout, null, ErrorRed)
            Spacer(Modifier.width(8.dp))
            Text("Cerrar Sesión", color = ErrorRed)
        }
    }
}
