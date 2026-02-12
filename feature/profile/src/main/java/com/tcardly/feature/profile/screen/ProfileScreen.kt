package com.tcardly.feature.profile.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import com.tcardly.core.ui.component.TCardlyButton
import com.tcardly.core.ui.component.TCardlyCard
import com.tcardly.core.ui.component.TCardlyTextField
import com.tcardly.feature.profile.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = TCardlyColors.CloudBase,
        topBar = {
            TopAppBar(
                title = { Text("내 프로필") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "뒤로")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        if (uiState.isEditing) viewModel.saveProfile() else viewModel.toggleEditing()
                    }) {
                        Icon(
                            imageVector = if (uiState.isEditing) Icons.Default.Check else Icons.Default.Edit,
                            contentDescription = if (uiState.isEditing) "저장" else "편집",
                            tint = TCardlyColors.TealDeep
                        )
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
                // 프로필 헤더
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier.size(96.dp).clip(CircleShape).background(TCardlyColors.TealSoft),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(uiState.name.take(1).ifBlank { "?" },
                                style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold,
                                color = TCardlyColors.TealDeep)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        if (!uiState.isEditing) {
                            Text(uiState.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                            if (uiState.currentPosition.isNotBlank() || uiState.currentCompany.isNotBlank()) {
                                Text(
                                    listOfNotNull(
                                        uiState.currentPosition.ifBlank { null },
                                        uiState.currentCompany.ifBlank { null }
                                    ).joinToString(" · "),
                                    color = TCardlyColors.SlateMid
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (uiState.isEditing) {
                    TCardlyCard {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            TCardlyTextField(value = uiState.name, onValueChange = { viewModel.updateField(name = it) },
                                placeholder = "이름 *", leadingIcon = Icons.Default.Person)
                            TCardlyTextField(value = uiState.currentCompany, onValueChange = { viewModel.updateField(company = it) },
                                placeholder = "현재 회사", leadingIcon = Icons.Default.Business)
                            TCardlyTextField(value = uiState.currentPosition, onValueChange = { viewModel.updateField(position = it) },
                                placeholder = "현재 직책", leadingIcon = Icons.Default.Badge)
                            TCardlyTextField(value = uiState.mobilePhone, onValueChange = { viewModel.updateField(phone = it) },
                                placeholder = "휴대전화", leadingIcon = Icons.Default.Phone)
                            TCardlyTextField(value = uiState.email, onValueChange = { viewModel.updateField(email = it) },
                                placeholder = "이메일", leadingIcon = Icons.Default.Email)
                        }
                    }
                    uiState.error?.let {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(it, color = TCardlyColors.CoralWarm, style = MaterialTheme.typography.bodySmall)
                    }
                } else {
                    TCardlyCard {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("연락처", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.height(8.dp))
                            if (uiState.mobilePhone.isNotBlank()) ProfileInfoRow("휴대전화", uiState.mobilePhone)
                            if (uiState.email.isNotBlank()) ProfileInfoRow("이메일", uiState.email)
                        }
                    }

                    if (uiState.careers.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        TCardlyCard {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("경력", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                                Spacer(modifier = Modifier.height(8.dp))
                                uiState.careers.forEach { career ->
                                    Text("${career.company} · ${career.position}", fontWeight = FontWeight.Medium)
                                    Text("${career.startDate} ~ ${career.endDate ?: "현재"}", color = TCardlyColors.SlateMid,
                                        style = MaterialTheme.typography.bodySmall)
                                    Spacer(modifier = Modifier.height(4.dp))
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun ProfileInfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = TCardlyColors.SlateMid, modifier = Modifier.width(80.dp))
        Text(value, style = MaterialTheme.typography.bodyMedium, color = TCardlyColors.SlateDeep)
    }
}
