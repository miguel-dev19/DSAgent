package com.dsagent.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dsagent.ui.theme.*

@Composable
fun CodeBlock(code: String, language: String = "code") {
    var copied by remember { mutableStateOf(false) }
    val clipboard = LocalClipboardManager.current
    
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        color = CodeBackground,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            // Cabecera: lenguaje + boton copiar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = language,
                    style = MaterialTheme.typography.labelMedium,
                    color = LightBlue,
                    fontWeight = FontWeight.Medium
                )
                
                TextButton(onClick = {
                    clipboard.setText(AnnotatedString(code))
                    copied = true
                }) {
                    Icon(
                        if (copied) Icons.Rounded.Check else Icons.Rounded.ContentCopy,
                        contentDescription = "Copiar codigo",
                        tint = if (copied) SuccessGreen else LightBlue,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        if (copied) "Copiado" else "Copiar",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (copied) SuccessGreen else LightBlue
                    )
                }
            }
            
            // Contenido del codigo
            Text(
                text = code,
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
                fontFamily = FontFamily.Monospace,
                color = White,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
