package com.tcardly.domain.usecase.card

import com.tcardly.domain.model.BusinessCard
import com.tcardly.domain.repository.CardRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecentCardsUseCase @Inject constructor(
    private val cardRepository: CardRepository
) {
    operator fun invoke(limit: Int = 5): Flow<List<BusinessCard>> {
        return cardRepository.getRecentCards(limit)
    }
}
