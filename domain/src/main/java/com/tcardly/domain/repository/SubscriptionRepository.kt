package com.tcardly.domain.repository

import com.tcardly.core.common.model.ResultWrapper
import com.tcardly.core.common.model.SubscriptionPlan
import com.tcardly.core.common.model.SubscriptionStatus
import kotlinx.coroutines.flow.Flow

interface SubscriptionRepository {
    fun observeSubscription(): Flow<SubscriptionState>
    suspend fun getSubscriptionState(): SubscriptionState
    suspend fun purchaseSubscription(plan: SubscriptionPlan): ResultWrapper<Unit>
    suspend fun restorePurchases(): ResultWrapper<SubscriptionState>
    suspend fun verifySubscription(): ResultWrapper<SubscriptionState>
    suspend fun checkAiUsageLimit(): Boolean  // true = 사용 가능
    suspend fun incrementAiUsage()
    suspend fun checkExportLimit(): Boolean
    suspend fun incrementExportCount()
    suspend fun resetMonthlyAiUsage()
}

data class SubscriptionState(
    val plan: SubscriptionPlan = SubscriptionPlan.FREE,
    val status: SubscriptionStatus = SubscriptionStatus.NONE,
    val expiresAt: Long? = null,
    val aiUsageCount: Int = 0,
    val aiUsageLimit: Int = 3,  // Free: 3, Pro: 30, Biz: unlimited
    val exportCount: Int = 0,
    val exportLimit: Int = 100  // Free: 100, Pro/Biz: unlimited
)
