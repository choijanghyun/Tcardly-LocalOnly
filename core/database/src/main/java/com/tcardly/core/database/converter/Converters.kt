package com.tcardly.core.database.converter

import androidx.room.TypeConverter
import com.tcardly.core.common.model.ActivityType
import com.tcardly.core.common.model.SourceType
import com.tcardly.core.common.model.SubscriptionPlan
import com.tcardly.core.common.model.SubscriptionStatus

class Converters {
    @TypeConverter
    fun fromSourceType(value: SourceType): String = value.name
    @TypeConverter
    fun toSourceType(value: String): SourceType = SourceType.valueOf(value)

    @TypeConverter
    fun fromActivityType(value: ActivityType): String = value.name
    @TypeConverter
    fun toActivityType(value: String): ActivityType = ActivityType.valueOf(value)

    @TypeConverter
    fun fromSubscriptionPlan(value: SubscriptionPlan): String = value.name
    @TypeConverter
    fun toSubscriptionPlan(value: String): SubscriptionPlan = SubscriptionPlan.valueOf(value)

    @TypeConverter
    fun fromSubscriptionStatus(value: SubscriptionStatus): String = value.name
    @TypeConverter
    fun toSubscriptionStatus(value: String): SubscriptionStatus = SubscriptionStatus.valueOf(value)
}
