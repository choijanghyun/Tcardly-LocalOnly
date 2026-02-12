package com.tcardly.core.common.model

enum class SubscriptionPlan(val productId: String, val displayName: String, val priceWon: Int) {
    FREE("", "Free", 0),
    PRO("tcardly_pro_monthly", "Pro", 9900),
    BUSINESS("tcardly_biz_monthly", "Business", 29900);
    
    companion object {
        fun fromProductId(productId: String): SubscriptionPlan {
            return entries.find { it.productId == productId } ?: FREE
        }
    }
}

enum class SubscriptionStatus {
    ACTIVE,
    EXPIRED,
    CANCELLED,
    PENDING,
    NONE
}
