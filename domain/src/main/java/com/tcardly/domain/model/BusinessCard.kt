package com.tcardly.domain.model

import com.tcardly.core.common.model.SourceType

data class BusinessCard(
    val id: Long = 0,
    val name: String,
    val company: String? = null,
    val position: String? = null,
    val department: String? = null,
    val mobilePhone: String? = null,
    val officePhone: String? = null,
    val email: String? = null,
    val address: String? = null,
    val website: String? = null,
    val memo: String? = null,
    val profileImageUri: String? = null,
    val cardImageUri: String? = null,
    val sourceType: SourceType,
    val ocrConfidence: Float? = null,
    val groupId: Long? = null,
    val companyInfoId: Long? = null,
    val isFavorite: Boolean = false,
    val lastContactedAt: Long? = null,
    val createdAt: Long,
    val updatedAt: Long,
    val tags: List<Tag> = emptyList()
)
