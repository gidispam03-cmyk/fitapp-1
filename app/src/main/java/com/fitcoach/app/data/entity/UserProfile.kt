package com.fitcoach.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class Sex { MALE, FEMALE }

enum class ActivityLevel { LOW, MEDIUM, HIGH }

enum class TrainingExperience { BEGINNER, INTERMEDIATE, ADVANCED }

enum class Goal {
    MUSCLE_GAIN,
    FAT_LOSS,
    MAINTENANCE,
    STRENGTH,
    ATHLETICISM
}

enum class DietaryPreference { NONE, KOSHER_NO_PORK, KOSHER_FULL }

/**
 * פרופיל המשתמש היחיד באפליקציה (אפליקציה לשימוש אישי, לכן שורה בודדת מספיקה
 * בשלב זה - id קבוע כדי לפשט שאילתות, ניתן להרחיב למשתמשים מרובים בעתיד).
 */
@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1,
    val age: Int,
    val heightCm: Int,
    val weightKg: Float,
    val sex: Sex,
    val bodyFatPercent: Float? = null,
    val activityLevel: ActivityLevel,
    val workoutsPerWeek: Int,
    val avgSleepHours: Float,
    val trainingExperience: TrainingExperience,
    val primaryGoal: Goal,
    val dietaryPreference: DietaryPreference = DietaryPreference.NONE,
    val onboardingCompleted: Boolean = false
)
