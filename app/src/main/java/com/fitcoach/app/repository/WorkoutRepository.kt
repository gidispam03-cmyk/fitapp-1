package com.fitcoach.app.repository

import com.fitcoach.app.data.WorkoutSeedData
import com.fitcoach.app.data.dao.WorkoutDao
import com.fitcoach.app.data.entity.Exercise
import com.fitcoach.app.data.entity.PlannedExercise
import com.fitcoach.app.data.entity.SetLog
import com.fitcoach.app.data.entity.WorkoutPlan
import com.fitcoach.app.data.entity.WorkoutPlanKey
import com.fitcoach.app.data.entity.WorkoutSession
import com.fitcoach.app.domain.OverloadRecommendation
import com.fitcoach.app.domain.ProgressiveOverloadUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

data class WorkoutExerciseItem(
    val plannedExercise: PlannedExercise,
    val exercise: Exercise
)

@Singleton
class WorkoutRepository @Inject constructor(
    private val dao: WorkoutDao
) {

    /** מוזין פעם אחת בלבד, בעליית האפליקציה הראשונה - בדיקה אידמפוטנטית. */
    suspend fun seedDefaultPlansIfNeeded() {
        if (dao.exerciseCount() > 0) return

        val exercises = WorkoutSeedData.exerciseSeeds.map {
            Exercise(name = it.name, muscleGroup = it.muscleGroup, substituteNote = it.substituteNote)
        }
        dao.insertExercises(exercises)
        val byName = dao.getAllExercises().associateBy { it.name }

        val planA = WorkoutPlan(key = WorkoutPlanKey.A, name = WorkoutSeedData.planKeyName(WorkoutPlanKey.A))
        val planB = WorkoutPlan(key = WorkoutPlanKey.B, name = WorkoutSeedData.planKeyName(WorkoutPlanKey.B))
        val planIds = dao.insertPlans(listOf(planA, planB))

        val plannedA = WorkoutSeedData.toPlannedExercises(planIds[0], WorkoutSeedData.planAItems, byName)
        val plannedB = WorkoutSeedData.toPlannedExercises(planIds[1], WorkoutSeedData.planBItems, byName)
        dao.insertPlannedExercises(plannedA + plannedB)
    }

    /** תוכניות A/B מתחלפות: אם האימון האחרון היה A, היום B, ולהפך. אין היסטוריה -> מתחילים ב-A. */
    suspend fun determineTodayPlanKey(): WorkoutPlanKey {
        val lastSession = dao.getLastSession() ?: return WorkoutPlanKey.A
        val lastPlan = dao.getPlanById(lastSession.planId)
        return if (lastPlan?.key == WorkoutPlanKey.A) WorkoutPlanKey.B else WorkoutPlanKey.A
    }

    suspend fun getPlanExerciseItems(planId: Long): List<WorkoutExerciseItem> {
        val planned = dao.getPlannedExercisesForPlanOnce(planId)
        val exercises = dao.getExercisesByIds(planned.map { it.exerciseId }).associateBy { it.id }
        return planned.mapNotNull { pe -> exercises[pe.exerciseId]?.let { WorkoutExerciseItem(pe, it) } }
    }

    suspend fun getPlanExercises(planKey: WorkoutPlanKey): Pair<WorkoutPlan, List<WorkoutExerciseItem>>? {
        val plan = dao.getPlanByKey(planKey) ?: return null
        return plan to getPlanExerciseItems(plan.id)
    }

    suspend fun getSession(sessionId: Long): WorkoutSession? = dao.getSessionById(sessionId)

    suspend fun getPlan(planId: Long): WorkoutPlan? = dao.getPlanById(planId)

    suspend fun startSession(planId: Long): Long =
        dao.insertSession(WorkoutSession(planId = planId, dateEpochMillis = System.currentTimeMillis()))

    suspend fun logSet(sessionId: Long, exerciseId: Long, setNumber: Int, weightKg: Float, reps: Int, rpe: Float?) {
        dao.insertSetLog(
            SetLog(
                sessionId = sessionId,
                exerciseId = exerciseId,
                setNumber = setNumber,
                weightKg = weightKg,
                reps = reps,
                rpe = rpe
            )
        )
    }

    suspend fun getSetLogsOnce(sessionId: Long, exerciseId: Long): List<SetLog> =
        dao.getSetLogsForSessionAndExercise(sessionId, exerciseId)

    suspend fun completeSession(sessionId: Long) {
        dao.getSessionById(sessionId)?.let { dao.updateSession(it.copy(completed = true)) }
    }

    fun observeSetLogsForSession(sessionId: Long): Flow<List<SetLog>> = dao.observeSetLogsForSession(sessionId)

    fun observeSessionHistory(): Flow<List<WorkoutSession>> = dao.observeSessionHistory()

    suspend fun getCompletedSessionsSince(sinceEpochMillis: Long): List<WorkoutSession> =
        dao.getCompletedSessionsSince(sinceEpochMillis)

    suspend fun getOverloadRecommendation(
        exerciseId: Long,
        currentSessionId: Long,
        targetRepsMax: Int
    ): OverloadRecommendation {
        val currentSets = dao.getSetLogsForSessionAndExercise(currentSessionId, exerciseId)
        val previousSets = findPreviousCompletedSets(exerciseId, excludeSessionId = currentSessionId)
        return ProgressiveOverloadUseCase.evaluate(currentSets, targetRepsMax, previousSets)
    }

    /** מחפש את הפעם האחרונה שבה בוצע תרגיל זה, בסשן שהושלם, לצורך השוואת ביצועים. */
    private suspend fun findPreviousCompletedSets(exerciseId: Long, excludeSessionId: Long): List<SetLog> {
        val sessions = dao.getCompletedSessionsDesc()
        for (session in sessions) {
            if (session.id == excludeSessionId) continue
            val sets = dao.getSetLogsForSessionAndExercise(session.id, exerciseId)
            if (sets.isNotEmpty()) return sets
        }
        return emptyList()
    }
}
