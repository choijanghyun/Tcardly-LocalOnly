package com.tcardly.core.database.entity

import androidx.room.*

@Entity(tableName = "ad_preference")
data class AdPreferenceEntity(
    @PrimaryKey val id: Long = 1,
    @ColumnInfo(name = "frequency") val frequency: String = "normal",
    @ColumnInfo(name = "blockedAdIds") val blockedAdIds: String? = null
)
