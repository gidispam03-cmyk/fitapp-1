package com.fitcoach.app.di

import android.content.Context
import androidx.room.Room
import com.fitcoach.app.data.FitCoachDatabase
import com.fitcoach.app.data.dao.NutritionDao
import com.fitcoach.app.data.dao.TrackingDao
import com.fitcoach.app.data.dao.UserProfileDao
import com.fitcoach.app.data.dao.WorkoutDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): FitCoachDatabase =
        Room.databaseBuilder(
            context,
            FitCoachDatabase::class.java,
            "fitcoach.db"
        )
            // זמני, לשלב הפיתוח בלבד (ראו הערה ב-FitCoachDatabase.kt) - יוחלף ב-Migration-ים
            // אמיתיים לפני שחרור גרסה שיש בה נתוני משתמשים אמיתיים לשמר.
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideUserProfileDao(database: FitCoachDatabase): UserProfileDao =
        database.userProfileDao()

    @Provides
    fun provideWorkoutDao(database: FitCoachDatabase): WorkoutDao =
        database.workoutDao()

    @Provides
    fun provideNutritionDao(database: FitCoachDatabase): NutritionDao =
        database.nutritionDao()

    @Provides
    fun provideTrackingDao(database: FitCoachDatabase): TrackingDao =
        database.trackingDao()
}
