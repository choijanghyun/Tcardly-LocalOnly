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
        // 캐시가 유효하면 한도를 차감하지 않고 반환 (캐시 TTL은 Repository가 관리)
        val cached = companyRepository.getAiAnalysisCache(companyName)
        if (cached != null && System.currentTimeMillis() - cached.generatedAt < AI_CACHE_TTL) {
            return ResultWrapper.Success(cached)
        }

        // 새로운 분석 요청 시 한도 체크
        if (!subscriptionRepository.checkAiUsageLimit()) {
            return ResultWrapper.Error("AI 분석 월간 한도를 초과했습니다.")
        }

        val result = companyRepository.requestAiAnalysis(companyName)
        if (result is ResultWrapper.Success) {
            subscriptionRepository.incrementAiUsage()
        }
        return result
    }

    companion object {
        private const val AI_CACHE_TTL = 24L * 60 * 60 * 1000
    }
}
