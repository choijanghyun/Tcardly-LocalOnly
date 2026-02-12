package com.tcardly.core.database.entity

import androidx.room.*

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Long = 1,  // 싱글톤 고정 ID
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "currentCompany") val currentCompany: String? = null,
    @ColumnInfo(name = "currentPosition") val currentPosition: String? = null,
    @ColumnInfo(name = "mobilePhone") val mobilePhone: String? = null,
    @ColumnInfo(name = "email") val email: String? = null,
    @ColumnInfo(name = "profileImageUri") val profileImageUri: String? = null,
    @ColumnInfo(name = "careersJson") val careersJson: String? = null,
    @ColumnInfo(name = "educationsJson") val educationsJson: String? = null,
    @ColumnInfo(name = "interestsJson") val interestsJson: String? = null,
    @ColumnInfo(name = "createdAt") val createdAt: Long,
    @ColumnInfo(name = "updatedAt") val updatedAt: Long
)
