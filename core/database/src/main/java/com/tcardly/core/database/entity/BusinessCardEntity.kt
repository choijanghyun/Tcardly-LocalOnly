package com.tcardly.core.database.entity

import androidx.room.*
import com.tcardly.core.common.model.SourceType

@Entity(
    tableName = "business_cards",
    indices = [
        Index(value = ["mobilePhone"]),
        Index(value = ["company"]),
        Index(value = ["groupId"]),
        Index(value = ["createdAt"]),
        Index(value = ["name", "mobilePhone"]),
        Index(value = ["email"]),
        Index(value = ["isFavorite"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = GroupEntity::class,
            parentColumns = ["id"],
            childColumns = ["groupId"],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class BusinessCardEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "company") val company: String? = null,
    @ColumnInfo(name = "position") val position: String? = null,
    @ColumnInfo(name = "department") val department: String? = null,
    @ColumnInfo(name = "mobilePhone") val mobilePhone: String? = null,
    @ColumnInfo(name = "officePhone") val officePhone: String? = null,
    @ColumnInfo(name = "email") val email: String? = null,
    @ColumnInfo(name = "address") val address: String? = null,
    @ColumnInfo(name = "website") val website: String? = null,
    @ColumnInfo(name = "memo") val memo: String? = null,
    @ColumnInfo(name = "profileImageUri") val profileImageUri: String? = null,
    @ColumnInfo(name = "cardImageUri") val cardImageUri: String? = null,
    @ColumnInfo(name = "sourceType") val sourceType: SourceType,
    @ColumnInfo(name = "ocrConfidence") val ocrConfidence: Float? = null,
    @ColumnInfo(name = "groupId") val groupId: Long? = null,
    @ColumnInfo(name = "companyInfoId") val companyInfoId: Long? = null,
    @ColumnInfo(name = "isFavorite") val isFavorite: Boolean = false,
    @ColumnInfo(name = "lastContactedAt") val lastContactedAt: Long? = null,
    @ColumnInfo(name = "createdAt") val createdAt: Long,
    @ColumnInfo(name = "updatedAt") val updatedAt: Long
)
