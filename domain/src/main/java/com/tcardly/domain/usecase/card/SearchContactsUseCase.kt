package com.tcardly.domain.usecase.card

import com.tcardly.domain.model.BusinessCard
import com.tcardly.domain.repository.CardRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchContactsUseCase @Inject constructor(
    private val cardRepository: CardRepository
) {
    operator fun invoke(query: String): Flow<List<BusinessCard>> {
        return if (query.isBlank()) {
            cardRepository.getAllCards()
        } else {
            cardRepository.searchCards(query)
        }
    }
}
