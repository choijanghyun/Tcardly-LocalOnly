package com.tcardly.feature.settings.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tcardly.core.common.model.SubscriptionPlan
import com.tcardly.core.designsystem.theme.TCardlyColors
import com.tcardly.core.ui.component.SubscriptionBadge
import com.tcardly.core.ui.component.TCardlyCard
import com.tcardly.feature.settings.viewmodel.MenuViewModel

@Composable
fun MenuScreen(
    onNavigateToProfile: () -> Unit,
    onNavigateToSubscriptionManage: () -> Unit,
    onNavigateToGroupTagManage: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToPaywall: () -> Unit,
    viewModel: MenuViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TCardlyColors.CloudBase)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // 프로필 헤더
        TCardlyCard(modifier = Modifier.clickable(onClick = onNavigateToProfile)) {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(56.dp).clip(CircleShape).background(TCardlyColors.TealSoft),
                    contentAlignment = Alignment.Center
                ) {
                    Text(uiState.userName.take(1), style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold, color = TCardlyColors.TealDeep)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(uiState.userName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(8.dp))
                        SubscriptionBadge(plan = uiState.plan)
                    }
                    if (uiState.userCompany.isNotBlank()) {
                        Text(uiState.userCompany, color = TCardlyColors.SlateMid, style = MaterialTheme.typography.bodySmall)
                    }
                }
                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TCardlyColors.SlateLight)
            }
        }

        // 통계
        Spacer(modifier = Modifier.height(16.dp))
        TCardlyCard {
            Row(
                modifier = Modifier.fillMaxWidth().padding(20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem("총 명함", "${uiState.totalCardCount}")
            }
        }

        // Pro CTA (Free 사용자만)
        if (uiState.plan == SubscriptionPlan.FREE) {
            Spacer(modifier = Modifier.height(16.dp))
            Surface(
                modifier = Modifier.fillMaxWidth().clickable(onClick = onNavigateToPaywall),
                shape = RoundedCornerShape(16.dp),
                color = TCardlyColors.TealDeep
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Workspace_Premium, contentDescription = null, tint = TCardlyColors.PureWhite)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Pro로 업그레이드", color = TCardlyColors.PureWhite, fontWeight = FontWeight.Bold)
                        Text("광고 제거 · AI 분석 30회 · 무제한 내보내기",
                            color = TCardlyColors.PureWhite.copy(alpha = 0.8f),
                            style = MaterialTheme.typography.bodySmall)
                    }
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TCardlyColors.PureWhite)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 메뉴 항목
        Text("관리", style = MaterialTheme.typography.labelMedium, color = TCardlyColors.SlateMid,
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp))
        TCardlyCard {
            Column {
                MenuItem(Icons.Default.Subscriptions, "구독 관리", onNavigateToSubscriptionManage)
                HorizontalDivider(color = TCardlyColors.BorderSoft)
                MenuItem(Icons.Default.Label, "그룹·태그 관리", onNavigateToGroupTagManage)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("설정", style = MaterialTheme.typography.labelMedium, color = TCardlyColors.SlateMid,
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp))
        TCardlyCard {
            Column {
                MenuItem(Icons.Default.Settings, "앱 설정", onNavigateToSettings)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = TCardlyColors.TealDeep)
        Text(label, style = MaterialTheme.typography.bodySmall, color = TCardlyColors.SlateMid)
    }
}

@Composable
private fun MenuItem(icon: ImageVector, title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = title, tint = TCardlyColors.TealDeep, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(title, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TCardlyColors.SlateLight, modifier = Modifier.size(20.dp))
    }
}
