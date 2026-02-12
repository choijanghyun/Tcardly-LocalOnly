package com.tcardly.feature.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tcardly.core.common.model.ResultWrapper
import com.tcardly.domain.model.Career
import com.tcardly.domain.model.Education
import com.tcardly.domain.model.UserProfile
import com.tcardly.domain.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val name: String = "",
    val currentCompany: String = "",
    val currentPosition: String = "",
    val mobilePhone: String = "",
    val email: String = "",
    val careers: List<Career> = emptyList(),
    val educations: List<Education> = emptyList(),
    val interests: List<String> = emptyList(),
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val isEditing: Boolean = false,
    val error: String? = null,
    val saveSuccess: Boolean = false
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init { loadProfile() }

    private fun loadProfile() {
        viewModelScope.launch {
            profileRepository.observeProfile().collect { profile ->
                if (profile != null) {
                    _uiState.update {
                        it.copy(
                            name = profile.name,
                            currentCompany = profile.currentCompany ?: "",
                            currentPosition = profile.currentPosition ?: "",
                            mobilePhone = profile.mobilePhone ?: "",
                            email = profile.email ?: "",
                            careers = profile.careers,
                            educations = profile.educations,
                            interests = profile.interests,
                            isLoading = false
                        )
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    fun toggleEditing() { _uiState.update { it.copy(isEditing = !it.isEditing) } }

    fun updateField(
        name: String? = null, company: String? = null, position: String? = null,
        phone: String? = null, email: String? = null
    ) {
        _uiState.update { s ->
            s.copy(
                name = name ?: s.name, currentCompany = company ?: s.currentCompany,
                currentPosition = position ?: s.currentPosition,
                mobilePhone = phone ?: s.mobilePhone, email = email ?: s.email
            )
        }
    }

    fun saveProfile() {
        val s = _uiState.value
        if (s.name.isBlank()) {
            _uiState.update { it.copy(error = "이름을 입력해 주세요.") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null) }
            val profile = UserProfile(
                name = s.name.trim(),
                currentCompany = s.currentCompany.trim().ifBlank { null },
                currentPosition = s.currentPosition.trim().ifBlank { null },
                mobilePhone = s.mobilePhone.trim().ifBlank { null },
                email = s.email.trim().ifBlank { null },
                careers = s.careers, educations = s.educations, interests = s.interests
            )
            when (profileRepository.updateProfile(profile)) {
                is ResultWrapper.Success -> _uiState.update { it.copy(isSaving = false, isEditing = false, saveSuccess = true) }
                is ResultWrapper.Error -> _uiState.update { it.copy(isSaving = false, error = "프로필 저장에 실패했습니다.") }
                is ResultWrapper.Loading -> {}
            }
        }
    }
}
