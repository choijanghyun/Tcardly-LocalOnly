package com.tcardly.core.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TagChip(
    name: String,
    bgColor: Color,
    textColor: Color,
    isSelected: Boolean = false,
    onToggle: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    if (onToggle != null) {
        FilterChip(
            selected = isSelected,
            onClick = onToggle,
            label = { Text(name, style = MaterialTheme.typography.labelMedium) },
            modifier = modifier,
            shape = RoundedCornerShape(20.dp),
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = bgColor,
                selectedLabelColor = textColor
            )
        )
    } else {
        SuggestionChip(
            onClick = {},
            label = { Text(name, style = MaterialTheme.typography.labelSmall, color = textColor) },
            modifier = modifier,
            shape = RoundedCornerShape(20.dp),
            colors = SuggestionChipDefaults.suggestionChipColors(
                containerColor = bgColor
            )
        )
    }
}
