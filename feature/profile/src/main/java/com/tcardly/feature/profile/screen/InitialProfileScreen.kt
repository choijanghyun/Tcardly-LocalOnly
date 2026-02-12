package com.tcardly.feature.profile.screen

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tcardly.core.designsystem.theme.TCardlyColors
import com.tcardly.core.ui.component.TCardlyButton
import com.tcardly.core.ui.component.TCardlyCard
import com.tcardly.core.ui.component.TCardlyTextField
import com.tcardly.feature.profile.viewmodel.InitialProfileViewModel

@Composable
fun InitialProfileScreen(
    onNavigateToHome: () -> Unit,
    viewModel: InitialProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        Icon(Icons.Default.Person, contentDescription = null, tint = TCardlyColors.TealDeep,
            modifier = Modifier.size(64.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Text("프로필 설정", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text("기본 정보를 입력하면\n명함 교환이 더 편리해집니다.",
            color = TCardlyColors.SlateMid, textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(32.dp))

        TCardlyCard {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                TCardlyTextField(value = uiState.name, onValueChange = { viewModel.updateField(name = it) },
                    placeholder = "이름 *", leadingIcon = Icons.Default.Person)
                TCardlyTextField(value = uiState.company, onValueChange = { viewModel.updateField(company = it) },
                    placeholder = "현재 회사", leadingIcon = Icons.Default.Business)
                TCardlyTextField(value = uiState.position, onValueChange = { viewModel.updateField(position = it) },
                    placeholder = "직책", leadingIcon = Icons.Default.Badge)
                TCardlyTextField(value = uiState.phone, onValueChange = { viewModel.updateField(phone = it) },
                    placeholder = "휴대전화", leadingIcon = Icons.Default.Phone)
                TCardlyTextField(value = uiState.email, onValueChange = { viewModel.updateField(email = it) },
                    placeholder = "이메일", leadingIcon = Icons.Default.Email)
            }
        }

        uiState.error?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(it, color = TCardlyColors.CoralWarm, style = MaterialTheme.typography.bodySmall)
        }

        Spacer(modifier = Modifier.height(24.dp))

        TCardlyButton(
            text = if (uiState.isSaving) "저장 중..." else "시작하기",
            onClick = { viewModel.createProfile { onNavigateToHome() } },
            enabled = uiState.name.isNotBlank() && !uiState.isSaving,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(onClick = onNavigateToHome) {
            Text("나중에 설정할게요", color = TCardlyColors.SlateMid)
        }
    }
}
