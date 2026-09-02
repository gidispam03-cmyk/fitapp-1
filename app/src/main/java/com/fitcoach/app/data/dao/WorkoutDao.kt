package com.fitcoach.app.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.fitcoach.app.data.entity.Exercise
import com.fitcoach.app.data.entity.PlannedExercise
import com.fitcoach.app.data.entity.SetLog
import com.fitcoach.app.data.entity.WorkoutPlan
import com.fitcoach.app.data.entity.WorkoutPlanKey
import com.fitcoach.app.data.entity.WorkoutSession
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {

    // --- Exercises ---
    @Insert
    suspend fun insertExercises(exercises: List<Exercise>)

    @Query("SELECT * FROM exercises")
    suspend fun getAllExercises(): List<Exercise>

    @Query("SELECT * FROM exercises WHERE id IN (:ids)")
    suspend fun getExercisesByIds(ids: List<Long>): List<Exercise>

    @Query("SELECT COUNT(*) FROM exercises")
    suspend fun exerciseCount(): Int

    // --- Plans ---
    @Insert
    suspend fun insertPlans(plans: List<WorkoutPlan>): List<Long>

    @Query("SELECT * FROM workout_plans WHERE `key` = :key LIMIT 1")
    suspend fun getPlanByKey(key: WorkoutPlanKey): WorkoutPlan?

    @Query("SELECT * FROM workout_plans WHERE id = :id LIMIT 1")
    suspend fun getPlanById(id: Long): WorkoutPlan?

    // --- Planned exercises ---
    @Insert
    suspend fun insertPlannedExercises(items: List<PlannedExercise>)

    @Query("SELECT * FROM planned_exercises WHERE planId = :planId ORDER BY orderIndex ASC")
    suspend fun getPlannedExercisesForPlanOnce(planId: Long): List<PlannedExercise>

    // --- Sessions ---
    @Insert
    suspend fun insertSession(session: WorkoutSession): Long

    @Update
    suspend fun updateSession(session: WorkoutSession)

    @Query("SELECT * FROM workout_sessions WHERE id = :id LIMIT 1")
    suspend fun getSessionById(id: Long): WorkoutSession?

    @Query("SELECT * FROM workout_sessions ORDER BY dateEpochMillis DESC LIMIT 1")
    suspend fun getLastSession(): WorkoutSession?

    @Query("SELECT * FROM workout_sessions WHERE completed = 1 ORDER BY dateEpochMillis DESC")
    suspend fun getCompletedSessionsDesc(): List<WorkoutSession>

    @Query("SELECT * FROM workout_sessions WHERE completed = 1 AND dateEpochMillis >= :sinceEpochMillis ORDER BY dateEpochMillis DESC")
    suspend fun getCompletedSessionsSince(sinceEpochMillis: Long): List<WorkoutSession>

    @Query("SELECT * FROM workout_sessions WHERE completed = 1 ORDER BY dateEpochMillis DESC")
    fun observeSessionHistory(): Flow<List<WorkoutSession>>

    // --- Set logs ---
    @Insert
    suspend fun insertSetLog(setLog: SetLog): Long

    @Query("SELECT * FROM set_logs WHERE sessionId = :sessionId ORDER BY exerciseId, setNumber")
    fun observeSetLogsForSession(sessionId: Long): Flow<List<SetLog>>

    @Query("SELECT * FROM set_logs WHERE sessionId = :sessionId AND exerciseId = :exerciseId ORDER BY setNumber")
    suspend fun getSetLogsForSessionAndExercise(sessionId: Long, exerciseId: Long): List<SetLog>
}
