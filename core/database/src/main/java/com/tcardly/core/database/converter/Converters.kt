package com.tcardly.core.database.converter

import androidx.room.TypeConverter
import com.tcardly.core.common.model.ActivityType
import com.tcardly.core.common.model.SourceType
import com.tcardly.core.common.model.SubscriptionPlan
import com.tcardly.core.common.model.SubscriptionStatus
import timber.log.Timber

class Converters {
    @TypeConverter
    fun fromSourceType(value: SourceType): String = value.name
    @TypeConverter
    fun toSourceType(value: String): SourceType = enumValueOfSafe(value) ?: SourceType.MANUAL

    @TypeConverter
    fun fromActivityType(value: ActivityType): String = value.name
    @TypeConverter
    fun toActivityType(value: String): ActivityType = enumValueOfSafe(value) ?: ActivityType.MANUAL_INPUT

    @TypeConverter
    fun fromSubscriptionPlan(value: SubscriptionPlan): String = value.name
    @TypeConverter
    fun toSubscriptionPlan(value: String): SubscriptionPlan = enumValueOfSafe(value) ?: SubscriptionPlan.FREE

    @TypeConverter
    fun fromSubscriptionStatus(value: SubscriptionStatus): String = value.name
    @TypeConverter
    fun toSubscriptionStatus(value: String): SubscriptionStatus = enumValueOfSafe(value) ?: SubscriptionStatus.NONE

    private inline fun <reified T : Enum<T>> enumValueOfSafe(name: String): T? {
        return try {
            enumValueOf<T>(name)
        } catch (e: IllegalArgumentException) {
            Timber.w(e, "알 수 없는 enum 값: ${T::class.simpleName}.$name")
            null
        }
    }
}
