package com.tcardly.feature.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tcardly.core.common.model.SubscriptionPlan
import com.tcardly.core.common.util.DateUtils
import com.tcardly.domain.model.BusinessCard
import com.tcardly.domain.model.UserProfile
import com.tcardly.domain.repository.CardRepository
import com.tcardly.domain.repository.ProfileRepository
import com.tcardly.domain.repository.SubscriptionRepository
import com.tcardly.domain.repository.SubscriptionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(
        val profile: UserProfile?,
        val greeting: String,
        val cardCount: Int,
        val recentCards: List<BusinessCard>,
        val subscription: SubscriptionState
    ) : HomeUiState
    data object Empty : HomeUiState
    data class Error(val message: String) : HomeUiState
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val cardRepository: CardRepository,
    private val profileRepository: ProfileRepository,
    private val subscriptionRepository: SubscriptionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            try {
                combine(
                    profileRepository.observeProfile(),
                    cardRepository.getRecentCards(5),
                    cardRepository.getCardCount(),
                    subscriptionRepository.observeSubscription()
                ) { profile, recentCards, cardCount, subscription ->
                    if (cardCount == 0) {
                        HomeUiState.Empty
                    } else {
                        HomeUiState.Success(
                            profile = profile,
                            greeting = "${DateUtils.getGreeting()}, ${profile?.name ?: "사용자"}님",
                            cardCount = cardCount,
                            recentCards = recentCards,
                            subscription = subscription
                        )
                    }
                }.collect { state ->
                    _uiState.value = state
                }
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e.message ?: "데이터 로드 실패")
            }
        }
    }
}
