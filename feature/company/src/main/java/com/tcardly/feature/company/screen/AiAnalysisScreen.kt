package com.tcardly.feature.company.screen

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tcardly.core.designsystem.theme.TCardlyColors
import com.tcardly.core.ui.component.TCardlyButton
import com.tcardly.core.ui.component.TCardlyCard
import com.tcardly.feature.company.viewmodel.AiAnalysisViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiAnalysisScreen(
    companyName: String,
    onNavigateBack: () -> Unit,
    onNavigateToAiChat: (String) -> Unit,
    onNavigateToPaywall: () -> Unit,
    viewModel: AiAnalysisViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(companyName) {
        viewModel.requestAnalysis(companyName)
    }

    Scaffold(
        containerColor = TCardlyColors.CloudBase,
        topBar = {
            TopAppBar(
                title = { Text("AI 기업 분석") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "뒤로")
                    }
                }
            )
        }
    ) { padding ->
        when {
            uiState.isLoading -> {
                Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = TCardlyColors.TealDeep)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("AI가 ${companyName}을 분석하고 있습니다...", color = TCardlyColors.SlateMid)
                    }
                }
            }
            uiState.isLimitExceeded -> {
                Box(Modifier.fillMaxSize().padding(padding).padding(24.dp), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = TCardlyColors.Amber, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("AI 분석 한도 초과", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("이번 달 AI 분석 횟수를 모두 사용했습니다.\nPro 플랜으로 업그레이드하면 월 30회까지 사용할 수 있습니다.",
                            color = TCardlyColors.SlateMid, style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(24.dp))
                        TCardlyButton(text = "Pro 플랜 보기", onClick = onNavigateToPaywall, modifier = Modifier.fillMaxWidth())
                    }
                }
            }
            uiState.error != null -> {
                Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(uiState.error!!, color = TCardlyColors.CoralWarm)
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedButton(onClick = { viewModel.requestAnalysis(companyName) }) { Text("다시 시도") }
                    }
                }
            }
            uiState.report != null -> {
                val report = uiState.report!!
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    // 투자 매력도
                    TCardlyCard {
                        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("투자 매력도", style = MaterialTheme.typography.titleMedium)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("${report.investmentAttractiveness}",
                                style = MaterialTheme.typography.displayMedium,
                                fontWeight = FontWeight.Bold,
                                color = when {
                                    report.investmentAttractiveness >= 70 -> TCardlyColors.Emerald
                                    report.investmentAttractiveness >= 40 -> TCardlyColors.Amber
                                    else -> TCardlyColors.CoralWarm
                                }
                            )
                            Text("/ 100", color = TCardlyColors.SlateMid)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // AI 요약
                    if (report.aiSummary.isNotBlank()) {
                        TCardlyCard {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("AI 요약", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(report.aiSummary, style = MaterialTheme.typography.bodyMedium, color = TCardlyColors.SlateDeep)
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // SWOT 분석
                    report.swot?.let { swot ->
                        TCardlyCard {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("SWOT 분석", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                                Spacer(modifier = Modifier.height(12.dp))
                                SwotSection("강점", swot.strengths, TCardlyColors.Emerald)
                                SwotSection("약점", swot.weaknesses, TCardlyColors.CoralWarm)
                                SwotSection("기회", swot.opportunities, TCardlyColors.Sky)
                                SwotSection("위협", swot.threats, TCardlyColors.Amber)
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // 사업전망
                    if (report.businessOutlook.isNotBlank()) {
                        TCardlyCard {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("사업 전망", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(report.businessOutlook, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // 리스크
                    if (report.riskIssues.isNotBlank()) {
                        TCardlyCard {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("리스크 이슈", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(report.riskIssues, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // AI Q&A
                    OutlinedButton(
                        onClick = { onNavigateToAiChat(companyName) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TCardlyColors.TealDeep)
                    ) {
                        Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("AI에게 추가 질문하기")
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
private fun SwotSection(title: String, items: List<String>, color: androidx.compose.ui.graphics.Color) {
    if (items.isEmpty()) return
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(8.dp).background(color, RoundedCornerShape(4.dp)))
            Spacer(modifier = Modifier.width(8.dp))
            Text(title, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold, color = color)
        }
        items.forEach { item ->
            Text("  · $item", style = MaterialTheme.typography.bodySmall, color = TCardlyColors.SlateDeep,
                modifier = Modifier.padding(start = 16.dp, top = 2.dp))
        }
        Spacer(modifier = Modifier.height(4.dp))
    }
}
