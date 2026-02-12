package com.tcardly.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tcardly.core.database.converter.Converters
import com.tcardly.core.database.dao.*
import com.tcardly.core.database.entity.*

@Database(
    entities = [
        BusinessCardEntity::class,
        UserProfileEntity::class,
        GroupEntity::class,
        TagEntity::class,
        CardTagEntity::class,
        ActivityLogEntity::class,
        MeetingEntity::class,
        SubscriptionCacheEntity::class,
        CompanyInfoEntity::class,
        AiAnalysisCacheEntity::class,
        AdPreferenceEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class TCardlyDatabase : RoomDatabase() {
    abstract fun businessCardDao(): BusinessCardDao
    abstract fun userProfileDao(): UserProfileDao
    abstract fun groupDao(): GroupDao
    abstract fun tagDao(): TagDao
    abstract fun activityLogDao(): ActivityLogDao
    abstract fun meetingDao(): MeetingDao
    abstract fun subscriptionCacheDao(): SubscriptionCacheDao
    abstract fun companyInfoDao(): CompanyInfoDao

    companion object {
        const val DATABASE_NAME = "tcardly.db"
    }
}
