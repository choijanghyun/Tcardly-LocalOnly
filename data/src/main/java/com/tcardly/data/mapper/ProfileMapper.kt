package com.tcardly.data.mapper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tcardly.core.database.entity.UserProfileEntity
import com.tcardly.domain.model.Career
import com.tcardly.domain.model.Education
import com.tcardly.domain.model.UserProfile

private val gson = Gson()

fun UserProfileEntity.toDomain(): UserProfile {
    return UserProfile(
        id = id, name = name,
        currentCompany = currentCompany, currentPosition = currentPosition,
        mobilePhone = mobilePhone, email = email,
        profileImageUri = profileImageUri,
        careers = careersJson?.let { gson.fromJson(it, object : TypeToken<List<Career>>() {}.type) } ?: emptyList(),
        educations = educationsJson?.let { gson.fromJson(it, object : TypeToken<List<Education>>() {}.type) } ?: emptyList(),
        interests = interestsJson?.let { gson.fromJson(it, object : TypeToken<List<String>>() {}.type) } ?: emptyList(),
        createdAt = createdAt, updatedAt = updatedAt
    )
}

fun UserProfile.toEntity(): UserProfileEntity {
    return UserProfileEntity(
        id = id, name = name,
        currentCompany = currentCompany, currentPosition = currentPosition,
        mobilePhone = mobilePhone, email = email,
        profileImageUri = profileImageUri,
        careersJson = if (careers.isNotEmpty()) gson.toJson(careers) else null,
        educationsJson = if (educations.isNotEmpty()) gson.toJson(educations) else null,
        interestsJson = if (interests.isNotEmpty()) gson.toJson(interests) else null,
        createdAt = createdAt, updatedAt = updatedAt
    )
}
