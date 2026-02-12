package com.tcardly.domain.model

data class OcrResult(
    val name: String? = null,
    val company: String? = null,
    val position: String? = null,
    val department: String? = null,
    val mobilePhone: String? = null,
    val officePhone: String? = null,
    val email: String? = null,
    val address: String? = null,
    val website: String? = null,
    val confidence: Float = 0f,
    val cardImageUri: String? = null,
    val rawText: String = ""
)
