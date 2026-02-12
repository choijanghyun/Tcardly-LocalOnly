package com.tcardly.domain.usecase.card

import com.tcardly.core.common.model.ResultWrapper
import com.tcardly.domain.repository.CardRepository
import com.tcardly.domain.repository.SubscriptionRepository
import javax.inject.Inject

class ExportCsvUseCase @Inject constructor(
    private val cardRepository: CardRepository,
    private val subscriptionRepository: SubscriptionRepository
) {
    suspend operator fun invoke(): ResultWrapper<String> {
        if (!subscriptionRepository.checkExportLimit()) {
            return ResultWrapper.Error("내보내기 한도를 초과했습니다. Pro로 업그레이드하세요.")
        }
        val result = cardRepository.exportToCsv()
        if (result is ResultWrapper.Success) {
            subscriptionRepository.incrementExportCount()
        }
        return result
    }
}
