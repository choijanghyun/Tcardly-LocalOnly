package com.tcardly.core.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.tcardly.core.designsystem.theme.TCardlyColors

@Composable
fun TCardlyTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    leadingIcon: ImageVector? = null,
    error: String? = null,
    singleLine: Boolean = true,
    enabled: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        label = label?.let { { Text(it) } },
        placeholder = placeholder?.let { { Text(it, color = TCardlyColors.SlateLight) } },
        leadingIcon = leadingIcon?.let { { Icon(it, contentDescription = null, tint = TCardlyColors.SlateMid) } },
        isError = error != null,
        supportingText = error?.let { { Text(it, color = TCardlyColors.CoralWarm) } },
        singleLine = singleLine,
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = TCardlyColors.TealDeep,
            unfocusedBorderColor = TCardlyColors.BorderSoft,
            cursorColor = TCardlyColors.TealDeep,
            focusedLabelColor = TCardlyColors.TealDeep
        )
    )
}
