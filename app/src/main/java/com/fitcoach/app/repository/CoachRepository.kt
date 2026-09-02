package com.fitcoach.app.repository

import com.fitcoach.app.data.dao.TrackingDao
import com.fitcoach.app.data.entity.Recommendation
import com.fitcoach.app.domain.WeeklyAnalysisInput
import com.fitcoach.app.domain.WeeklyAnalysisUseCase
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CoachRepository @Inject constructor(
    private val trackingDao: TrackingDao,
    private val userRepository: UserRepository,
    private val workoutRepository: WorkoutRepository,
    private val nutritionRepository: NutritionRepository,
    private val bodyTrackingRepository: BodyTrackingRepository
) {

    fun observeRecommendations(): Flow<List<Recommendation>> = trackingDao.observeRecommendations()

    fun observeLatestRecommendation(): Flow<Recommendation?> = trackingDao.observeLatestRecommendation()

    /**
     * מריץ ניתוח שבועי מלא ושומר את ההמלצות שהתקבלו.
     * נקרא לאחר שליחת Check-in שבועי, אך אפשר גם להריץ בכל נקודה אחרת בעתיד (למשל תזמון אוטומטי).
     */
    suspend fun runWeeklyAnalysis() {
        val profile = userRepository.getProfileOnce() ?: return
        val targets = nutritionRepository.calculateTargets(profile)

        val today = LocalDate.now()
        val measurementsWindowStart = today.minusWeeks(3).toEpochDay()
        val measurements = bodyTrackingRepository.getMeasurementsSince(measurementsWindowStart)

        val weekStart = today.minusDays(6)
        val weekStartEpochDay = weekStart.toEpochDay()
        val weekStartEpochMillis = weekStart.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

        val weekEntries = nutritionRepository.getEntriesSince(weekStartEpochDay)
        val daysWithEntries = weekEntries.map { it.dateEpochDay }.distinct().size.coerceAtLeast(1)
        val avgCalories = if (weekEntries.isEmpty()) null else weekEntries.sumOf { it.calories } / daysWithEntries
        val avgProtein = if (weekEntries.isEmpty()) null else weekEntries.sumOf { it.proteinG } / daysWithEntries

        val completedSessions = workoutRepository.getCompletedSessionsSince(weekStartEpochMillis)
        val latestCheckIn = bodyTrackingRepository.getLatestCheckIn()

        val input = WeeklyAnalysisInput(
            goal = profile.primaryGoal,
            measurements = measurements,
            targetWorkoutsPerWeek = profile.workoutsPerWeek,
            completedWorkoutsThisWeek = completedSessions.size,
            avgDailyCaloriesThisWeek = avgCalories,
            targetCalories = targets.calories,
            avgDailyProteinThisWeek = avgProtein,
            targetProteinG = targets.proteinG,
            latestCheckIn = latestCheckIn
        )

        val generated = WeeklyAnalysisUseCase.analyze(input)
        val now = System.currentTimeMillis()
        val recommendations = generated.map {
            Recommendation(dateEpochMillis = now, type = it.type, message = it.message)
        }
        trackingDao.insertRecommendations(recommendations)
    }
}
