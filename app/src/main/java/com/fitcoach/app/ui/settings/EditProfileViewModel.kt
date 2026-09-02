package com.fitcoach.app.ui.settings

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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EditProfileState(
    val isLoading: Boolean = true,
    val age: String = "",
    val heightCm: String = "",
    val weightKg: String = "",
    val sex: Sex = Sex.MALE,
    val activityLevel: ActivityLevel = ActivityLevel.MEDIUM,
    val workoutsPerWeek: String = "",
    val avgSleepHours: String = "",
    val trainingExperience: TrainingExperience = TrainingExperience.BEGINNER,
    val primaryGoal: Goal = Goal.MUSCLE_GAIN,
    val errorMessage: String? = null,
    val saved: Boolean = false
)

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(EditProfileState())
    val state: StateFlow<EditProfileState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val profile = userRepository.getProfileOnce()
            _state.value = if (profile != null) {
                EditProfileState(
                    isLoading = false,
                    age = profile.age.toString(),
                    heightCm = profile.heightCm.toString(),
                    weightKg = profile.weightKg.toString(),
                    sex = profile.sex,
                    activityLevel = profile.activityLevel,
                    workoutsPerWeek = profile.workoutsPerWeek.toString(),
                    avgSleepHours = profile.avgSleepHours.toString(),
                    trainingExperience = profile.trainingExperience,
                    primaryGoal = profile.primaryGoal
                )
            } else {
                _state.value.copy(isLoading = false)
            }
        }
    }

    fun updateAge(v: String) = update { copy(age = v.filter { c -> c.isDigit() }) }
    fun updateHeight(v: String) = update { copy(heightCm = v.filter { c -> c.isDigit() }) }
    fun updateWeight(v: String) = update { copy(weightKg = v.filter { c -> c.isDigit() || c == '.' }) }
    fun updateSex(v: Sex) = update { copy(sex = v) }
    fun updateActivityLevel(v: ActivityLevel) = update { copy(activityLevel = v) }
    fun updateWorkoutsPerWeek(v: String) = update { copy(workoutsPerWeek = v.filter { c -> c.isDigit() }) }
    fun updateSleepHours(v: String) = update { copy(avgSleepHours = v.filter { c -> c.isDigit() || c == '.' }) }
    fun updateExperience(v: TrainingExperience) = update { copy(trainingExperience = v) }
    fun updateGoal(v: Goal) = update { copy(primaryGoal = v) }

    fun save() {
        val s = _state.value
        val age = s.age.toIntOrNull()
        val height = s.heightCm.toIntOrNull()
        val weight = s.weightKg.toFloatOrNull()
        val workouts = s.workoutsPerWeek.toIntOrNull()
        val sleep = s.avgSleepHours.toFloatOrNull()

        if (age == null || height == null || weight == null || workouts == null || sleep == null) {
            _state.value = s.copy(errorMessage = "נא למלא את כל השדות בצורה תקינה")
            return
        }

        viewModelScope.launch {
            userRepository.saveProfile(
                UserProfile(
                    age = age,
                    heightCm = height,
                    weightKg = weight,
                    sex = s.sex,
                    activityLevel = s.activityLevel,
                    workoutsPerWeek = workouts,
                    avgSleepHours = sleep,
                    trainingExperience = s.trainingExperience,
                    primaryGoal = s.primaryGoal,
                    onboardingCompleted = true
                )
            )
            _state.value = _state.value.copy(saved = true)
        }
    }

    private inline fun update(block: EditProfileState.() -> EditProfileState) {
        _state.value = _state.value.block()
    }
}
