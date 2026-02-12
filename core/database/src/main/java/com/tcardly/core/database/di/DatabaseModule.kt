package com.tcardly.core.database.di

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
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
import timber.log.Timber
import java.security.KeyStore
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private const val KEYSTORE_ALIAS = "tcardly_db_key"
    private const val ANDROID_KEYSTORE = "AndroidKeyStore"

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
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    Timber.d("데이터베이스 생성 완료: ${TCardlyDatabase.DATABASE_NAME}")
                }

                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onOpen(db)
                    Timber.d("데이터베이스 연결: version=${db.version}")
                }
            })
            .build()
    }

    /**
     * Android Keystore를 사용하여 SQLCipher 패스프레이즈를 안전하게 관리.
     * Keystore에 키가 없으면 새로 생성하고, 실패 시 디바이스 고유값 기반 폴백 사용.
     */
    private fun getOrCreatePassphrase(context: Context): ByteArray {
        return try {
            val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
            keyStore.load(null)

            if (!keyStore.containsAlias(KEYSTORE_ALIAS)) {
                val keyGenerator = KeyGenerator.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE
                )
                keyGenerator.init(
                    KeyGenParameterSpec.Builder(
                        KEYSTORE_ALIAS,
                        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                    )
                        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                        .setKeySize(256)
                        .build()
                )
                keyGenerator.generateKey()
                Timber.d("새 데이터베이스 암호화 키 생성 완료")
            }

            val secretKey = keyStore.getKey(KEYSTORE_ALIAS, null) as SecretKey
            SQLiteDatabase.getBytes(secretKey.encoded.toString().toCharArray())
        } catch (e: Exception) {
            Timber.e(e, "Keystore 기반 패스프레이즈 생성 실패, 폴백 사용")
            SQLiteDatabase.getBytes(
                (context.packageName + android.os.Build.FINGERPRINT).toCharArray()
            )
        }
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
