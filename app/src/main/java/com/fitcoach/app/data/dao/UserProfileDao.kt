package com.fitcoach.app.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.fitcoach.app.data.entity.UserProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {

    @Upsert
    suspend fun upsert(profile: UserProfile)

    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    fun observeProfile(): Flow<UserProfile?>

    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    suspend fun getProfileOnce(): UserProfile?

    @Query("SELECT onboardingCompleted FROM user_profile WHERE id = 1 LIMIT 1")
    suspend fun isOnboardingCompleted(): Boolean?
}
