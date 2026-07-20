package com.dsagent.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dsagent.ui.theme.*

@Composable
fun MarkdownRenderer(text: String) {
    val blocks = remember(text) { parseMarkdownBlocks(text) }
    
    Column(modifier = Modifier.fillMaxWidth()) {
        blocks.forEach { block ->
            when (block) {
                is MarkdownBlock.Text -> {
                    ClickableText(
                        text = block.annotatedString,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = DarkText,
                            lineHeight = 24.sp
                        ),
                        onClick = { offset ->
                            block.annotatedString.getStringAnnotations("url", offset, offset)
                                .firstOrNull()?.let { /* Abrir URL */ }
                        }
                    )
                }
                is MarkdownBlock.Code -> {
                    CodeBlock(code = block.code, language = block.language)
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

sealed class MarkdownBlock {
    data class Text(val annotatedString: AnnotatedString) : MarkdownBlock()
    data class Code(val code: String, val language: String) : MarkdownBlock()
}

fun parseMarkdownBlocks(text: String): List<MarkdownBlock> {
    val blocks = mutableListOf<MarkdownBlock>()
    val cleaned = cleanMarkdown(text)
    
    // Detectar bloques de codigo ```
    val codeRegex = Regex("```(\\w*)\\n([\\s\\S]*?)```")
    var lastIndex = 0
    
    codeRegex.findAll(cleaned).forEach { match ->
        // Texto antes del bloque de codigo
        if (match.range.first > lastIndex) {
            val before = cleaned.substring(lastIndex, match.range.first)
            if (before.isNotBlank()) {
                blocks.add(MarkdownBlock.Text(parseInlineMarkdown(before)))
            }
        }
        
        // Bloque de codigo
        val language = match.groupValues[1].ifEmpty { "code" }
        val code = match.groupValues[2].trim()
        blocks.add(MarkdownBlock.Code(code, language))
        
        lastIndex = match.range.last + 1
    }
    
    // Texto restante despues del ultimo bloque
    if (lastIndex < cleaned.length) {
        val after = cleaned.substring(lastIndex)
        if (after.isNotBlank()) {
            blocks.add(MarkdownBlock.Text(parseInlineMarkdown(after)))
        }
    }
    
    // Si no hay bloques de codigo, todo es texto
    if (blocks.isEmpty() && cleaned.isNotBlank()) {
        blocks.add(MarkdownBlock.Text(parseInlineMarkdown(cleaned)))
    }
    
    return blocks
}

fun cleanMarkdown(text: String): String {
    var cleaned = text
    cleaned = Regex("\\s*FINISHED\\s*", RegexOption.IGNORE_CASE).replace(cleaned, "")
    cleaned = Regex("^\\s*\\*\\s+", RegexOption.MULTILINE).replace(cleaned, "- ")
    cleaned = Regex(" +").replace(cleaned, " ")
    cleaned = Regex("\n{3,}").replace(cleaned, "\n\n")
    return cleaned.trim()
}

fun parseInlineMarkdown(text: String): AnnotatedString {
    return buildAnnotatedString {
        var bold = false
        var italic = false
        var code = false
        var i = 0
        
        while (i < text.length) {
            when {
                text.startsWith("**", i) -> { bold = !bold; i += 2 }
                text.startsWith("*", i) && !text.startsWith("**", i) -> { italic = !italic; i += 1 }
                text.startsWith("`", i) -> { code = !code; i += 1 }
                text.startsWith("[", i) -> {
                    val end = text.indexOf("](", i)
                    val urlEnd = text.indexOf(")", end)
                    if (end != -1 && urlEnd != -1) {
                        pushStringAnnotation("url", text.substring(end + 2, urlEnd))
                        withStyle(SpanStyle(color = LightBlue, textDecoration = TextDecoration.Underline)) {
                            append(text.substring(i + 1, end))
                        }
                        pop()
                        i = urlEnd + 1
                    } else { append(text[i]); i++ }
                }
                text.startsWith("### ", i) -> {
                    val end = text.indexOf("\n", i).let { if (it == -1) text.length else it }
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp)) {
                        append(text.substring(i + 4, end))
                        append("\n")
                    }
                    i = end
                }
                text.startsWith("## ", i) -> {
                    val end = text.indexOf("\n", i).let { if (it == -1) text.length else it }
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)) {
                        append(text.substring(i + 3, end))
                        append("\n")
                    }
                    i = end
                }
                text.startsWith("# ", i) -> {
                    val end = text.indexOf("\n", i).let { if (it == -1) text.length else it }
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold, fontSize = 22.sp)) {
                        append(text.substring(i + 2, end))
                        append("\n")
                    }
                    i = end
                }
                text.startsWith("- ", i) -> {
                    append("\n  - ")
                    i += 2
                }
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
                    withStyle(style) { append(text[i]) }
                    i++
                }
            }
        }
    }
}
