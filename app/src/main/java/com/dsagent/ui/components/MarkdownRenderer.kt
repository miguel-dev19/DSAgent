package com.dsagent.ui.components

import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import com.dsagent.ui.theme.*

@Composable
fun MarkdownRenderer(text: String) {
    val cleanedText = remember(text) { cleanMarkdown(text) }
    val annotatedString = remember(cleanedText) { parseMarkdown(cleanedText) }
    
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

fun cleanMarkdown(text: String): String {
    var cleaned = text
    // Eliminar FINISHED
    cleaned = Regex("\\s*FINISHED\\s*", RegexOption.IGNORE_CASE).replace(cleaned, "")
    // Convertir listas con * a -
    cleaned = Regex("^\\s*\\*\\s+", RegexOption.MULTILINE).replace(cleaned, "- ")
    // Limpiar espacios multiples
    cleaned = Regex(" +").replace(cleaned, " ")
    // Limpiar saltos excesivos
    cleaned = Regex("\n{3,}").replace(cleaned, "\n\n")
    return cleaned.trim()
}

fun parseMarkdown(text: String): AnnotatedString {
    return buildAnnotatedString {
        var bold = false
        var italic = false
        var code = false
        var i = 0
        
        while (i < text.length) {
            when {
                text.startsWith("**", i) -> { bold = !bold; i += 2 }
                text.startsWith("*", i) -> { italic = !italic; i += 1 }
                text.startsWith("`", i) -> { code = !code; i += 1 }
                text.startsWith("[", i) -> {
                    val end = text.indexOf("](", i)
                    val urlEnd = text.indexOf(")", end)
                    if (end != -1 && urlEnd != -1) {
                        val linkText = text.substring(i + 1, end)
                        val url = text.substring(end + 2, urlEnd)
                        pushStringAnnotation("url", url)
                        withStyle(SpanStyle(color = LightBlue, textDecoration = TextDecoration.Underline)) {
                            append(linkText)
                        }
                        pop()
                        i = urlEnd + 1
                    } else { append(text[i]); i++ }
                }
                text.startsWith("```", i) -> {
                    val end = text.indexOf("```", i + 3)
                    if (end != -1) {
                        withStyle(SpanStyle(fontFamily = FontFamily.Monospace, background = LightGray.copy(alpha = 0.3f))) {
                            append(text.substring(i + 3, end).trim())
                        }
                        i = end + 3
                    } else { append(text[i]); i++ }
                }
                text.startsWith("# ", i) -> {
                    val end = text.indexOf("\n", i)
                    val title = if (end != -1) text.substring(i + 2, end) else text.substring(i + 2)
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)) {
                        append(title)
                    }
                    i = if (end != -1) end else text.length
                }
                text.startsWith("- ", i) -> {
                    append("\n  - ")
                    i += 2
                }
                else -> {
                    val style = SpanStyle(
                        fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
                        fontStyle = if (italic) FontStyle.Italic else FontStyle.Normal,
                        fontFamily = if (code) FontFamily.Monospace else FontFamily.Default,
                        background = if (code) LightGray.copy(alpha = 0.3f) else androidx.compose.ui.graphics.Color.Transparent
                    )
                    withStyle(style) { append(text[i]) }
                    i++
                }
            }
        }
    }
}
