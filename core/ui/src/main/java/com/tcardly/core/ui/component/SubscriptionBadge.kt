package com.tcardly.core.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tcardly.core.common.model.SubscriptionPlan
import com.tcardly.core.designsystem.theme.TCardlyColors

@Composable
fun SubscriptionBadge(
    plan: SubscriptionPlan,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, label) = when (plan) {
        SubscriptionPlan.FREE -> Triple(TCardlyColors.SlateLight, TCardlyColors.PureWhite, "Free")
        SubscriptionPlan.PRO -> Triple(TCardlyColors.TealDeep, TCardlyColors.PureWhite, "Pro")
        SubscriptionPlan.BUSINESS -> Triple(Color(0xFFD4A017), TCardlyColors.PureWhite, "Business")
    }
    
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = bgColor
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = textColor,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
        )
    }
}
