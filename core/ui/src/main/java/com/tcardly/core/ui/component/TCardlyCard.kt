package com.tcardly.core.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tcardly.core.designsystem.theme.TCardlyColors

@Composable
fun TCardlyCard(
    modifier: Modifier = Modifier,
    accentColor: Color = TCardlyColors.TealDeep,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(16.dp)
    
    if (onClick != null) {
        Card(
            onClick = onClick,
            modifier = modifier
                .shadow(
                    elevation = 4.dp,
                    shape = shape,
                    ambientColor = accentColor.copy(alpha = 0.06f),
                    spotColor = accentColor.copy(alpha = 0.06f)
                ),
            shape = shape,
            colors = CardDefaults.cardColors(
                containerColor = TCardlyColors.PureWhite
            )
        ) {
            Column(content = content)
        }
    } else {
        Card(
            modifier = modifier
                .shadow(
                    elevation = 4.dp,
                    shape = shape,
                    ambientColor = accentColor.copy(alpha = 0.06f),
                    spotColor = accentColor.copy(alpha = 0.06f)
                ),
            shape = shape,
            colors = CardDefaults.cardColors(
                containerColor = TCardlyColors.PureWhite
            )
        ) {
            Column(content = content)
        }
    }
}
