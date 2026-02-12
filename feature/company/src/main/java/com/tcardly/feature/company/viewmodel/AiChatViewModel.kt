package com.tcardly.feature.company.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tcardly.core.common.model.ResultWrapper
import com.tcardly.domain.repository.CompanyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChatMessage(val content: String, val isUser: Boolean, val timestamp: Long = System.currentTimeMillis())

data class AiChatUiState(
    val companyName: String = "",
    val messages: List<ChatMessage> = emptyList(),
    val inputText: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class AiChatViewModel @Inject constructor(
    private val companyRepository: CompanyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AiChatUiState())
    val uiState: StateFlow<AiChatUiState> = _uiState.asStateFlow()

    fun init(companyName: String) {
        _uiState.update { it.copy(companyName = companyName) }
    }

    fun updateInput(text: String) {
        _uiState.update { it.copy(inputText = text) }
    }

    fun sendMessage() {
        val text = _uiState.value.inputText.trim()
        if (text.isBlank()) return

        val userMsg = ChatMessage(content = text, isUser = true)
        _uiState.update {
            it.copy(
                messages = it.messages + userMsg,
                inputText = "",
                isLoading = true,
                error = null
            )
        }

        viewModelScope.launch {
            val context = _uiState.value.messages.takeLast(10).map {
                "${if (it.isUser) "User" else "AI"}: ${it.content}"
            }
            when (val result = companyRepository.sendAiQuestion(_uiState.value.companyName, text, context)) {
                is ResultWrapper.Success -> {
                    val aiMsg = ChatMessage(content = result.data, isUser = false)
                    _uiState.update { it.copy(messages = it.messages + aiMsg, isLoading = false) }
                }
                is ResultWrapper.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = result.message) }
                }
                is ResultWrapper.Loading -> {}
            }
        }
    }
}
