package com.tcardly.core.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tcardly.core.designsystem.theme.TCardlyColors

@Composable
fun ConfidenceBadge(
    confidence: Float,
    modifier: Modifier = Modifier
) {
    val (color, icon, label) = when {
        confidence >= 0.8f -> Triple(TCardlyColors.Emerald, Icons.Default.Check, "높음")
        confidence >= 0.5f -> Triple(TCardlyColors.Amber, Icons.Default.Warning, "보통")
        else -> Triple(TCardlyColors.CoralWarm, Icons.Default.Error, "낮음")
    }
    
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = color.copy(alpha = 0.1f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(14.dp))
            Text(
                text = "${(confidence * 100).toInt()}% $label",
                style = MaterialTheme.typography.labelSmall,
                color = color
            )
        }
    }
}
