package com.tcardly.core.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tcardly.core.designsystem.theme.TCardlyColors

enum class TCardlyButtonStyle { PRIMARY, SECONDARY, GHOST }

@Composable
fun TCardlyButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: TCardlyButtonStyle = TCardlyButtonStyle.PRIMARY,
    isLoading: Boolean = false,
    enabled: Boolean = true
) {
    val shape = RoundedCornerShape(12.dp)
    
    when (style) {
        TCardlyButtonStyle.PRIMARY -> {
            Button(
                onClick = onClick,
                modifier = modifier.height(52.dp),
                enabled = enabled && !isLoading,
                shape = shape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = TCardlyColors.TealDeep,
                    contentColor = TCardlyColors.PureWhite
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = TCardlyColors.PureWhite,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(text)
                }
            }
        }
        TCardlyButtonStyle.SECONDARY -> {
            OutlinedButton(
                onClick = onClick,
                modifier = modifier.height(52.dp),
                enabled = enabled && !isLoading,
                shape = shape,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = TCardlyColors.TealDeep
                )
            ) {
                Text(text)
            }
        }
        TCardlyButtonStyle.GHOST -> {
            TextButton(
                onClick = onClick,
                modifier = modifier.height(52.dp),
                enabled = enabled && !isLoading,
                shape = shape,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = TCardlyColors.SlateMid
                )
            ) {
                Text(text)
            }
        }
    }
}
