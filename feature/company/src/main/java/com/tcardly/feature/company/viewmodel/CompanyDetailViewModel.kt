package com.tcardly.feature.company.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tcardly.core.common.model.ResultWrapper
import com.tcardly.domain.model.CompanyInfo
import com.tcardly.domain.model.FinancialYear
import com.tcardly.domain.usecase.company.GetCompanyInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CompanyDetailUiState(
    val companyName: String = "",
    val companyInfo: CompanyInfo? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class CompanyDetailViewModel @Inject constructor(
    private val getCompanyInfoUseCase: GetCompanyInfoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CompanyDetailUiState())
    val uiState: StateFlow<CompanyDetailUiState> = _uiState.asStateFlow()

    fun loadCompany(companyName: String) {
        _uiState.value = CompanyDetailUiState(companyName = companyName, isLoading = true)
        viewModelScope.launch {
            when (val result = getCompanyInfoUseCase(companyName)) {
                is ResultWrapper.Success -> {
                    _uiState.value = CompanyDetailUiState(
                        companyName = companyName,
                        companyInfo = result.data,
                        isLoading = false
                    )
                }
                is ResultWrapper.Error -> {
                    _uiState.value = CompanyDetailUiState(
                        companyName = companyName,
                        isLoading = false,
                        error = result.message
                    )
                }
                is ResultWrapper.Loading -> {}
            }
        }
    }
}
