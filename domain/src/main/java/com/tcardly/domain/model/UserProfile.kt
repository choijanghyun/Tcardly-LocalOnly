package com.tcardly.domain.model

data class UserProfile(
    val id: Long = 1,
    val name: String,
    val currentCompany: String? = null,
    val currentPosition: String? = null,
    val mobilePhone: String? = null,
    val email: String? = null,
    val profileImageUri: String? = null,
    val careers: List<Career> = emptyList(),
    val educations: List<Education> = emptyList(),
    val interests: List<String> = emptyList(),
    val createdAt: Long = 0,
    val updatedAt: Long = 0
)

data class Career(
    val company: String,
    val position: String,
    val startDate: String,
    val endDate: String? = null,
    val isCurrent: Boolean = false
)

data class Education(
    val school: String,
    val major: String? = null,
    val degree: String? = null,
    val graduationYear: String? = null
)
