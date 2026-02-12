package com.tcardly.core.ui.util

import androidx.compose.ui.graphics.Color

fun String.toComposeColor(): Color {
    return try {
        val hex = this.removePrefix("#")
        val colorLong = hex.toLong(16)
        when (hex.length) {
            6 -> Color(0xFF000000 or colorLong)
            8 -> Color(colorLong)
            else -> Color.Gray
        }
    } catch (_: Exception) {
        Color.Gray
    }
}
