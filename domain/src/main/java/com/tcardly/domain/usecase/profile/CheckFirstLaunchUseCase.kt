package com.tcardly.domain.usecase.profile

import com.tcardly.domain.repository.ProfileRepository
import javax.inject.Inject

class CheckFirstLaunchUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(): Boolean = !profileRepository.hasProfile()
}
