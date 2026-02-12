package com.tcardly.domain.usecase.subscription

import com.tcardly.core.common.model.ResultWrapper
import com.tcardly.core.common.model.SubscriptionPlan
import com.tcardly.domain.repository.SubscriptionRepository
import javax.inject.Inject

class PurchaseSubscriptionUseCase @Inject constructor(
    private val subscriptionRepository: SubscriptionRepository
) {
    suspend operator fun invoke(plan: SubscriptionPlan): ResultWrapper<Unit> {
        return subscriptionRepository.purchaseSubscription(plan)
    }
}
