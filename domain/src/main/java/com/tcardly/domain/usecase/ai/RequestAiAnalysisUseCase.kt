package com.tcardly.domain.usecase.ai

import com.tcardly.core.common.model.ResultWrapper
import com.tcardly.domain.model.AiAnalysisReport
import com.tcardly.domain.repository.CompanyRepository
import com.tcardly.domain.repository.SubscriptionRepository
import javax.inject.Inject

class RequestAiAnalysisUseCase @Inject constructor(
    private val companyRepository: CompanyRepository,
    private val subscriptionRepository: SubscriptionRepository
) {
    suspend operator fun invoke(companyName: String): ResultWrapper<AiAnalysisReport> {
        // 캐시 확인 (24시간 이내 → 한도 미차감)
        val cached = companyRepository.getAiAnalysisCache(companyName)
        if (cached != null && System.currentTimeMillis() - cached.generatedAt < 24 * 60 * 60 * 1000L) {
            return ResultWrapper.Success(cached)
        }

        // AI 사용 한도 체크
        if (!subscriptionRepository.checkAiUsageLimit()) {
            return ResultWrapper.Error("AI 분석 월간 한도를 초과했습니다.")
        }

        val result = companyRepository.requestAiAnalysis(companyName)
        if (result is ResultWrapper.Success) {
            subscriptionRepository.incrementAiUsage()
        }
        return result
    }
}
