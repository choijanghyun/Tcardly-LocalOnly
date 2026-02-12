package com.tcardly.domain.usecase.card

import com.tcardly.core.common.model.ResultWrapper
import com.tcardly.domain.model.BusinessCard
import com.tcardly.domain.repository.CardRepository
import javax.inject.Inject

class SaveBusinessCardUseCase @Inject constructor(
    private val cardRepository: CardRepository
) {
    suspend operator fun invoke(card: BusinessCard): ResultWrapper<Long> {
        return cardRepository.saveCard(card)
    }
}
