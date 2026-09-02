package com.fitcoach.app.repository

import com.fitcoach.app.data.dao.UserProfileDao
import com.fitcoach.app.data.entity.UserProfile
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userProfileDao: UserProfileDao
) {
    fun observeProfile(): Flow<UserProfile?> = userProfileDao.observeProfile()

    suspend fun getProfileOnce(): UserProfile? = userProfileDao.getProfileOnce()

    suspend fun saveProfile(profile: UserProfile) = userProfileDao.upsert(profile)

    suspend fun hasCompletedOnboarding(): Boolean =
        userProfileDao.isOnboardingCompleted() == true
}
