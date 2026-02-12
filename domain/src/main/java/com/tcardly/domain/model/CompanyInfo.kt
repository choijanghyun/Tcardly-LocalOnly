package com.tcardly.domain.model

data class CompanyInfo(
    val id: Long = 0,
    val companyName: String,
    val ceoName: String? = null,
    val industry: String? = null,
    val foundedDate: String? = null,
    val address: String? = null,
    val employeeCount: Int? = null,
    val financialData: List<FinancialYear> = emptyList(),
    val creditGrade: String? = null,
    val isListed: Boolean = false,
    val website: String? = null,
    val fetchedAt: Long = 0
)

data class FinancialYear(
    val year: Int,
    val revenue: Long? = null,
    val operatingProfit: Long? = null,
    val netIncome: Long? = null,
    val totalAssets: Long? = null,
    val totalEquity: Long? = null
)
