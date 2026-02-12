package com.tcardly.feature.contacts.screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tcardly.core.designsystem.theme.TCardlyColors
import com.tcardly.core.ui.component.BusinessCardItem
import com.tcardly.core.ui.component.TCardlyTextField
import com.tcardly.feature.contacts.viewmodel.ContactListUiState
import com.tcardly.feature.contacts.viewmodel.ContactListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactListScreen(
    onNavigateToCardDetail: (Long) -> Unit,
    onNavigateToCompany: (String) -> Unit,
    onNavigateToScan: () -> Unit,
    onNavigateToManualInput: () -> Unit,
    viewModel: ContactListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddSheet by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = TCardlyColors.CloudBase,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddSheet = true },
                containerColor = TCardlyColors.TealDeep,
                contentColor = TCardlyColors.PureWhite
            ) {
                Icon(Icons.Default.Add, contentDescription = "추가")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // 검색바
            var searchQuery by remember { mutableStateOf("") }
            TCardlyTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    viewModel.onSearchQueryChanged(it)
                },
                placeholder = "이름, 회사, 전화번호 검색",
                leadingIcon = Icons.Default.Search,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            when (val state = uiState) {
                is ContactListUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = TCardlyColors.TealDeep)
                    }
                }
                is ContactListUiState.Success -> {
                    // 태그 필터 칩
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.tags) { tag ->
                            FilterChip(
                                selected = state.selectedTagId == tag.id,
                                onClick = {
                                    viewModel.onTagSelected(
                                        if (state.selectedTagId == tag.id) null else tag.id
                                    )
                                },
                                label = { Text(tag.name) },
                                shape = RoundedCornerShape(20.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // 카드 목록
                    LazyColumn {
                        items(state.cards, key = { it.id }) { card ->
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
                }
                is ContactListUiState.Empty -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("등록된 명함이 없습니다", color = TCardlyColors.SlateMid)
                    }
                }
                is ContactListUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(state.message, color = TCardlyColors.CoralWarm)
                    }
                }
            }
        }
    }

    // 추가 바텀시트
    if (showAddSheet) {
        ModalBottomSheet(onDismissRequest = { showAddSheet = false }) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("명함 추가", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))
                ListItem(
                    headlineContent = { Text("스캔으로 추가") },
                    leadingContent = { Icon(Icons.Default.CameraAlt, contentDescription = null, tint = TCardlyColors.TealDeep) },
                    modifier = Modifier.fillMaxWidth().background(TCardlyColors.PureWhite, RoundedCornerShape(12.dp))
                        .padding(4.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                ListItem(
                    headlineContent = { Text("수기 입력") },
                    leadingContent = { Icon(Icons.Default.Edit, contentDescription = null, tint = TCardlyColors.TealDeep) },
                    modifier = Modifier.fillMaxWidth().background(TCardlyColors.PureWhite, RoundedCornerShape(12.dp))
                        .padding(4.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
