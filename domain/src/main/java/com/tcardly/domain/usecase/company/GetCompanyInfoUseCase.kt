package com.tcardly.domain.usecase.company

import com.tcardly.core.common.model.ResultWrapper
import com.tcardly.domain.model.CompanyInfo
import com.tcardly.domain.repository.CompanyRepository
import javax.inject.Inject

class GetCompanyInfoUseCase @Inject constructor(
    private val companyRepository: CompanyRepository
) {
    suspend operator fun invoke(companyName: String): ResultWrapper<CompanyInfo> {
        return companyRepository.getCompanyInfo(companyName)
    }
}
