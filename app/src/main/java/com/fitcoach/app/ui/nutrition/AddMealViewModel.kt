package com.fitcoach.app.ui.nutrition

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitcoach.app.data.entity.MealTemplate
import com.fitcoach.app.data.entity.MealType
import com.fitcoach.app.repository.NutritionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AddMealViewModel @Inject constructor(
    private val repository: NutritionRepository
) : ViewModel() {

    val templates: StateFlow<List<MealTemplate>> = repository.observeAllTemplates()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private val todayEpochDay = LocalDate.now().toEpochDay()

    fun logTemplate(template: MealTemplate, onDone: () -> Unit) {
        viewModelScope.launch {
            repository.logMealFromTemplate(todayEpochDay, template)
            onDone()
        }
    }

    fun logCustom(
        mealType: MealType,
        name: String,
        calories: Int,
        protein: Int,
        carbs: Int,
        fat: Int,
        onDone: () -> Unit
    ) {
        viewModelScope.launch {
            repository.logCustomMeal(todayEpochDay, mealType, name, calories, protein, carbs, fat)
            onDone()
        }
    }
}
