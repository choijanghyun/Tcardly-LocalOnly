package com.tcardly.feature.contacts.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tcardly.core.common.model.ResultWrapper
import com.tcardly.domain.model.BusinessCard
import com.tcardly.domain.model.Tag
import com.tcardly.domain.usecase.card.DeleteCardUseCase
import com.tcardly.domain.usecase.card.SearchContactsUseCase
import com.tcardly.domain.usecase.card.ToggleFavoriteUseCase
import com.tcardly.domain.repository.GroupTagRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface ContactListUiState {
    data object Loading : ContactListUiState
    data class Success(
        val cards: List<BusinessCard>,
        val tags: List<Tag>,
        val selectedTagId: Long? = null,
        val query: String = "",
        val sortBy: SortOption = SortOption.DATE
    ) : ContactListUiState
    data object Empty : ContactListUiState
    data class Error(val message: String) : ContactListUiState
}

enum class SortOption { DATE, NAME, COMPANY }

@OptIn(FlowPreview::class)
@HiltViewModel
class ContactListViewModel @Inject constructor(
    private val searchContactsUseCase: SearchContactsUseCase,
    private val deleteCardUseCase: DeleteCardUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val groupTagRepository: GroupTagRepository
) : ViewModel() {

    private val _query = MutableStateFlow("")
    private val _selectedTagId = MutableStateFlow<Long?>(null)
    private val _sortBy = MutableStateFlow(SortOption.DATE)
    
    private val _uiState = MutableStateFlow<ContactListUiState>(ContactListUiState.Loading)
    val uiState: StateFlow<ContactListUiState> = _uiState.asStateFlow()

    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage: SharedFlow<String> = _snackbarMessage.asSharedFlow()

    init {
        viewModelScope.launch {
            combine(
                _query.debounce(300),
                groupTagRepository.getAllTags(),
                _selectedTagId,
                _sortBy
            ) { query, tags, selectedTagId, sortBy ->
                searchContactsUseCase(query).map { cards ->
                    val filteredCards = if (selectedTagId != null) {
                        cards.filter { card -> card.tags.any { it.id == selectedTagId } }
                    } else cards

                    val sortedCards = when (sortBy) {
                        SortOption.DATE -> filteredCards.sortedByDescending { it.createdAt }
                        SortOption.NAME -> filteredCards.sortedBy { it.name }
                        SortOption.COMPANY -> filteredCards.sortedBy { it.company ?: "" }
                    }

                    if (sortedCards.isEmpty() && query.isBlank() && selectedTagId == null) {
                        ContactListUiState.Empty
                    } else {
                        ContactListUiState.Success(
                            cards = sortedCards,
                            tags = tags,
                            query = query,
                            selectedTagId = selectedTagId,
                            sortBy = sortBy
                        )
                    }
                }
            }.flatMapLatest { it }.collect { _uiState.value = it }
        }
    }

    fun onSearchQueryChanged(query: String) { _query.value = query }
    
    fun onTagSelected(tagId: Long?) { _selectedTagId.value = tagId }
    
    fun onSortChanged(sort: SortOption) { _sortBy.value = sort }

    fun onDeleteCard(cardId: Long) {
        viewModelScope.launch {
            when (val result = deleteCardUseCase(cardId)) {
                is ResultWrapper.Success -> _snackbarMessage.emit("명함이 삭제되었습니다")
                is ResultWrapper.Error -> _snackbarMessage.emit(result.message)
                else -> {}
            }
        }
    }

    fun onToggleFavorite(cardId: Long, isFavorite: Boolean) {
        viewModelScope.launch {
            toggleFavoriteUseCase(cardId, !isFavorite)
        }
    }
}
