package com.tcardly.data.repository

import com.tcardly.core.common.model.ResultWrapper
import com.tcardly.core.common.model.SubscriptionPlan
import com.tcardly.core.common.model.SubscriptionStatus
import com.tcardly.core.database.dao.SubscriptionCacheDao
import com.tcardly.core.database.entity.SubscriptionCacheEntity
import com.tcardly.domain.repository.SubscriptionRepository
import com.tcardly.domain.repository.SubscriptionState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubscriptionRepositoryImpl @Inject constructor(
    private val dao: SubscriptionCacheDao
) : SubscriptionRepository {

    override fun observeSubscription(): Flow<SubscriptionState> {
        return dao.observeSubscription().map { entity ->
            entity?.toSubscriptionState() ?: SubscriptionState()
        }
    }

    override suspend fun getSubscriptionState(): SubscriptionState {
        return dao.getSubscription()?.toSubscriptionState() ?: SubscriptionState()
    }

    override suspend fun purchaseSubscription(plan: SubscriptionPlan): ResultWrapper<Unit> {
        return try {
            val now = System.currentTimeMillis()
            val expiresAt = now + 30L * 24 * 60 * 60 * 1000
            val entity = SubscriptionCacheEntity(
                id = 1,
                plan = plan,
                status = SubscriptionStatus.ACTIVE,
                expiresAt = expiresAt,
                purchaseToken = "",
                aiUsageCount = 0,
                exportCount = 0,
                aiUsageResetAt = now,
                lastVerifiedAt = now
            )
            dao.insertOrUpdate(entity)
            ResultWrapper.Success(Unit)
        } catch (e: Exception) {
            ResultWrapper.Error("구매 처리 중 오류가 발생했습니다.", e)
        }
    }

    override suspend fun restorePurchases(): ResultWrapper<SubscriptionState> {
        return try {
            val current = getSubscriptionState()
            ResultWrapper.Success(current)
        } catch (e: Exception) {
            ResultWrapper.Error("구매 복원에 실패했습니다.", e)
        }
    }

    override suspend fun verifySubscription(): ResultWrapper<SubscriptionState> {
        return try {
            val entity = dao.getSubscription()
                ?: return ResultWrapper.Success(SubscriptionState())
            val now = System.currentTimeMillis()
            if (entity.expiresAt != null && entity.expiresAt < now
                && entity.status == SubscriptionStatus.ACTIVE
            ) {
                val updated = entity.copy(status = SubscriptionStatus.EXPIRED, lastVerifiedAt = now)
                dao.insertOrUpdate(updated)
                return ResultWrapper.Success(updated.toSubscriptionState())
            }
            ResultWrapper.Success(entity.toSubscriptionState())
        } catch (e: Exception) {
            ResultWrapper.Error("구독 검증에 실패했습니다.", e)
        }
    }

    override suspend fun checkAiUsageLimit(): Boolean {
        val state = getSubscriptionState()
        val effectivePlan = if (state.status == SubscriptionStatus.ACTIVE) state.plan else SubscriptionPlan.FREE
        return when (effectivePlan) {
            SubscriptionPlan.BUSINESS -> true
            SubscriptionPlan.PRO -> state.aiUsageCount < 30
            SubscriptionPlan.FREE -> state.aiUsageCount < 3
        }
    }

    override suspend fun incrementAiUsage() {
        try { dao.incrementAiUsage() } catch (_: Exception) { }
    }

    override suspend fun checkExportLimit(): Boolean {
        val state = getSubscriptionState()
        val effectivePlan = if (state.status == SubscriptionStatus.ACTIVE) state.plan else SubscriptionPlan.FREE
        return when (effectivePlan) {
            SubscriptionPlan.BUSINESS, SubscriptionPlan.PRO -> true
            SubscriptionPlan.FREE -> state.exportCount < 100
        }
    }

    override suspend fun incrementExportCount() {
        try { dao.incrementExportCount() } catch (_: Exception) { }
    }

    override suspend fun resetMonthlyAiUsage() {
        try { dao.resetAiUsage(System.currentTimeMillis()) } catch (_: Exception) { }
    }

    private fun SubscriptionCacheEntity.toSubscriptionState(): SubscriptionState {
        return SubscriptionState(
            plan = plan, status = status, expiresAt = expiresAt,
            aiUsageCount = aiUsageCount,
            aiUsageLimit = when (plan) {
                SubscriptionPlan.FREE -> 3; SubscriptionPlan.PRO -> 30; SubscriptionPlan.BUSINESS -> Int.MAX_VALUE
            },
            exportCount = exportCount,
            exportLimit = when (plan) { SubscriptionPlan.FREE -> 100; else -> Int.MAX_VALUE }
        )
    }
}
