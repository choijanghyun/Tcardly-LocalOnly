package com.tcardly.data.repository

import com.tcardly.core.common.model.ResultWrapper
import com.tcardly.core.common.util.DateUtils
import com.tcardly.core.database.dao.UserProfileDao
import com.tcardly.data.mapper.toDomain
import com.tcardly.data.mapper.toEntity
import com.tcardly.domain.model.UserProfile
import com.tcardly.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepositoryImpl @Inject constructor(
    private val profileDao: UserProfileDao
) : ProfileRepository {

    override fun observeProfile(): Flow<UserProfile?> =
        profileDao.observeProfile().map { it?.toDomain() }

    override suspend fun getProfile(): UserProfile? =
        profileDao.getProfile()?.toDomain()

    override suspend fun hasProfile(): Boolean =
        profileDao.hasProfile()

    override suspend fun createProfile(profile: UserProfile): ResultWrapper<Unit> {
        return try {
            val now = DateUtils.now()
            profileDao.insertOrUpdate(profile.copy(createdAt = now, updatedAt = now).toEntity())
            ResultWrapper.Success(Unit)
        } catch (e: Exception) {
            ResultWrapper.Error("프로필 생성 실패: ${e.message}", e)
        }
    }

    override suspend fun updateProfile(profile: UserProfile): ResultWrapper<Unit> {
        return try {
            profileDao.insertOrUpdate(profile.copy(updatedAt = DateUtils.now()).toEntity())
            ResultWrapper.Success(Unit)
        } catch (e: Exception) {
            ResultWrapper.Error("프로필 수정 실패: ${e.message}", e)
        }
    }
}
