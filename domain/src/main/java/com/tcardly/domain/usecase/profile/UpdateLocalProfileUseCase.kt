package com.tcardly.domain.usecase.profile

import com.tcardly.core.common.model.ResultWrapper
import com.tcardly.domain.model.UserProfile
import com.tcardly.domain.repository.ProfileRepository
import javax.inject.Inject

class UpdateLocalProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(profile: UserProfile): ResultWrapper<Unit> {
        if (profile.name.trim().length < 2) {
            return ResultWrapper.Error("이름은 2자 이상 입력해주세요.")
        }
        return profileRepository.updateProfile(profile)
    }
}
