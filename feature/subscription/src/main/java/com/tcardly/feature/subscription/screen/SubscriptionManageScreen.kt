package com.tcardly.feature.subscription.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tcardly.core.common.model.SubscriptionPlan
import com.tcardly.core.common.model.SubscriptionStatus
import com.tcardly.core.designsystem.theme.TCardlyColors
import com.tcardly.core.ui.component.TCardlyButton
import com.tcardly.core.ui.component.TCardlyCard
import com.tcardly.feature.subscription.viewmodel.SubscriptionManageViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionManageScreen(
    onNavigateBack: () -> Unit,
    onNavigateToPaywall: () -> Unit,
    viewModel: SubscriptionManageViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val sub = uiState.subscriptionState

    Scaffold(
        containerColor = TCardlyColors.CloudBase,
        topBar = {
            TopAppBar(
                title = { Text("구독 관리") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "뒤로")
                    }
                }
            )
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = TCardlyColors.TealDeep)
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)
            ) {
                // 현재 플랜
                TCardlyCard {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("현재 플랜", style = MaterialTheme.typography.labelMedium, color = TCardlyColors.SlateMid)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(sub.plan.displayName, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold,
                            color = TCardlyColors.TealDeep)

                        if (sub.status == SubscriptionStatus.ACTIVE) {
                            sub.expiresAt?.let { expiresAt ->
                                Spacer(modifier = Modifier.height(8.dp))
                                val dateStr = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA).format(Date(expiresAt))
                                Text("다음 결제일: $dateStr", color = TCardlyColors.SlateMid)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 사용량
                TCardlyCard {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("이번 달 사용량", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(16.dp))

                        UsageRow(
                            label = "AI 기업 분석",
                            used = sub.aiUsageCount,
                            limit = sub.aiUsageLimit,
                            isUnlimited = sub.plan == SubscriptionPlan.BUSINESS
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        UsageRow(
                            label = "엑셀 내보내기",
                            used = sub.exportCount,
                            limit = sub.exportLimit,
                            isUnlimited = sub.plan != SubscriptionPlan.FREE
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (sub.plan == SubscriptionPlan.FREE) {
                    TCardlyButton(
                        text = "Pro로 업그레이드",
                        onClick = onNavigateToPaywall,
                        modifier = Modifier.fillMaxWidth()
                    )
                } else if (sub.plan == SubscriptionPlan.PRO) {
                    OutlinedButton(
                        onClick = onNavigateToPaywall,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TCardlyColors.TealDeep)
                    ) {
                        Text("Business로 업그레이드")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                TextButton(
                    onClick = { viewModel.restorePurchases() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("구매 복원", color = TCardlyColors.TealDeep)
                }
            }
        }
    }
}

@Composable
private fun UsageRow(label: String, used: Int, limit: Int, isUnlimited: Boolean) {
    Column {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, style = MaterialTheme.typography.bodyMedium, color = TCardlyColors.SlateDeep)
            Text(
                if (isUnlimited) "$used / 무제한" else "$used / $limit",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = if (!isUnlimited && used >= limit) TCardlyColors.CoralWarm else TCardlyColors.TealDeep
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        if (!isUnlimited) {
            LinearProgressIndicator(
                progress = { (used.toFloat() / limit).coerceIn(0f, 1f) },
                modifier = Modifier.fillMaxWidth().height(6.dp),
                color = if (used >= limit) TCardlyColors.CoralWarm else TCardlyColors.TealDeep,
                trackColor = TCardlyColors.BorderSoft
            )
        }
    }
}
