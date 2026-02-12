package com.tcardly.feature.subscription.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tcardly.core.common.model.ResultWrapper
import com.tcardly.core.common.model.SubscriptionPlan
import com.tcardly.domain.repository.SubscriptionRepository
import com.tcardly.domain.repository.SubscriptionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PaywallUiState(
    val currentPlan: SubscriptionPlan = SubscriptionPlan.FREE,
    val selectedPlan: SubscriptionPlan = SubscriptionPlan.PRO,
    val isPurchasing: Boolean = false,
    val isRestoring: Boolean = false,
    val error: String? = null,
    val purchaseSuccess: Boolean = false
)

@HiltViewModel
class PaywallViewModel @Inject constructor(
    private val subscriptionRepository: SubscriptionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PaywallUiState())
    val uiState: StateFlow<PaywallUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            subscriptionRepository.observeSubscription().collect { state ->
                _uiState.update { it.copy(currentPlan = state.plan) }
            }
        }
    }

    fun selectPlan(plan: SubscriptionPlan) {
        _uiState.update { it.copy(selectedPlan = plan) }
    }

    fun resetPurchaseSuccess() {
        _uiState.update { it.copy(purchaseSuccess = false) }
    }

    fun purchase() {
        viewModelScope.launch {
            _uiState.update { it.copy(isPurchasing = true, error = null) }
            when (val result = subscriptionRepository.purchaseSubscription(_uiState.value.selectedPlan)) {
                is ResultWrapper.Success -> _uiState.update { it.copy(isPurchasing = false, purchaseSuccess = true) }
                is ResultWrapper.Error -> _uiState.update { it.copy(isPurchasing = false, error = result.message) }
                is ResultWrapper.Loading -> {}
            }
        }
    }

    fun restorePurchases() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRestoring = true, error = null) }
            when (val result = subscriptionRepository.restorePurchases()) {
                is ResultWrapper.Success -> {
                    val restoredPlan = result.data.plan
                    _uiState.update {
                        it.copy(
                            isRestoring = false,
                            purchaseSuccess = restoredPlan != SubscriptionPlan.FREE,
                            error = if (restoredPlan == SubscriptionPlan.FREE) "복원 가능한 구독이 없습니다." else null
                        )
                    }
                }
                is ResultWrapper.Error -> _uiState.update { it.copy(isRestoring = false, error = result.message) }
                is ResultWrapper.Loading -> {}
            }
        }
    }
}
