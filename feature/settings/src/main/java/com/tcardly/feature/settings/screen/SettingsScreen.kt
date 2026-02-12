package com.tcardly.feature.settings.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.tcardly.core.designsystem.theme.TCardlyColors
import com.tcardly.core.ui.component.TCardlyCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit
) {
    var darkMode by remember { mutableStateOf(false) }
    var notifications by remember { mutableStateOf(true) }

    Scaffold(
        containerColor = TCardlyColors.CloudBase,
        topBar = {
            TopAppBar(
                title = { Text("앱 설정") },
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
            // 일반 설정
            Text("일반", style = MaterialTheme.typography.labelMedium, color = TCardlyColors.SlateMid,
                modifier = Modifier.padding(start = 4.dp, bottom = 8.dp))
            TCardlyCard {
                Column {
                    SettingsToggleItem(
                        icon = Icons.Default.DarkMode,
                        title = "다크 모드",
                        checked = darkMode,
                        onCheckedChange = { darkMode = it }
                    )
                    HorizontalDivider(color = TCardlyColors.BorderSoft)
                    SettingsToggleItem(
                        icon = Icons.Default.Notifications,
                        title = "알림",
                        checked = notifications,
                        onCheckedChange = { notifications = it }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 데이터 관리
            Text("데이터", style = MaterialTheme.typography.labelMedium, color = TCardlyColors.SlateMid,
                modifier = Modifier.padding(start = 4.dp, bottom = 8.dp))
            TCardlyCard {
                Column {
                    SettingsClickItem(icon = Icons.Default.CloudUpload, title = "데이터 백업") {
                        // TODO: 백업 기능
                    }
                    HorizontalDivider(color = TCardlyColors.BorderSoft)
                    SettingsClickItem(icon = Icons.Default.CloudDownload, title = "데이터 복원") {
                        // TODO: 복원 기능
                    }
                    HorizontalDivider(color = TCardlyColors.BorderSoft)
                    SettingsClickItem(icon = Icons.Default.FileDownload, title = "전체 명함 내보내기 (CSV)") {
                        // TODO: CSV 내보내기
                    }
                    HorizontalDivider(color = TCardlyColors.BorderSoft)
                    SettingsClickItem(icon = Icons.Default.ContactPage, title = "전체 명함 내보내기 (vCard)") {
                        // TODO: vCard 내보내기
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 정보
            Text("정보", style = MaterialTheme.typography.labelMedium, color = TCardlyColors.SlateMid,
                modifier = Modifier.padding(start = 4.dp, bottom = 8.dp))
            TCardlyCard {
                Column {
                    SettingsClickItem(icon = Icons.Default.Info, title = "앱 버전", subtitle = "5.0.0") {}
                    HorizontalDivider(color = TCardlyColors.BorderSoft)
                    SettingsClickItem(icon = Icons.Default.Description, title = "이용약관") {}
                    HorizontalDivider(color = TCardlyColors.BorderSoft)
                    SettingsClickItem(icon = Icons.Default.PrivacyTip, title = "개인정보 처리방침") {}
                    HorizontalDivider(color = TCardlyColors.BorderSoft)
                    SettingsClickItem(icon = Icons.Default.Code, title = "오픈소스 라이선스") {}
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SettingsToggleItem(
    icon: ImageVector,
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = title, tint = TCardlyColors.TealDeep, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(title, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(checkedTrackColor = TCardlyColors.TealDeep)
        )
    }
}

@Composable
private fun SettingsClickItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = title, tint = TCardlyColors.TealDeep, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(title, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
        subtitle?.let {
            Text(it, style = MaterialTheme.typography.bodyMedium, color = TCardlyColors.SlateMid)
        }
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TCardlyColors.SlateLight, modifier = Modifier.size(20.dp))
    }
}
