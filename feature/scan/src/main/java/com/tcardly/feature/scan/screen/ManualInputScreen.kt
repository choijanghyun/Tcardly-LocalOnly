package com.tcardly.feature.scan.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tcardly.core.designsystem.theme.TCardlyColors
import com.tcardly.core.ui.component.TCardlyButton
import com.tcardly.core.ui.component.TCardlyCard
import com.tcardly.core.ui.component.TCardlyTextField
import com.tcardly.feature.scan.viewmodel.ManualInputViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManualInputScreen(
    onNavigateToHome: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: ManualInputViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = TCardlyColors.CloudBase,
        topBar = {
            TopAppBar(
                title = { Text("명함 수기 입력") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "뒤로")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            TCardlyCard {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("기본 정보", style = MaterialTheme.typography.titleMedium, color = TCardlyColors.SlateDeep)
                    TCardlyTextField(
                        value = uiState.name,
                        onValueChange = { viewModel.updateField(name = it) },
                        placeholder = "이름 *",
                        leadingIcon = Icons.Default.Person
                    )
                    TCardlyTextField(
                        value = uiState.company,
                        onValueChange = { viewModel.updateField(company = it) },
                        placeholder = "회사명",
                        leadingIcon = Icons.Default.Business
                    )
                    TCardlyTextField(
                        value = uiState.position,
                        onValueChange = { viewModel.updateField(position = it) },
                        placeholder = "직책",
                        leadingIcon = Icons.Default.Badge
                    )
                    TCardlyTextField(
                        value = uiState.department,
                        onValueChange = { viewModel.updateField(department = it) },
                        placeholder = "부서",
                        leadingIcon = Icons.Default.Groups
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TCardlyCard {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("연락처", style = MaterialTheme.typography.titleMedium, color = TCardlyColors.SlateDeep)
                    TCardlyTextField(
                        value = uiState.mobilePhone,
                        onValueChange = { viewModel.updateField(mobilePhone = it) },
                        placeholder = "휴대전화",
                        leadingIcon = Icons.Default.Phone
                    )
                    TCardlyTextField(
                        value = uiState.officePhone,
                        onValueChange = { viewModel.updateField(officePhone = it) },
                        placeholder = "사무실 전화",
                        leadingIcon = Icons.Default.Phone
                    )
                    TCardlyTextField(
                        value = uiState.email,
                        onValueChange = { viewModel.updateField(email = it) },
                        placeholder = "이메일",
                        leadingIcon = Icons.Default.Email
                    )
                    TCardlyTextField(
                        value = uiState.address,
                        onValueChange = { viewModel.updateField(address = it) },
                        placeholder = "주소",
                        leadingIcon = Icons.Default.LocationOn
                    )
                    TCardlyTextField(
                        value = uiState.website,
                        onValueChange = { viewModel.updateField(website = it) },
                        placeholder = "웹사이트",
                        leadingIcon = Icons.Default.Language
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TCardlyCard {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("메모", style = MaterialTheme.typography.titleMedium, color = TCardlyColors.SlateDeep)
                    Spacer(modifier = Modifier.height(8.dp))
                    TCardlyTextField(
                        value = uiState.memo,
                        onValueChange = { viewModel.updateField(memo = it) },
                        placeholder = "메모를 입력해 주세요",
                        leadingIcon = Icons.Default.Notes
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            TCardlyButton(
                text = if (uiState.isSaving) "저장 중..." else "명함 저장",
                onClick = { viewModel.saveCard { onNavigateToHome() } },
                enabled = uiState.name.isNotBlank() && !uiState.isSaving,
                modifier = Modifier.fillMaxWidth()
            )

            uiState.error?.let { error ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(error, color = TCardlyColors.CoralWarm, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
