package com.tcardly.domain.usecase.card

import com.tcardly.core.common.model.ResultWrapper
import com.tcardly.domain.repository.CardRepository
import javax.inject.Inject

class DeleteCardUseCase @Inject constructor(
    private val cardRepository: CardRepository
) {
    suspend operator fun invoke(cardId: Long): ResultWrapper<Unit> {
        return cardRepository.deleteCard(cardId)
    }
}
