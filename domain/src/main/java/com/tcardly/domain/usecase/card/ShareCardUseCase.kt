package com.tcardly.domain.usecase.card

import com.tcardly.core.common.model.ResultWrapper
import com.tcardly.domain.repository.CardRepository
import javax.inject.Inject

class ShareCardUseCase @Inject constructor(
    private val cardRepository: CardRepository
) {
    suspend operator fun invoke(cardId: Long): ResultWrapper<String> {
        return cardRepository.generateVCard(cardId)
    }
}
