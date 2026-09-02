package com.fitcoach.app.ui.nutrition

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitcoach.app.data.entity.MealEntry
import com.fitcoach.app.domain.NutritionTargets
import com.fitcoach.app.repository.NutritionRepository
import com.fitcoach.app.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class NutritionUiState(
    val isLoading: Boolean = true,
    val targets: NutritionTargets? = null,
    val entries: List<MealEntry> = emptyList(),
    val consumedCalories: Int = 0,
    val consumedProtein: Int = 0,
    val consumedCarbs: Int = 0,
    val consumedFat: Int = 0
)

@HiltViewModel
class NutritionViewModel @Inject constructor(
    private val nutritionRepository: NutritionRepository,
    userRepository: UserRepository
) : ViewModel() {

    private val todayEpochDay = LocalDate.now().toEpochDay()

    val uiState: StateFlow<NutritionUiState> = combine(
        userRepository.observeProfile(),
        nutritionRepository.observeEntriesForDay(todayEpochDay)
    ) { profile, entries ->
        NutritionUiState(
            isLoading = false,
            targets = profile?.let { nutritionRepository.calculateTargets(it) },
            entries = entries,
            consumedCalories = entries.sumOf { it.calories },
            consumedProtein = entries.sumOf { it.proteinG },
            consumedCarbs = entries.sumOf { it.carbsG },
            consumedFat = entries.sumOf { it.fatG }
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, NutritionUiState())

    fun deleteEntry(entry: MealEntry) {
        viewModelScope.launch { nutritionRepository.deleteEntry(entry) }
    }
}
