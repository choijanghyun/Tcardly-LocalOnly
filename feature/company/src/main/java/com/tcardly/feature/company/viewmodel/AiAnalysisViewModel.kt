package com.tcardly.feature.company.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tcardly.core.common.model.ResultWrapper
import com.tcardly.domain.model.AiAnalysisReport
import com.tcardly.domain.usecase.ai.RequestAiAnalysisUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AiAnalysisUiState(
    val companyName: String = "",
    val report: AiAnalysisReport? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLimitExceeded: Boolean = false
)

@HiltViewModel
class AiAnalysisViewModel @Inject constructor(
    private val requestAiAnalysisUseCase: RequestAiAnalysisUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AiAnalysisUiState())
    val uiState: StateFlow<AiAnalysisUiState> = _uiState.asStateFlow()

    fun requestAnalysis(companyName: String) {
        _uiState.value = AiAnalysisUiState(companyName = companyName, isLoading = true)
        viewModelScope.launch {
            when (val result = requestAiAnalysisUseCase(companyName)) {
                is ResultWrapper.Success -> {
                    _uiState.value = AiAnalysisUiState(
                        companyName = companyName, report = result.data, isLoading = false
                    )
                }
                is ResultWrapper.Error -> {
                    val isLimit = result.throwable == RequestAiAnalysisUseCase.LIMIT_EXCEEDED_ERROR
                    _uiState.value = AiAnalysisUiState(
                        companyName = companyName, isLoading = false,
                        error = result.message, isLimitExceeded = isLimit
                    )
                }
                is ResultWrapper.Loading -> {}
            }
        }
    }
}
