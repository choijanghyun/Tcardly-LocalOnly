package com.tcardly.data.mapper

import com.tcardly.core.database.entity.BusinessCardEntity
import com.tcardly.domain.model.BusinessCard

fun BusinessCardEntity.toDomain(tags: List<com.tcardly.domain.model.Tag> = emptyList()): BusinessCard {
    return BusinessCard(
        id = id, name = name, company = company, position = position,
        department = department, mobilePhone = mobilePhone, officePhone = officePhone,
        email = email, address = address, website = website, memo = memo,
        profileImageUri = profileImageUri, cardImageUri = cardImageUri,
        sourceType = sourceType, ocrConfidence = ocrConfidence,
        groupId = groupId, companyInfoId = companyInfoId,
        isFavorite = isFavorite, lastContactedAt = lastContactedAt,
        createdAt = createdAt, updatedAt = updatedAt, tags = tags
    )
}

fun BusinessCard.toEntity(): BusinessCardEntity {
    return BusinessCardEntity(
        id = id, name = name, company = company, position = position,
        department = department, mobilePhone = mobilePhone, officePhone = officePhone,
        email = email, address = address, website = website, memo = memo,
        profileImageUri = profileImageUri, cardImageUri = cardImageUri,
        sourceType = sourceType, ocrConfidence = ocrConfidence,
        groupId = groupId, companyInfoId = companyInfoId,
        isFavorite = isFavorite, lastContactedAt = lastContactedAt,
        createdAt = createdAt, updatedAt = updatedAt
    )
}
