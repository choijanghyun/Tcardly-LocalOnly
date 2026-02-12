package com.tcardly.domain.usecase.subscription

import com.tcardly.core.common.model.ResultWrapper
import com.tcardly.domain.repository.SubscriptionRepository
import com.tcardly.domain.repository.SubscriptionState
import javax.inject.Inject

class RestorePurchaseUseCase @Inject constructor(
    private val subscriptionRepository: SubscriptionRepository
) {
    suspend operator fun invoke(): ResultWrapper<SubscriptionState> {
        return subscriptionRepository.restorePurchases()
    }
}
