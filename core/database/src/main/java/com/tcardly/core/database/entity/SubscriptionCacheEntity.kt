package com.tcardly.core.database.entity

import androidx.room.*
import com.tcardly.core.common.model.SubscriptionPlan
import com.tcardly.core.common.model.SubscriptionStatus

@Entity(tableName = "subscription_cache")
data class SubscriptionCacheEntity(
    @PrimaryKey val id: Long = 1,  // 싱글톤
    @ColumnInfo(name = "plan") val plan: SubscriptionPlan = SubscriptionPlan.FREE,
    @ColumnInfo(name = "status") val status: SubscriptionStatus = SubscriptionStatus.NONE,
    @ColumnInfo(name = "expiresAt") val expiresAt: Long? = null,
    @ColumnInfo(name = "purchaseToken") val purchaseToken: String? = null,
    @ColumnInfo(name = "aiUsageCount") val aiUsageCount: Int = 0,
    @ColumnInfo(name = "aiUsageResetAt") val aiUsageResetAt: Long = 0,
    @ColumnInfo(name = "exportCount") val exportCount: Int = 0,
    @ColumnInfo(name = "lastVerifiedAt") val lastVerifiedAt: Long = 0
)
