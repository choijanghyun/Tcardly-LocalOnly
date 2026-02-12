package com.tcardly.feature.scan.screen

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tcardly.core.designsystem.theme.TCardlyColors
import com.tcardly.feature.scan.viewmodel.ScanEvent
import com.tcardly.feature.scan.viewmodel.ScanMode
import com.tcardly.feature.scan.viewmodel.ScanViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanScreen(
    onNavigateToScanResult: (String) -> Unit,
    onNavigateToManualInput: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: ScanViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // 갤러리 런처
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.onGalleryImageSelected(it) }
    }

    // 이벤트 핸들링
    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is ScanEvent.NavigateToResult -> onNavigateToScanResult(event.ocrJson)
                is ScanEvent.ShowError -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    Scaffold(
        containerColor = Color.Black,
        topBar = {
            TopAppBar(
                title = { Text("명함 스캔", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "뒤로", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToManualInput) {
                        Icon(Icons.Default.Edit, contentDescription = "수기입력", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // 카메라 프리뷰 영역 (실제 CameraX 프리뷰 대체)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.DarkGray),
                contentAlignment = Alignment.Center
            ) {
                if (uiState.isProcessing) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = TCardlyColors.TealMid)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("명함을 인식하고 있습니다...", color = Color.White)
                    }
                } else {
                    // 가이드 프레임
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .width(300.dp)
                                .height(180.dp)
                                .background(Color.Transparent)
                        ) {
                            // 모서리 가이드라인
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = when (uiState.scanMode) {
                                ScanMode.CAMERA -> "명함을 프레임 안에 맞춰주세요"
                                ScanMode.QR -> "QR 코드를 스캔해 주세요"
                                ScanMode.GALLERY -> "갤러리에서 이미지를 선택해 주세요"
                            },
                            color = Color.White.copy(alpha = 0.8f),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // 에러 표시
                uiState.error?.let { error ->
                    Snackbar(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(16.dp),
                        action = {
                            TextButton(onClick = { viewModel.clearError() }) {
                                Text("확인", color = TCardlyColors.TealMid)
                            }
                        }
                    ) {
                        Text(error)
                    }
                }
            }

            // 하단 컨트롤
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 모드 선택 탭
                Row(
                    modifier = Modifier
                        .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(24.dp))
                        .padding(horizontal = 4.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    ScanModeChip("촬영", ScanMode.CAMERA, uiState.scanMode) {
                        viewModel.changeScanMode(ScanMode.CAMERA)
                    }
                    ScanModeChip("QR", ScanMode.QR, uiState.scanMode) {
                        viewModel.changeScanMode(ScanMode.QR)
                    }
                    ScanModeChip("갤러리", ScanMode.GALLERY, uiState.scanMode) {
                        viewModel.changeScanMode(ScanMode.GALLERY)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 캡처/갤러리 버튼
                Row(
                    horizontalArrangement = Arrangement.spacedBy(32.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 플래시 토글
                    if (uiState.scanMode == ScanMode.CAMERA) {
                        IconButton(onClick = { viewModel.toggleFlash() }) {
                            Icon(
                                imageVector = if (uiState.flashEnabled) Icons.Default.FlashOn
                                else Icons.Default.FlashOff,
                                contentDescription = "플래시",
                                tint = Color.White
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.size(48.dp))
                    }

                    // 셔터 버튼
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(TCardlyColors.TealDeep)
                            .clickable {
                                when (uiState.scanMode) {
                                    ScanMode.CAMERA -> {
                                        // TODO: CameraX 캡처 트리거
                                    }
                                    ScanMode.QR -> {
                                        // QR 모드는 자동 스캔
                                    }
                                    ScanMode.GALLERY -> {
                                        galleryLauncher.launch("image/*")
                                    }
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when (uiState.scanMode) {
                                ScanMode.CAMERA -> Icons.Default.CameraAlt
                                ScanMode.QR -> Icons.Default.QrCodeScanner
                                ScanMode.GALLERY -> Icons.Default.PhotoLibrary
                            },
                            contentDescription = "스캔",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.size(48.dp))
                }
            }
        }
    }
}

@Composable
private fun ScanModeChip(
    label: String,
    mode: ScanMode,
    currentMode: ScanMode,
    onClick: () -> Unit
) {
    val selected = mode == currentMode
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = if (selected) TCardlyColors.TealDeep else Color.Transparent,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Text(
            text = label,
            color = if (selected) Color.White else Color.White.copy(alpha = 0.6f),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.labelMedium
        )
    }
}
