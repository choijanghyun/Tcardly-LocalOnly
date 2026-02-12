package com.tcardly.core.database.di

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
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
import java.security.KeyStore
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private const val KEYSTORE_ALIAS = "tcardly_db_key"
    private const val PREF_NAME = "tcardly_db_prefs"
    private const val PREF_KEY_PASSPHRASE = "db_passphrase"

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TCardlyDatabase {
        val passphrase = getOrCreatePassphrase(context)
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

    private fun getOrCreatePassphrase(context: Context): ByteArray {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val existing = prefs.getString(PREF_KEY_PASSPHRASE, null)
        if (existing != null) {
            return SQLiteDatabase.getBytes(existing.toCharArray())
        }
        val generated = java.util.UUID.randomUUID().toString().replace("-", "")
        prefs.edit().putString(PREF_KEY_PASSPHRASE, generated).apply()
        return SQLiteDatabase.getBytes(generated.toCharArray())
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
