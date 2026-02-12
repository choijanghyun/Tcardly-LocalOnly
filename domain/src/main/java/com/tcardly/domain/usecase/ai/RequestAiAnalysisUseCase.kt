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
        if (companyName.isBlank()) {
            return ResultWrapper.Error("기업명을 입력해주세요.")
        }

        // Repository가 캐시를 관리하므로 캐시 히트 시 한도 차감 없이 반환됨
        // 새로운 분석 요청 시 한도 체크
        if (!subscriptionRepository.checkAiUsageLimit()) {
            return ResultWrapper.Error("AI 분석 월간 한도를 초과했습니다.", LIMIT_EXCEEDED_ERROR)
        }

        val result = companyRepository.requestAiAnalysis(companyName)
        if (result is ResultWrapper.Success) {
            subscriptionRepository.incrementAiUsage()
        }
        return result
    }

    companion object {
        val LIMIT_EXCEEDED_ERROR = IllegalStateException("limit_exceeded")
    }
}
