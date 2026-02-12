package com.tcardly.core.database.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.tcardly.core.database.TCardlyDatabase
import com.tcardly.core.database.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TCardlyDatabase {
        // SQLCipher 암호화 설정
        val passphrase = SQLiteDatabase.getBytes("tcardly_secure_key".toCharArray())
        val factory: SupportSQLiteOpenHelper.Factory = SupportFactory(passphrase)

        return Room.databaseBuilder(
            context,
            TCardlyDatabase::class.java,
            TCardlyDatabase.DATABASE_NAME
        )
            .openHelperFactory(factory)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides fun provideBusinessCardDao(db: TCardlyDatabase): BusinessCardDao = db.businessCardDao()
    @Provides fun provideUserProfileDao(db: TCardlyDatabase): UserProfileDao = db.userProfileDao()
    @Provides fun provideGroupDao(db: TCardlyDatabase): GroupDao = db.groupDao()
    @Provides fun provideTagDao(db: TCardlyDatabase): TagDao = db.tagDao()
    @Provides fun provideActivityLogDao(db: TCardlyDatabase): ActivityLogDao = db.activityLogDao()
    @Provides fun provideMeetingDao(db: TCardlyDatabase): MeetingDao = db.meetingDao()
    @Provides fun provideSubscriptionCacheDao(db: TCardlyDatabase): SubscriptionCacheDao = db.subscriptionCacheDao()
    @Provides fun provideCompanyInfoDao(db: TCardlyDatabase): CompanyInfoDao = db.companyInfoDao()
}
