package com.tcardly.feature.settings.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.tcardly.core.ui.component.TagChip
import com.tcardly.core.ui.component.TCardlyCard
import com.tcardly.core.ui.component.TCardlyTextField
import com.tcardly.core.ui.util.toComposeColor
import com.tcardly.feature.settings.viewmodel.GroupTagManageViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupTagManageScreen(
    onNavigateBack: () -> Unit,
    viewModel: GroupTagManageViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = TCardlyColors.CloudBase,
        topBar = {
            TopAppBar(
                title = { Text("그룹·태그 관리") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "뒤로")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 그룹 섹션
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("그룹", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    IconButton(onClick = { viewModel.showGroupDialog() }) {
                        Icon(Icons.Default.Add, contentDescription = "그룹 추가", tint = TCardlyColors.TealDeep)
                    }
                }
            }

            items(uiState.groups, key = { it.id }) { group ->
                TCardlyCard {
                    Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(group.name, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge)
                        if (!group.isDefault) {
                            IconButton(onClick = { viewModel.deleteGroup(group.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = "삭제", tint = TCardlyColors.CoralWarm, modifier = Modifier.size(20.dp))
                            }
                        }
                    }
                }
            }

            // 태그 섹션
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("태그", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    IconButton(onClick = { viewModel.showTagDialog() }) {
                        Icon(Icons.Default.Add, contentDescription = "태그 추가", tint = TCardlyColors.TealDeep)
                    }
                }
            }

            items(uiState.tags, key = { it.id }) { tag ->
                TCardlyCard {
                    Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        TagChip(name = tag.name, bgColor = tag.bgColor.toComposeColor(), textColor = tag.textColor.toComposeColor())
                        Spacer(modifier = Modifier.weight(1f))
                        if (!tag.isDefault) {
                            IconButton(onClick = { viewModel.deleteTag(tag.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = "삭제", tint = TCardlyColors.CoralWarm, modifier = Modifier.size(20.dp))
                            }
                        }
                    }
                }
            }
        }
    }

    // 그룹 추가 다이얼로그
    if (uiState.showGroupDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.hideGroupDialog() },
            title = { Text("그룹 추가") },
            text = {
                TCardlyTextField(
                    value = uiState.editingGroupName,
                    onValueChange = { viewModel.updateGroupName(it) },
                    placeholder = "그룹 이름"
                )
            },
            confirmButton = {
                TextButton(onClick = { viewModel.createGroup() }) { Text("추가", color = TCardlyColors.TealDeep) }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.hideGroupDialog() }) { Text("취소") }
            }
        )
    }

    // 태그 추가 다이얼로그
    if (uiState.showTagDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.hideTagDialog() },
            title = { Text("태그 추가") },
            text = {
                TCardlyTextField(
                    value = uiState.editingTagName,
                    onValueChange = { viewModel.updateTagName(it) },
                    placeholder = "태그 이름"
                )
            },
            confirmButton = {
                TextButton(onClick = { viewModel.createTag() }) { Text("추가", color = TCardlyColors.TealDeep) }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.hideTagDialog() }) { Text("취소") }
            }
        )
    }
}
