package com.tcardly.feature.subscription.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tcardly.core.common.model.SubscriptionPlan
import com.tcardly.core.common.model.SubscriptionStatus
import com.tcardly.domain.repository.SubscriptionRepository
import com.tcardly.domain.repository.SubscriptionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SubscriptionManageUiState(
    val subscriptionState: SubscriptionState = SubscriptionState(),
    val isLoading: Boolean = true
)

@HiltViewModel
class SubscriptionManageViewModel @Inject constructor(
    private val subscriptionRepository: SubscriptionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SubscriptionManageUiState())
    val uiState: StateFlow<SubscriptionManageUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            subscriptionRepository.observeSubscription().collect { state ->
                _uiState.update { it.copy(subscriptionState = state, isLoading = false) }
            }
        }
    }

    fun restorePurchases() {
        viewModelScope.launch {
            subscriptionRepository.restorePurchases()
        }
    }
}
