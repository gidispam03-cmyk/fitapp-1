package com.fitcoach.app

import android.app.Application
import com.fitcoach.app.repository.NutritionRepository
import com.fitcoach.app.repository.WorkoutRepository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class FitCoachApplication : Application() {

    @Inject lateinit var workoutRepository: WorkoutRepository
    @Inject lateinit var nutritionRepository: NutritionRepository

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        // זריעה אידמפוטנטית - רצה על כל עלייה, אך רק מוסיפה נתונים אם הטבלאות ריקות.
        applicationScope.launch {
            workoutRepository.seedDefaultPlansIfNeeded()
            nutritionRepository.seedIfNeeded()
        }
    }
}
