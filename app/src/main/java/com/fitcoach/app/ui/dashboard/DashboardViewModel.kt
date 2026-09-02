package com.fitcoach.app.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitcoach.app.data.entity.Recommendation
import com.fitcoach.app.data.entity.UserProfile
import com.fitcoach.app.domain.NutritionTargets
import com.fitcoach.app.repository.BodyTrackingRepository
import com.fitcoach.app.repository.CoachRepository
import com.fitcoach.app.repository.NutritionRepository
import com.fitcoach.app.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import javax.inject.Inject

data class DashboardUiState(
    val profile: UserProfile? = null,
    val targets: NutritionTargets? = null,
    val consumedCalories: Int = 0,
    val consumedProtein: Int = 0,
    val latestRecommendation: Recommendation? = null,
    val checkInDue: Boolean = false
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    userRepository: UserRepository,
    nutritionRepository: NutritionRepository,
    coachRepository: CoachRepository,
    bodyTrackingRepository: BodyTrackingRepository
) : ViewModel() {

    private val todayEpochDay = LocalDate.now().toEpochDay()

    val uiState: StateFlow<DashboardUiState> = combine(
        userRepository.observeProfile(),
        nutritionRepository.observeEntriesForDay(todayEpochDay),
        coachRepository.observeLatestRecommendation(),
        bodyTrackingRepository.observeCheckIns()
    ) { profile, entries, latestRecommendation, checkIns ->
        val lastCheckInWeekStart = checkIns.firstOrNull()?.weekStartEpochDay
        val checkInDue = lastCheckInWeekStart == null || (todayEpochDay - lastCheckInWeekStart) >= 7

        DashboardUiState(
            profile = profile,
            targets = profile?.let { nutritionRepository.calculateTargets(it) },
            consumedCalories = entries.sumOf { it.calories },
            consumedProtein = entries.sumOf { it.proteinG },
            latestRecommendation = latestRecommendation,
            checkInDue = checkInDue
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, DashboardUiState())
}
