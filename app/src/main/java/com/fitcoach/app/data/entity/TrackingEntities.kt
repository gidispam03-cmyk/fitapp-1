package com.fitcoach.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "body_measurements")
data class BodyMeasurement(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dateEpochDay: Long,
    val weightKg: Float,
    val waistCm: Float? = null,
    val chestCm: Float? = null,
    val armCm: Float? = null,
    val thighCm: Float? = null,
    val bodyFatPercent: Float? = null
)

@Entity(tableName = "weekly_checkins")
data class WeeklyCheckIn(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val weekStartEpochDay: Long,
    val avgSleepHours: Float,
    /** 1 (נמוכה) עד 5 (גבוהה) */
    val energyLevel: Int,
    /** 1 (נמוך) עד 5 (גבוה) */
    val hungerLevel: Int,
    val hasPain: Boolean,
    val painNote: String? = null,
    val missedWorkouts: Int,
    val weightsFeltHeavy: Boolean
)

enum class RecommendationType { NUTRITION, WORKOUT, RECOVERY, GENERAL }

@Entity(tableName = "recommendations")
data class Recommendation(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dateEpochMillis: Long,
    val type: RecommendationType,
    val message: String,
    val isRead: Boolean = false
)
