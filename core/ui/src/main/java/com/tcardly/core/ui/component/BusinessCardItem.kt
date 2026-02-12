package com.tcardly.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tcardly.core.designsystem.theme.TCardlyColors

@Composable
fun BusinessCardItem(
    name: String,
    company: String?,
    position: String?,
    profileImageUri: String?,
    modifier: Modifier = Modifier,
    onTap: () -> Unit = {},
    onCompanyTap: (() -> Unit)? = null
) {
    TCardlyCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        onClick = onTap
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Teal accent bar
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(48.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(TCardlyColors.TealDeep)
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Profile image
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(TCardlyColors.TealSoft),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = TCardlyColors.TealDeep,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = TCardlyColors.SlateDeep,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                if (!company.isNullOrBlank()) {
                    Text(
                        text = company,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (onCompanyTap != null) TCardlyColors.TealDeep else TCardlyColors.SlateMid,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = if (onCompanyTap != null) {
                            Modifier.clickable { onCompanyTap() }
                        } else Modifier
                    )
                }
                
                if (!position.isNullOrBlank()) {
                    Text(
                        text = position,
                        style = MaterialTheme.typography.bodySmall,
                        color = TCardlyColors.SlateLight,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
