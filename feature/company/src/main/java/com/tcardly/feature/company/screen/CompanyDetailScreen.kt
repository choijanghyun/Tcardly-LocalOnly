package com.tcardly.feature.company.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tcardly.core.designsystem.theme.TCardlyColors
import com.tcardly.core.ui.component.TCardlyButton
import com.tcardly.core.ui.component.TCardlyCard
import com.tcardly.feature.company.viewmodel.CompanyDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyDetailScreen(
    companyName: String,
    onNavigateBack: () -> Unit,
    onNavigateToAiAnalysis: (String) -> Unit,
    onNavigateToAiChat: (String) -> Unit,
    viewModel: CompanyDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(companyName) {
        viewModel.loadCompany(companyName)
    }

    Scaffold(
        containerColor = TCardlyColors.CloudBase,
        topBar = {
            TopAppBar(
                title = { Text(companyName) },
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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                val info = uiState.companyInfo

                // 기본 정보
                TCardlyCard {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("기업 정보", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(12.dp))
                        info?.ceoName?.let { InfoRow("대표", it) }
                        info?.industry?.let { InfoRow("업종", it) }
                        info?.foundedDate?.let { InfoRow("설립일", it) }
                        info?.employeeCount?.let { InfoRow("직원 수", "${it}명") }
                        info?.address?.let { InfoRow("주소", it) }
                        info?.website?.let { InfoRow("웹사이트", it) }
                        info?.creditGrade?.let { InfoRow("신용등급", it) }
                        if (info?.isListed == true) InfoRow("상장", "상장기업")

                        if (info == null && uiState.error != null) {
                            Text(uiState.error.orEmpty(), color = TCardlyColors.CoralWarm, style = MaterialTheme.typography.bodySmall)
                        } else if (info == null) {
                            Text("기업 정보를 불러오는 중...", color = TCardlyColors.SlateMid)
                        }
                    }
                }

                // 재무 정보
                info?.financialData?.takeIf { it.isNotEmpty() }?.let { years ->
                    Spacer(modifier = Modifier.height(16.dp))
                    TCardlyCard {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("재무 정보", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.height(12.dp))
                            years.sortedByDescending { it.year }.forEach { fy ->
                                Text("${fy.year}년", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
                                fy.revenue?.let { InfoRow("매출", formatKrw(it)) }
                                fy.operatingProfit?.let { InfoRow("영업이익", formatKrw(it)) }
                                fy.netIncome?.let { InfoRow("순이익", formatKrw(it)) }
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // AI 분석 버튼
                TCardlyButton(
                    text = "AI 기업 분석",
                    onClick = { onNavigateToAiAnalysis(companyName) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = { onNavigateToAiChat(companyName) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TCardlyColors.TealDeep)
                ) {
                    Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("AI에게 질문하기")
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = TCardlyColors.SlateMid)
        Text(value, style = MaterialTheme.typography.bodyMedium, color = TCardlyColors.SlateDeep, fontWeight = FontWeight.Medium)
    }
}

private fun formatKrw(amount: Long): String {
    return when {
        amount >= 1_0000_0000_0000 -> "${amount / 1_0000_0000_0000}조 ${(amount % 1_0000_0000_0000) / 1_0000_0000}억원"
        amount >= 1_0000_0000 -> "${amount / 1_0000_0000}억원"
        amount >= 1_0000 -> "${amount / 1_0000}만원"
        else -> "${amount}원"
    }
}
