package com.tcardly.core.database.dao

import androidx.room.*
import com.tcardly.core.database.entity.SubscriptionCacheEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SubscriptionCacheDao {
    @Query("SELECT * FROM subscription_cache WHERE id = 1")
    fun observeSubscription(): Flow<SubscriptionCacheEntity?>

    @Query("SELECT * FROM subscription_cache WHERE id = 1")
    suspend fun getSubscription(): SubscriptionCacheEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(cache: SubscriptionCacheEntity)

    @Query("UPDATE subscription_cache SET aiUsageCount = aiUsageCount + 1 WHERE id = 1")
    suspend fun incrementAiUsage()

    @Query("UPDATE subscription_cache SET exportCount = exportCount + 1 WHERE id = 1")
    suspend fun incrementExportCount()

    @Query("UPDATE subscription_cache SET aiUsageCount = 0, aiUsageResetAt = :resetAt WHERE id = 1")
    suspend fun resetAiUsage(resetAt: Long)
}
