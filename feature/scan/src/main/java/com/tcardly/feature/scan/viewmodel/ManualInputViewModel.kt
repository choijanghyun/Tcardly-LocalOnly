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
import javax.inject.Inject

data class ManualInputUiState(
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
    val isSaving: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ManualInputViewModel @Inject constructor(
    private val saveBusinessCardUseCase: SaveBusinessCardUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ManualInputUiState())
    val uiState: StateFlow<ManualInputUiState> = _uiState.asStateFlow()

    fun updateField(
        name: String? = null, company: String? = null,
        position: String? = null, department: String? = null,
        mobilePhone: String? = null, officePhone: String? = null,
        email: String? = null, address: String? = null,
        website: String? = null, memo: String? = null
    ) {
        _uiState.update { s ->
            s.copy(
                name = name ?: s.name, company = company ?: s.company,
                position = position ?: s.position, department = department ?: s.department,
                mobilePhone = mobilePhone ?: s.mobilePhone, officePhone = officePhone ?: s.officePhone,
                email = email ?: s.email, address = address ?: s.address,
                website = website ?: s.website, memo = memo ?: s.memo
            )
        }
    }

    fun saveCard(onSuccess: () -> Unit) {
        val s = _uiState.value
        if (s.name.isBlank()) {
            _uiState.update { it.copy(error = "이름을 입력해 주세요.") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null) }
            val now = System.currentTimeMillis()
            val card = BusinessCard(
                name = s.name.trim(),
                company = s.company.trim().ifBlank { null },
                position = s.position.trim().ifBlank { null },
                department = s.department.trim().ifBlank { null },
                mobilePhone = s.mobilePhone.trim().ifBlank { null },
                officePhone = s.officePhone.trim().ifBlank { null },
                email = s.email.trim().ifBlank { null },
                address = s.address.trim().ifBlank { null },
                website = s.website.trim().ifBlank { null },
                memo = s.memo.trim().ifBlank { null },
                sourceType = SourceType.MANUAL,
                createdAt = now, updatedAt = now
            )
            when (val result = saveBusinessCardUseCase(card)) {
                is ResultWrapper.Success -> {
                    _uiState.update { it.copy(isSaving = false) }
                    onSuccess()
                }
                is ResultWrapper.Error -> _uiState.update { it.copy(isSaving = false, error = result.message) }
                is ResultWrapper.Loading -> {}
            }
        }
    }
}
