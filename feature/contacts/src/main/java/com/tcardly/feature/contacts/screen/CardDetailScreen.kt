package com.tcardly.feature.contacts.screen

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tcardly.core.designsystem.theme.TCardlyColors
import com.tcardly.core.ui.component.ConfidenceBadge
import com.tcardly.core.ui.component.TagChip
import com.tcardly.core.ui.component.TCardlyButton
import com.tcardly.core.ui.component.TCardlyCard
import com.tcardly.feature.contacts.viewmodel.CardDetailUiState
import com.tcardly.feature.contacts.viewmodel.CardDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardDetailScreen(
    onNavigateBack: () -> Unit,
    onNavigateToCompany: (String) -> Unit,
    onNavigateToAiAnalysis: (String) -> Unit,
    viewModel: CardDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = TCardlyColors.CloudBase,
        topBar = {
            TopAppBar(
                title = { Text("명함 상세") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "뒤로")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.toggleFavorite() }) {
                        Icon(
                            imageVector = if (uiState.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "즐겨찾기",
                            tint = if (uiState.isFavorite) TCardlyColors.CoralWarm else TCardlyColors.SlateMid
                        )
                    }
                    IconButton(onClick = { viewModel.shareCard() }) {
                        Icon(Icons.Default.Share, contentDescription = "공유")
                    }
                }
            )
        }
    ) { padding ->
        when (uiState) {
            is CardDetailUiState.Loading -> {
                Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = TCardlyColors.TealDeep)
                }
            }
            is CardDetailUiState.Error -> {
                val errorState = uiState as? CardDetailUiState.Error
                Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    Text(errorState?.message ?: "알 수 없는 오류", color = TCardlyColors.CoralWarm)
                }
            }
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    // 프로필 헤더
                    TCardlyCard {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // 프로필 이미지
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                                    .background(TCardlyColors.TealSoft),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = uiState.name.take(1),
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = TCardlyColors.TealDeep
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            Text(uiState.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)

                            if (uiState.position != null || uiState.department != null) {
                                Text(
                                    text = listOfNotNull(uiState.position, uiState.department).joinToString(" · "),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TCardlyColors.SlateMid
                                )
                            }

                            uiState.company?.let { company ->
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = company,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = TCardlyColors.TealDeep,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.clickable { onNavigateToCompany(company) }
                                )
                            }

                            // 태그
                            if (uiState.tags.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(12.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    uiState.tags.forEach { tag ->
                                        TagChip(name = tag.name, bgColor = tag.bgColor, textColor = tag.textColor)
                                    }
                                }
                            }

                            // OCR 신뢰도
                            uiState.ocrConfidence?.let { confidence ->
                                Spacer(modifier = Modifier.height(8.dp))
                                ConfidenceBadge(confidence = confidence)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 연락처 정보
                    TCardlyCard {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("연락처", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.height(12.dp))
                            uiState.mobilePhone?.let { ContactRow(Icons.Default.Phone, "휴대전화", it) }
                            uiState.officePhone?.let { ContactRow(Icons.Default.Phone, "사무실", it) }
                            uiState.email?.let { ContactRow(Icons.Default.Email, "이메일", it) }
                            uiState.address?.let { ContactRow(Icons.Default.LocationOn, "주소", it) }
                            uiState.website?.let { ContactRow(Icons.Default.Language, "웹사이트", it) }
                        }
                    }

                    // 메모
                    uiState.memo?.let { memo ->
                        Spacer(modifier = Modifier.height(16.dp))
                        TCardlyCard {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("메모", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(memo, style = MaterialTheme.typography.bodyMedium, color = TCardlyColors.SlateMid)
                            }
                        }
                    }

                    // AI 분석 버튼
                    uiState.company?.let { company ->
                        Spacer(modifier = Modifier.height(16.dp))
                        TCardlyButton(
                            text = "AI 기업 분석",
                            onClick = { onNavigateToAiAnalysis(company) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
private fun ContactRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = label, tint = TCardlyColors.TealDeep, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(label, style = MaterialTheme.typography.labelSmall, color = TCardlyColors.SlateLight)
            Text(value, style = MaterialTheme.typography.bodyMedium, color = TCardlyColors.SlateDeep)
        }
    }
}
