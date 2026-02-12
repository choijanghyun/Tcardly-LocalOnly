package com.tcardly.feature.scan.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tcardly.core.common.model.ResultWrapper
import com.tcardly.core.common.model.SourceType
import com.tcardly.domain.model.BusinessCard
import com.tcardly.domain.usecase.card.SaveBusinessCardUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

data class ScanResultUiState(
    val name: String = "",
    val company: String = "",
    val position: String = "",
    val department: String = "",
    val mobilePhone: String = "",
    val officePhone: String = "",
    val email: String = "",
    val address: String = "",
    val website: String = "",
    val memo: String = "",
    val confidence: Float = 0f,
    val cardImageUri: String? = null,
    val sourceType: SourceType = SourceType.CAMERA_OCR,
    val isSaving: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ScanResultViewModel @Inject constructor(
    private val saveBusinessCardUseCase: SaveBusinessCardUseCase
) : ViewModel() {

    private val stringRegex = "\"(\\w+)\"\\s*:\\s*\"((?:[^\"\\\\]|\\\\.)*)\"".toRegex()
    private val numberRegex = "\"(\\w+)\"\\s*:\\s*([\\d.]+)".toRegex()

    private val _uiState = MutableStateFlow(ScanResultUiState())
    val uiState: StateFlow<ScanResultUiState> = _uiState.asStateFlow()

    fun parseOcrResult(json: String) {
        try {
            // 간단한 JSON 파싱 (Gson 없이)
            val map = parseSimpleJson(json)
            _uiState.update {
                it.copy(
                    name = map["name"] ?: "",
                    company = map["company"] ?: "",
                    position = map["position"] ?: "",
                    department = map["department"] ?: "",
                    mobilePhone = map["mobilePhone"] ?: "",
                    officePhone = map["officePhone"] ?: "",
                    email = map["email"] ?: "",
                    address = map["address"] ?: "",
                    website = map["website"] ?: "",
                    confidence = map["confidence"]?.toFloatOrNull() ?: 0f,
                    cardImageUri = map["cardImageUri"]?.takeIf { uri -> uri.isNotBlank() }
                )
            }
        } catch (e: Exception) {
            Timber.e(e, "OCR 결과 파싱 실패")
            _uiState.update { it.copy(error = "OCR 결과를 파싱할 수 없습니다.") }
        }
    }

    fun updateField(
        name: String? = null,
        company: String? = null,
        position: String? = null,
        department: String? = null,
        mobilePhone: String? = null,
        officePhone: String? = null,
        email: String? = null,
        address: String? = null,
        website: String? = null,
        memo: String? = null
    ) {
        _uiState.update { state ->
            state.copy(
                name = name ?: state.name,
                company = company ?: state.company,
                position = position ?: state.position,
                department = department ?: state.department,
                mobilePhone = mobilePhone ?: state.mobilePhone,
                officePhone = officePhone ?: state.officePhone,
                email = email ?: state.email,
                address = address ?: state.address,
                website = website ?: state.website,
                memo = memo ?: state.memo
            )
        }
    }

    fun saveCard(onSuccess: () -> Unit) {
        val state = _uiState.value
        if (state.name.isBlank()) {
            _uiState.update { it.copy(error = "이름을 입력해 주세요.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null) }
            val now = System.currentTimeMillis()
            val card = BusinessCard(
                name = state.name.trim(),
                company = state.company.trim().ifBlank { null },
                position = state.position.trim().ifBlank { null },
                department = state.department.trim().ifBlank { null },
                mobilePhone = state.mobilePhone.trim().ifBlank { null },
                officePhone = state.officePhone.trim().ifBlank { null },
                email = state.email.trim().ifBlank { null },
                address = state.address.trim().ifBlank { null },
                website = state.website.trim().ifBlank { null },
                memo = state.memo.trim().ifBlank { null },
                cardImageUri = state.cardImageUri,
                sourceType = state.sourceType,
                ocrConfidence = state.confidence,
                createdAt = now,
                updatedAt = now
            )

            when (val result = saveBusinessCardUseCase(card)) {
                is ResultWrapper.Success -> {
                    _uiState.update { it.copy(isSaving = false) }
                    onSuccess()
                }
                is ResultWrapper.Error -> {
                    _uiState.update { it.copy(isSaving = false, error = result.message) }
                }
                is ResultWrapper.Loading -> {}
            }
        }
    }

    private fun parseSimpleJson(json: String): Map<String, String> {
        val map = mutableMapOf<String, String>()
        val content = json.trim().removePrefix("{").removeSuffix("}")
        stringRegex.findAll(content).forEach { match ->
            val key = match.groupValues[1]
            val value = match.groupValues[2]
                .replace("\\n", "\n").replace("\\r", "\r")
                .replace("\\t", "\t").replace("\\\"", "\"")
                .replace("\\\\", "\\")
            map[key] = value
        }
        // confidence (숫자)
        numberRegex.findAll(content).forEach { match ->
            val key = match.groupValues[1]
            if (key !in map) map[key] = match.groupValues[2]
        }
        return map
    }
}
