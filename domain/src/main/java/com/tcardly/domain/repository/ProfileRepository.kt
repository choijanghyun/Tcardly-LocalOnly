package com.tcardly.domain.repository

import com.tcardly.core.common.model.ResultWrapper
import com.tcardly.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun observeProfile(): Flow<UserProfile?>
    suspend fun getProfile(): UserProfile?
    suspend fun hasProfile(): Boolean
    suspend fun createProfile(profile: UserProfile): ResultWrapper<Unit>
    suspend fun updateProfile(profile: UserProfile): ResultWrapper<Unit>
}
