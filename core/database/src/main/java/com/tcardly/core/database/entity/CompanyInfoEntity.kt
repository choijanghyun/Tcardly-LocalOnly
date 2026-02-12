package com.tcardly.core.database.entity

import androidx.room.*

@Entity(tableName = "company_info")
data class CompanyInfoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "companyName") val companyName: String,
    @ColumnInfo(name = "ceoName") val ceoName: String? = null,
    @ColumnInfo(name = "industry") val industry: String? = null,
    @ColumnInfo(name = "foundedDate") val foundedDate: String? = null,
    @ColumnInfo(name = "address") val address: String? = null,
    @ColumnInfo(name = "employeeCount") val employeeCount: Int? = null,
    @ColumnInfo(name = "financialDataJson") val financialDataJson: String? = null,
    @ColumnInfo(name = "creditGrade") val creditGrade: String? = null,
    @ColumnInfo(name = "isListed") val isListed: Boolean = false,
    @ColumnInfo(name = "website") val website: String? = null,
    @ColumnInfo(name = "fetchedAt") val fetchedAt: Long  // TTL 7일 캐시
)
