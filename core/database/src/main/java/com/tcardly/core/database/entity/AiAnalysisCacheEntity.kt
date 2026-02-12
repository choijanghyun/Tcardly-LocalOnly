package com.tcardly.core.database.entity

import androidx.room.*

@Entity(tableName = "ai_analysis_cache")
data class AiAnalysisCacheEntity(
    @PrimaryKey @ColumnInfo(name = "companyName") val companyName: String,
    @ColumnInfo(name = "analysisJson") val analysisJson: String,
    @ColumnInfo(name = "generatedAt") val generatedAt: Long
)
