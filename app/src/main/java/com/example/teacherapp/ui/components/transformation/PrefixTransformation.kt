package com.example.teacherapp.ui.components.transformation

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle

// TODO: Prefix could be replaced with TextField prefix slot.
class PrefixTransformation(private val prefix: String) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val prefix = buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Light)) {
                append(prefix)
            }
        }

        val prefixOffset = prefix.length

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return offset + prefixOffset
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset < prefixOffset) return 0
                return offset - prefixOffset
            }
        }

        val result = prefix + text

        return TransformedText(
            text = result,
            offsetMapping = offsetMapping,
        )
    }
}