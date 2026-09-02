package com.fitcoach.app.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

enum class WorkoutPlanKey { A, B }

@Entity(tableName = "exercises")
data class Exercise(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val muscleGroup: String,
    /** הערת תחליף, לדוגמה "אפשרות החלפה: Medicine Ball Chest Pass" */
    val substituteNote: String? = null
)

@Entity(tableName = "workout_plans")
data class WorkoutPlan(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val key: WorkoutPlanKey,
    val name: String
)

@Entity(
    tableName = "planned_exercises",
    foreignKeys = [
        ForeignKey(entity = WorkoutPlan::class, parentColumns = ["id"], childColumns = ["planId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Exercise::class, parentColumns = ["id"], childColumns = ["exerciseId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("planId"), Index("exerciseId")]
)
data class PlannedExercise(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val planId: Long,
    val exerciseId: Long,
    val orderIndex: Int,
    val targetSets: Int,
    val targetRepsMin: Int,
    val targetRepsMax: Int,
    /** לדוגמה "לכל רגל" / "לכל צד" */
    val repsNote: String? = null
)

@Entity(
    tableName = "workout_sessions",
    foreignKeys = [ForeignKey(entity = WorkoutPlan::class, parentColumns = ["id"], childColumns = ["planId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("planId")]
)
data class WorkoutSession(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val planId: Long,
    val dateEpochMillis: Long,
    val completed: Boolean = false,
    val notes: String? = null
)

@Entity(
    tableName = "set_logs",
    foreignKeys = [
        ForeignKey(entity = WorkoutSession::class, parentColumns = ["id"], childColumns = ["sessionId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Exercise::class, parentColumns = ["id"], childColumns = ["exerciseId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("sessionId"), Index("exerciseId")]
)
data class SetLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sessionId: Long,
    val exerciseId: Long,
    val setNumber: Int,
    val weightKg: Float,
    val reps: Int,
    val rpe: Float? = null
)
