package com.tcardly.domain.usecase.card

import com.tcardly.core.common.model.ResultWrapper
import com.tcardly.domain.repository.CardRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val cardRepository: CardRepository
) {
    suspend operator fun invoke(cardId: Long, isFavorite: Boolean): ResultWrapper<Unit> {
        return cardRepository.toggleFavorite(cardId, isFavorite)
    }
}
