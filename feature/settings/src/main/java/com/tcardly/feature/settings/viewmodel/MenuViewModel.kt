package com.tcardly.feature.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tcardly.core.common.model.SubscriptionPlan
import com.tcardly.domain.model.UserProfile
import com.tcardly.domain.repository.CardRepository
import com.tcardly.domain.repository.ProfileRepository
import com.tcardly.domain.repository.SubscriptionRepository
import com.tcardly.domain.repository.SubscriptionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MenuUiState(
    val userName: String = "",
    val userCompany: String = "",
    val plan: SubscriptionPlan = SubscriptionPlan.FREE,
    val totalCardCount: Int = 0,
    val isLoading: Boolean = true
)

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val subscriptionRepository: SubscriptionRepository,
    private val cardRepository: CardRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MenuUiState())
    val uiState: StateFlow<MenuUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                profileRepository.observeProfile(),
                subscriptionRepository.observeSubscription(),
                cardRepository.getCardCount()
            ) { profile, sub, count ->
                MenuUiState(
                    userName = profile?.name ?: "사용자",
                    userCompany = profile?.currentCompany ?: "",
                    plan = sub.plan,
                    totalCardCount = count,
                    isLoading = false
                )
            }.collect { _uiState.value = it }
        }
    }
}
