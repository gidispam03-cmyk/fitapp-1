package com.fitcoach.app.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitcoach.app.data.entity.ActivityLevel
import com.fitcoach.app.data.entity.Goal
import com.fitcoach.app.data.entity.Sex
import com.fitcoach.app.data.entity.TrainingExperience
import com.fitcoach.app.data.entity.UserProfile
import com.fitcoach.app.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * מצב הטופס בזמן מילוי ה-Onboarding.
 * שדות טקסט (String) כדי לאפשר קלט חופשי במקלדת לפני ולידציה,
 * וכדי לא לקרוס על ערכים ריקים/חלקיים תוך כדי הקלדה.
 */
data class OnboardingFormState(
    val age: String = "",
    val heightCm: String = "",
    val weightKg: String = "",
    val sex: Sex = Sex.MALE,
    val bodyFatPercent: String = "",
    val activityLevel: ActivityLevel = ActivityLevel.MEDIUM,
    val workoutsPerWeek: String = "3",
    val avgSleepHours: String = "7",
    val trainingExperience: TrainingExperience = TrainingExperience.BEGINNER,
    val primaryGoal: Goal = Goal.MUSCLE_GAIN,
    val currentStep: Int = 0,
    val errorMessage: String? = null
) {
    val isValid: Boolean
        get() = age.toIntOrNull() != null &&
            heightCm.toIntOrNull() != null &&
            weightKg.toFloatOrNull() != null &&
            workoutsPerWeek.toIntOrNull() != null &&
            avgSleepHours.toFloatOrNull() != null
}

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _formState = MutableStateFlow(OnboardingFormState())
    val formState: StateFlow<OnboardingFormState> = _formState

    /**
     * null = עוד לא ידוע (טוען מה-DB), true/false = ידוע.
     * מגיע ישירות מה-DB דרך Flow כדי שאם המשתמש כבר קיים, נדלג ישר ל-Dashboard.
     */
    val isOnboardingCompleted: StateFlow<Boolean?> = userRepository.observeProfile()
        .map { it?.onboardingCompleted == true }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun updateAge(value: String) = update { copy(age = value.filter { c -> c.isDigit() }) }
    fun updateHeight(value: String) = update { copy(heightCm = value.filter { c -> c.isDigit() }) }
    fun updateWeight(value: String) = update { copy(weightKg = value.filter { c -> c.isDigit() || c == '.' }) }
    fun updateBodyFat(value: String) = update { copy(bodyFatPercent = value.filter { c -> c.isDigit() || c == '.' }) }
    fun updateSex(value: Sex) = update { copy(sex = value) }
    fun updateActivityLevel(value: ActivityLevel) = update { copy(activityLevel = value) }
    fun updateWorkoutsPerWeek(value: String) = update { copy(workoutsPerWeek = value.filter { c -> c.isDigit() }) }
    fun updateSleepHours(value: String) = update { copy(avgSleepHours = value.filter { c -> c.isDigit() || c == '.' }) }
    fun updateExperience(value: TrainingExperience) = update { copy(trainingExperience = value) }
    fun updateGoal(value: Goal) = update { copy(primaryGoal = value) }

    fun nextStep() = update { copy(currentStep = currentStep + 1, errorMessage = null) }
    fun previousStep() = update { copy(currentStep = (currentStep - 1).coerceAtLeast(0), errorMessage = null) }

    fun submit() {
        val state = _formState.value
        if (!state.isValid) {
            update { copy(errorMessage = "נא למלא את כל השדות הנדרשים בצורה תקינה") }
            return
        }

        viewModelScope.launch {
            val profile = UserProfile(
                age = state.age.toInt(),
                heightCm = state.heightCm.toInt(),
                weightKg = state.weightKg.toFloat(),
                sex = state.sex,
                bodyFatPercent = state.bodyFatPercent.toFloatOrNull(),
                activityLevel = state.activityLevel,
                workoutsPerWeek = state.workoutsPerWeek.toInt(),
                avgSleepHours = state.avgSleepHours.toFloat(),
                trainingExperience = state.trainingExperience,
                primaryGoal = state.primaryGoal,
                onboardingCompleted = true
            )
            userRepository.saveProfile(profile)
            // isOnboardingCompleted יתעדכן אוטומטית דרך ה-Flow שמקורו ב-DB
        }
    }

    private inline fun update(block: OnboardingFormState.() -> OnboardingFormState) {
        _formState.value = _formState.value.block()
    }
}
