package com.tcardly.feature.scan.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tcardly.domain.model.OcrResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class ScanMode { CAMERA, QR, GALLERY }

data class ScanUiState(
    val scanMode: ScanMode = ScanMode.CAMERA,
    val isProcessing: Boolean = false,
    val flashEnabled: Boolean = false,
    val capturedImageUri: Uri? = null,
    val error: String? = null
)

sealed interface ScanEvent {
    data class NavigateToResult(val ocrJson: String) : ScanEvent
    data class ShowError(val message: String) : ScanEvent
}

@HiltViewModel
class ScanViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(ScanUiState())
    val uiState: StateFlow<ScanUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<ScanEvent>()
    val events = _events.asSharedFlow()

    fun changeScanMode(mode: ScanMode) {
        _uiState.update { it.copy(scanMode = mode, error = null) }
    }

    fun toggleFlash() {
        _uiState.update { it.copy(flashEnabled = !it.flashEnabled) }
    }

    fun onImageCaptured(imageUri: Uri) {
        _uiState.update { it.copy(capturedImageUri = imageUri, isProcessing = true) }
    }

    fun onGalleryImageSelected(imageUri: Uri) {
        _uiState.update {
            it.copy(
                capturedImageUri = imageUri,
                isProcessing = true,
                scanMode = ScanMode.GALLERY
            )
        }
    }

    fun onOcrCompleted(ocrResult: OcrResult) {
        viewModelScope.launch {
            _uiState.update { it.copy(isProcessing = false) }
            val json = ocrResultToJson(ocrResult)
            _events.emit(ScanEvent.NavigateToResult(json))
        }
    }

    fun onQrScanned(vCardString: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isProcessing = true) }
            val ocrResult = parseVCard(vCardString)
            _uiState.update { it.copy(isProcessing = false) }
            val json = ocrResultToJson(ocrResult)
            _events.emit(ScanEvent.NavigateToResult(json))
        }
    }

    fun onOcrError(error: String) {
        _uiState.update { it.copy(isProcessing = false, error = error) }
        viewModelScope.launch {
            _events.emit(ScanEvent.ShowError(error))
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    /**
     * OCR 결과를 JSON 문자열로 변환 (Navigation 전달용)
     */
    private fun ocrResultToJson(result: OcrResult): String {
        return buildString {
            append("{")
            append("\"name\":\"${result.name?.escapeJson() ?: ""}\",")
            append("\"company\":\"${result.company?.escapeJson() ?: ""}\",")
            append("\"position\":\"${result.position?.escapeJson() ?: ""}\",")
            append("\"department\":\"${result.department?.escapeJson() ?: ""}\",")
            append("\"mobilePhone\":\"${result.mobilePhone ?: ""}\",")
            append("\"officePhone\":\"${result.officePhone ?: ""}\",")
            append("\"email\":\"${result.email ?: ""}\",")
            append("\"address\":\"${result.address?.escapeJson() ?: ""}\",")
            append("\"website\":\"${result.website ?: ""}\",")
            append("\"confidence\":${result.confidence},")
            append("\"cardImageUri\":\"${result.cardImageUri ?: ""}\",")
            append("\"rawText\":\"${result.rawText.escapeJson()}\"")
            append("}")
        }
    }

    /**
     * vCard 문자열 파싱 → OcrResult
     */
    private fun parseVCard(vCard: String): OcrResult {
        val lines = vCard.lines()
        var name = ""
        var company = ""
        var position = ""
        var phone = ""
        var email = ""
        var address = ""
        var website = ""

        for (line in lines) {
            when {
                line.startsWith("FN:") -> name = line.substringAfter("FN:")
                line.startsWith("ORG:") -> company = line.substringAfter("ORG:").trimEnd(';')
                line.startsWith("TITLE:") -> position = line.substringAfter("TITLE:")
                line.startsWith("TEL;") || line.startsWith("TEL:") ->
                    phone = line.substringAfter(":").replace("-", "").replace(" ", "")
                line.startsWith("EMAIL") -> email = line.substringAfter(":")
                line.startsWith("ADR") -> address = line.substringAfter(":")
                    .replace(";", " ").trim()
                line.startsWith("URL:") -> website = line.substringAfter("URL:")
            }
        }

        return OcrResult(
            name = name.ifBlank { null },
            company = company.ifBlank { null },
            position = position.ifBlank { null },
            mobilePhone = phone.ifBlank { null },
            email = email.ifBlank { null },
            address = address.ifBlank { null },
            website = website.ifBlank { null },
            confidence = 0.95f,
            rawText = vCard
        )
    }

    private fun String.escapeJson(): String =
        replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t")
}
