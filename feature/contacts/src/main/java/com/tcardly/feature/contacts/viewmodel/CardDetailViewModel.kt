package com.tcardly.feature.contacts.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tcardly.domain.model.Tag
import com.tcardly.domain.usecase.card.GetCardDetailUseCase
import com.tcardly.domain.usecase.card.ToggleFavoriteUseCase
import com.tcardly.domain.usecase.card.ShareCardUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface CardDetailUiState {
    val name: String get() = ""
    val company: String? get() = null
    val position: String? get() = null
    val department: String? get() = null
    val mobilePhone: String? get() = null
    val officePhone: String? get() = null
    val email: String? get() = null
    val address: String? get() = null
    val website: String? get() = null
    val memo: String? get() = null
    val isFavorite: Boolean get() = false
    val ocrConfidence: Float? get() = null
    val tags: List<Tag> get() = emptyList()

    data object Loading : CardDetailUiState
    data class Error(val message: String) : CardDetailUiState

    data class Success(
        override val name: String,
        override val company: String?,
        override val position: String?,
        override val department: String?,
        override val mobilePhone: String?,
        override val officePhone: String?,
        override val email: String?,
        override val address: String?,
        override val website: String?,
        override val memo: String?,
        override val isFavorite: Boolean,
        override val ocrConfidence: Float?,
        override val tags: List<Tag>,
        val cardId: Long
    ) : CardDetailUiState
}

@HiltViewModel
class CardDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCardDetailUseCase: GetCardDetailUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val shareCardUseCase: ShareCardUseCase
) : ViewModel() {

    private val cardId: Long = savedStateHandle["cardId"] ?: 0L

    private val _uiState = MutableStateFlow<CardDetailUiState>(CardDetailUiState.Loading)
    val uiState: StateFlow<CardDetailUiState> = _uiState.asStateFlow()

    init {
        if (cardId <= 0L) {
            _uiState.value = CardDetailUiState.Error("유효하지 않은 명함 ID입니다.")
        } else {
            viewModelScope.launch {
                getCardDetailUseCase(cardId).collect { card ->
                    _uiState.value = if (card != null) {
                        CardDetailUiState.Success(
                            name = card.name, company = card.company,
                            position = card.position, department = card.department,
                            mobilePhone = card.mobilePhone, officePhone = card.officePhone,
                            email = card.email, address = card.address,
                            website = card.website, memo = card.memo,
                            isFavorite = card.isFavorite, ocrConfidence = card.ocrConfidence,
                            tags = card.tags, cardId = card.id
                        )
                    } else {
                        CardDetailUiState.Error("명함을 찾을 수 없습니다.")
                    }
                }
            }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            val current = (_uiState.value as? CardDetailUiState.Success) ?: return@launch
            toggleFavoriteUseCase(cardId, !current.isFavorite)
        }
    }

    fun shareCard() {
        viewModelScope.launch {
            shareCardUseCase(cardId)
        }
    }
}
