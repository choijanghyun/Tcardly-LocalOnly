package com.tcardly.domain.repository

import com.tcardly.core.common.model.ResultWrapper
import com.tcardly.domain.model.AiAnalysisReport
import com.tcardly.domain.model.CompanyInfo
import kotlinx.coroutines.flow.Flow

interface CompanyRepository {
    suspend fun getCompanyInfo(companyName: String): ResultWrapper<CompanyInfo>
    suspend fun requestAiAnalysis(companyName: String): ResultWrapper<AiAnalysisReport>
    suspend fun getAiAnalysisCache(companyName: String): AiAnalysisReport?
    suspend fun sendAiQuestion(companyName: String, question: String, context: List<String>): ResultWrapper<String>
}
