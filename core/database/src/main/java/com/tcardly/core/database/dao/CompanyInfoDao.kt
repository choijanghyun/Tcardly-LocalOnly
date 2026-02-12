package com.tcardly.core.database.dao

import androidx.room.*
import com.tcardly.core.database.entity.CompanyInfoEntity
import com.tcardly.core.database.entity.AiAnalysisCacheEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CompanyInfoDao {
    @Query("SELECT * FROM company_info WHERE companyName = :name LIMIT 1")
    suspend fun getByName(name: String): CompanyInfoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(info: CompanyInfoEntity): Long

    @Query("SELECT * FROM ai_analysis_cache WHERE companyName = :name LIMIT 1")
    suspend fun getAiCache(name: String): AiAnalysisCacheEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAiCache(cache: AiAnalysisCacheEntity)
}
