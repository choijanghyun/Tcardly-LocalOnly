package com.tcardly.feature.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tcardly.core.common.model.ResultWrapper
import com.tcardly.domain.model.UserProfile
import com.tcardly.domain.usecase.profile.CreateLocalProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class InitialProfileUiState(
    val name: String = "",
    val company: String = "",
    val position: String = "",
    val phone: String = "",
    val email: String = "",
    val isSaving: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class InitialProfileViewModel @Inject constructor(
    private val createLocalProfileUseCase: CreateLocalProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(InitialProfileUiState())
    val uiState: StateFlow<InitialProfileUiState> = _uiState.asStateFlow()

    fun updateField(
        name: String? = null, company: String? = null,
        position: String? = null, phone: String? = null, email: String? = null
    ) {
        _uiState.update { s ->
            s.copy(
                name = name ?: s.name, company = company ?: s.company,
                position = position ?: s.position, phone = phone ?: s.phone,
                email = email ?: s.email
            )
        }
    }

    fun createProfile(onSuccess: () -> Unit) {
        val s = _uiState.value
        if (s.name.isBlank()) {
            _uiState.update { it.copy(error = "이름을 입력해 주세요.") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null) }
            val profile = UserProfile(
                name = s.name.trim(),
                currentCompany = s.company.trim().ifBlank { null },
                currentPosition = s.position.trim().ifBlank { null },
                mobilePhone = s.phone.trim().ifBlank { null },
                email = s.email.trim().ifBlank { null }
            )
            when (createLocalProfileUseCase(profile)) {
                is ResultWrapper.Success -> {
                    _uiState.update { it.copy(isSaving = false) }
                    onSuccess()
                }
                is ResultWrapper.Error -> {
                    _uiState.update { it.copy(isSaving = false, error = "프로필 생성에 실패했습니다.") }
                }
                is ResultWrapper.Loading -> {}
            }
        }
    }
}
