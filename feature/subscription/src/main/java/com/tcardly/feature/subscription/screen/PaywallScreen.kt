package com.tcardly.feature.subscription.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tcardly.core.common.model.SubscriptionPlan
import com.tcardly.core.designsystem.theme.TCardlyColors
import com.tcardly.core.ui.component.TCardlyButton
import com.tcardly.feature.subscription.viewmodel.PaywallViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaywallScreen(
    onNavigateBack: () -> Unit,
    onSubscribed: () -> Unit,
    viewModel: PaywallViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.purchaseSuccess) {
        if (uiState.purchaseSuccess) {
            viewModel.resetPurchaseSuccess()
            onSubscribed()
        }
    }

    Scaffold(
        containerColor = TCardlyColors.CloudBase,
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.Close, contentDescription = "닫기")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding)
                .verticalScroll(rememberScrollState()).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Default.Workspace_Premium, contentDescription = null, tint = TCardlyColors.TealDeep,
                modifier = Modifier.size(56.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text("T-CARDLY Pro", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("더 강력한 명함 관리를 시작하세요", color = TCardlyColors.SlateMid)

            Spacer(modifier = Modifier.height(32.dp))

            // 플랜 카드
            PlanCard(
                plan = SubscriptionPlan.PRO,
                price = "₩9,900/월",
                features = listOf("광고 제거", "AI 기업 분석 월 30회", "무제한 엑셀 내보내기", "우선 지원"),
                isSelected = uiState.selectedPlan == SubscriptionPlan.PRO,
                isCurrentPlan = uiState.currentPlan == SubscriptionPlan.PRO,
                onSelect = { viewModel.selectPlan(SubscriptionPlan.PRO) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            PlanCard(
                plan = SubscriptionPlan.BUSINESS,
                price = "₩29,900/월",
                features = listOf("Pro 기능 전체 포함", "무제한 AI 분석", "팀 공유 기능", "전담 매니저"),
                isSelected = uiState.selectedPlan == SubscriptionPlan.BUSINESS,
                isCurrentPlan = uiState.currentPlan == SubscriptionPlan.BUSINESS,
                onSelect = { viewModel.selectPlan(SubscriptionPlan.BUSINESS) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            TCardlyButton(
                text = when {
                    uiState.isPurchasing -> "처리 중..."
                    uiState.currentPlan == uiState.selectedPlan -> "현재 플랜"
                    else -> "구독 시작"
                },
                onClick = { viewModel.purchase() },
                enabled = !uiState.isPurchasing && uiState.currentPlan != uiState.selectedPlan,
                modifier = Modifier.fillMaxWidth()
            )

            uiState.error?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(it, color = TCardlyColors.CoralWarm, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = { viewModel.restorePurchases() },
                enabled = !uiState.isRestoring
            ) {
                Text(
                    if (uiState.isRestoring) "복원 중..." else "구매 복원",
                    color = TCardlyColors.TealDeep
                )
            }
        }
    }
}

@Composable
private fun PlanCard(
    plan: SubscriptionPlan,
    price: String,
    features: List<String>,
    isSelected: Boolean,
    isCurrentPlan: Boolean,
    onSelect: () -> Unit
) {
    val borderColor = if (isSelected) TCardlyColors.TealDeep else TCardlyColors.BorderSoft
    Surface(
        modifier = Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(2.dp, borderColor, RoundedCornerShape(16.dp))
            .clickable(onClick = onSelect),
        color = if (isSelected) TCardlyColors.TealSoft.copy(alpha = 0.3f) else TCardlyColors.PureWhite
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(plan.displayName, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                if (isCurrentPlan) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(shape = RoundedCornerShape(12.dp), color = TCardlyColors.TealSoft) {
                        Text("현재", modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            color = TCardlyColors.TealDeep, style = MaterialTheme.typography.labelSmall)
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                RadioButton(selected = isSelected, onClick = onSelect,
                    colors = RadioButtonDefaults.colors(selectedColor = TCardlyColors.TealDeep))
            }
            Text(price, style = MaterialTheme.typography.titleMedium, color = TCardlyColors.TealDeep, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(12.dp))
            features.forEach { feature ->
                Row(modifier = Modifier.padding(vertical = 2.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = TCardlyColors.Emerald, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(feature, style = MaterialTheme.typography.bodyMedium, color = TCardlyColors.SlateDeep)
                }
            }
        }
    }
}
