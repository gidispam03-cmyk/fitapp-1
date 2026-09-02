package com.fitcoach.app.ui.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitcoach.app.repository.BodyTrackingRepository
import com.fitcoach.app.repository.CoachRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeeklyCheckInViewModel @Inject constructor(
    private val bodyTrackingRepository: BodyTrackingRepository,
    private val coachRepository: CoachRepository
) : ViewModel() {

    fun submit(
        avgSleepHours: Float,
        energyLevel: Int,
        hungerLevel: Int,
        hasPain: Boolean,
        painNote: String?,
        missedWorkouts: Int,
        weightsFeltHeavy: Boolean,
        onDone: () -> Unit
    ) {
        viewModelScope.launch {
            bodyTrackingRepository.submitCheckIn(
                avgSleepHours = avgSleepHours,
                energyLevel = energyLevel,
                hungerLevel = hungerLevel,
                hasPain = hasPain,
                painNote = painNote,
                missedWorkouts = missedWorkouts,
                weightsFeltHeavy = weightsFeltHeavy
            )
            coachRepository.runWeeklyAnalysis()
            onDone()
        }
    }
}
