package com.tcardly.feature.home.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tcardly.core.common.model.SubscriptionPlan
import com.tcardly.core.designsystem.theme.TCardlyColors
import com.tcardly.core.ui.component.BusinessCardItem
import com.tcardly.core.ui.component.TCardlyButton
import com.tcardly.core.ui.component.TCardlyButtonStyle
import com.tcardly.core.ui.component.TCardlyCard
import com.tcardly.feature.home.viewmodel.HomeUiState
import com.tcardly.feature.home.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToProfile: () -> Unit,
    onNavigateToCardDetail: (Long) -> Unit,
    onNavigateToCompany: (String) -> Unit,
    onNavigateToScan: () -> Unit,
    onNavigateToPaywall: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = TCardlyColors.CloudBase,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "T-CARDLY",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = TCardlyColors.TealDeep
                    )
                },
                navigationIcon = {
                    Box(
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(TCardlyColors.TealSoft)
                            .clickable { onNavigateToProfile() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "프로필",
                            tint = TCardlyColors.TealDeep,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = TCardlyColors.CloudBase
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToScan,
                containerColor = TCardlyColors.TealDeep,
                contentColor = TCardlyColors.PureWhite
            ) {
                Icon(Icons.Default.CameraAlt, contentDescription = "스캔")
            }
        }
    ) { padding ->
        when (val state = uiState) {
            is HomeUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = TCardlyColors.TealDeep)
                }
            }
            is HomeUiState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    // 인사 헤더
                    item {
                        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
                            Text(
                                text = state.greeting,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = TCardlyColors.SlateDeep
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "보유 명함 ${state.cardCount}장",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TCardlyColors.SlateMid
                            )
                        }
                    }

                    // 최근 등록 명함
                    if (state.recentCards.isNotEmpty()) {
                        item {
                            Text(
                                text = "최근 등록 명함",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = TCardlyColors.SlateDeep,
                                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                            )
                        }
                        items(state.recentCards, key = { it.id }) { card ->
                            BusinessCardItem(
                                name = card.name,
                                company = card.company,
                                position = card.position,
                                profileImageUri = card.profileImageUri,
                                onTap = { onNavigateToCardDetail(card.id) },
                                onCompanyTap = card.company?.let { { onNavigateToCompany(it) } }
                            )
                        }
                    }

                    // Pro 업그레이드 CTA (Free 사용자)
                    if (state.subscription.plan == SubscriptionPlan.FREE) {
                        item {
                            TCardlyCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                accentColor = TCardlyColors.TealDeep,
                                onClick = onNavigateToPaywall
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(20.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.Star,
                                        contentDescription = null,
                                        tint = TCardlyColors.TealDeep,
                                        modifier = Modifier.size(32.dp)
                                    )
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            "T-CARDLY Pro",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = TCardlyColors.TealDeep
                                        )
                                        Text(
                                            "광고 제거, AI 분석 30회, 무제한 내보내기",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = TCardlyColors.SlateMid
                                        )
                                    }
                                    TCardlyButton(
                                        text = "업그레이드",
                                        onClick = onNavigateToPaywall,
                                        style = TCardlyButtonStyle.PRIMARY
                                    )
                                }
                            }
                        }
                    }
                }
            }
            is HomeUiState.Empty -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("아직 등록된 명함이 없어요", style = MaterialTheme.typography.bodyLarge, color = TCardlyColors.SlateMid)
                        Spacer(modifier = Modifier.height(16.dp))
                        TCardlyButton(text = "첫 명함 스캔하기", onClick = onNavigateToScan)
                    }
                }
            }
            is HomeUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(state.message, color = TCardlyColors.CoralWarm)
                }
            }
        }
    }
}
