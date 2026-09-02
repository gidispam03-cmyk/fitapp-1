package com.fitcoach.app.repository

import com.fitcoach.app.data.dao.TrackingDao
import com.fitcoach.app.data.entity.BodyMeasurement
import com.fitcoach.app.data.entity.WeeklyCheckIn
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BodyTrackingRepository @Inject constructor(
    private val dao: TrackingDao
) {

    fun observeMeasurements(): Flow<List<BodyMeasurement>> = dao.observeMeasurements()

    suspend fun getMeasurementsSince(sinceEpochDay: Long): List<BodyMeasurement> =
        dao.getMeasurementsSince(sinceEpochDay)

    suspend fun getLatestMeasurement(): BodyMeasurement? = dao.getLatestMeasurement()

    suspend fun logMeasurement(
        weightKg: Float,
        waistCm: Float?,
        chestCm: Float?,
        armCm: Float?,
        thighCm: Float?,
        bodyFatPercent: Float?
    ) {
        dao.insertMeasurement(
            BodyMeasurement(
                dateEpochDay = LocalDate.now().toEpochDay(),
                weightKg = weightKg,
                waistCm = waistCm,
                chestCm = chestCm,
                armCm = armCm,
                thighCm = thighCm,
                bodyFatPercent = bodyFatPercent
            )
        )
    }

    fun observeCheckIns(): Flow<List<WeeklyCheckIn>> = dao.observeCheckIns()

    suspend fun getLatestCheckIn(): WeeklyCheckIn? = dao.getLatestCheckIn()

    suspend fun submitCheckIn(
        avgSleepHours: Float,
        energyLevel: Int,
        hungerLevel: Int,
        hasPain: Boolean,
        painNote: String?,
        missedWorkouts: Int,
        weightsFeltHeavy: Boolean
    ) {
        dao.insertCheckIn(
            WeeklyCheckIn(
                weekStartEpochDay = LocalDate.now().toEpochDay(),
                avgSleepHours = avgSleepHours,
                energyLevel = energyLevel,
                hungerLevel = hungerLevel,
                hasPain = hasPain,
                painNote = painNote,
                missedWorkouts = missedWorkouts,
                weightsFeltHeavy = weightsFeltHeavy
            )
        )
    }
}
