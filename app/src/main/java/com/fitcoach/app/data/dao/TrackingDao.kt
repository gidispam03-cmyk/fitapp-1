package com.fitcoach.app.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.fitcoach.app.data.entity.BodyMeasurement
import com.fitcoach.app.data.entity.Recommendation
import com.fitcoach.app.data.entity.WeeklyCheckIn
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackingDao {

    // --- Body measurements ---
    @Insert
    suspend fun insertMeasurement(measurement: BodyMeasurement): Long

    @Query("SELECT * FROM body_measurements ORDER BY dateEpochDay DESC")
    fun observeMeasurements(): Flow<List<BodyMeasurement>>

    @Query("SELECT * FROM body_measurements WHERE dateEpochDay >= :sinceEpochDay ORDER BY dateEpochDay ASC")
    suspend fun getMeasurementsSince(sinceEpochDay: Long): List<BodyMeasurement>

    @Query("SELECT * FROM body_measurements ORDER BY dateEpochDay DESC LIMIT 1")
    suspend fun getLatestMeasurement(): BodyMeasurement?

    // --- Weekly check-ins ---
    @Insert
    suspend fun insertCheckIn(checkIn: WeeklyCheckIn): Long

    @Query("SELECT * FROM weekly_checkins ORDER BY weekStartEpochDay DESC LIMIT 1")
    suspend fun getLatestCheckIn(): WeeklyCheckIn?

    @Query("SELECT * FROM weekly_checkins ORDER BY weekStartEpochDay DESC")
    fun observeCheckIns(): Flow<List<WeeklyCheckIn>>

    // --- Recommendations ---
    @Insert
    suspend fun insertRecommendations(recommendations: List<Recommendation>)

    @Query("SELECT * FROM recommendations ORDER BY dateEpochMillis DESC")
    fun observeRecommendations(): Flow<List<Recommendation>>

    @Query("SELECT * FROM recommendations ORDER BY dateEpochMillis DESC LIMIT 1")
    fun observeLatestRecommendation(): Flow<Recommendation?>

    @Update
    suspend fun updateRecommendation(recommendation: Recommendation)
}
