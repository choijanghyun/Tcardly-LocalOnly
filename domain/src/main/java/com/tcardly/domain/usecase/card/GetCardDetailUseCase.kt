package com.tcardly.domain.usecase.card

import com.tcardly.domain.model.BusinessCard
import com.tcardly.domain.repository.CardRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCardDetailUseCase @Inject constructor(
    private val cardRepository: CardRepository
) {
    operator fun invoke(cardId: Long): Flow<BusinessCard?> {
        return cardRepository.observeCard(cardId)
    }
}
