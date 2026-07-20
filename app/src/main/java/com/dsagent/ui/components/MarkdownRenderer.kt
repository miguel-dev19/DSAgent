package com.dsagent.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dsagent.ui.theme.*

@Composable
fun MarkdownRenderer(text: String) {
    val annotatedString = remember(text) { parseMarkdown(text) }
    
    ClickableText(
        text = annotatedString,
        style = MaterialTheme.typography.bodyMedium.copy(
            color = DarkText,
            lineHeight = 24.sp
        ),
        onClick = { offset ->
            annotatedString.getStringAnnotations("url", offset, offset)
                .firstOrNull()?.let { /* Abrir URL */ }
        }
    )
}

fun parseMarkdown(text: String): AnnotatedString {
    return buildAnnotatedString {
        var bold = false
        var italic = false
        var code = false
        var i = 0
        
        while (i < text.length) {
            when {
                // Negrita **texto**
                text.startsWith("**", i) -> {
                    bold = !bold
                    i += 2
                }
                // Cursiva *texto*
                text.startsWith("*", i) && !text.startsWith("**", i) -> {
                    italic = !italic
                    i += 1
                }
                // Codigo `texto`
                text.startsWith("`", i) -> {
                    code = !code
                    i += 1
                }
                // Enlace [texto](url)
                text.startsWith("[", i) -> {
                    val end = text.indexOf("](", i)
                    val urlEnd = text.indexOf(")", end)
                    if (end != -1 && urlEnd != -1) {
                        val linkText = text.substring(i + 1, end)
                        val url = text.substring(end + 2, urlEnd)
                        pushStringAnnotation("url", url)
                        withStyle(SpanStyle(
                            color = LightBlue,
                            textDecoration = TextDecoration.Underline
                        )) {
                            append(linkText)
                        }
                        pop()
                        i = urlEnd + 1
                    } else {
                        append(text[i])
                        i++
                    }
                }
                // Salto de linea
                text.startsWith("\n", i) -> {
                    append("\n")
                    i++
                }
                else -> {
                    val style = SpanStyle(
                        fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
                        fontStyle = if (italic) FontStyle.Italic else FontStyle.Normal,
                        fontFamily = if (code) FontFamily.Monospace else FontFamily.Default,
                        background = if (code) LightGray.copy(alpha = 0.3f) else androidx.compose.ui.graphics.Color.Transparent
                    )
                    withStyle(style) {
                        append(text[i])
                    }
                    i++
                }
            }
        }
    }
}
