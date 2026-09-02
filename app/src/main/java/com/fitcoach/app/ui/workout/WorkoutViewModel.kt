package com.fitcoach.app.ui.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitcoach.app.data.entity.WorkoutPlanKey
import com.fitcoach.app.repository.WorkoutExerciseItem
import com.fitcoach.app.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WorkoutUiState(
    val isLoading: Boolean = true,
    val planId: Long = 0L,
    val planName: String = "",
    val planKey: WorkoutPlanKey? = null,
    val exercises: List<WorkoutExerciseItem> = emptyList()
)

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val repository: WorkoutRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorkoutUiState())
    val uiState: StateFlow<WorkoutUiState> = _uiState.asStateFlow()

    init {
        loadTodayPlan()
    }

    fun loadTodayPlan() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val planKey = repository.determineTodayPlanKey()

            var result = repository.getPlanExercises(planKey)
            var attempts = 0
            // הזריעה הראשונית רצה ברקע מ-Application.onCreate; אם המסך נטען ממש
            // באותו רגע, ייתכן ועוד לא הסתיימה - ממתינים קצת ומנסים שוב.
            while (result == null && attempts < 5) {
                delay(200)
                result = repository.getPlanExercises(planKey)
                attempts++
            }

            _uiState.value = if (result != null) {
                val (plan, items) = result
                WorkoutUiState(
                    isLoading = false,
                    planId = plan.id,
                    planName = plan.name,
                    planKey = plan.key,
                    exercises = items
                )
            } else {
                _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun startSession(onStarted: (Long) -> Unit) {
        val planId = _uiState.value.planId
        if (planId == 0L) return
        viewModelScope.launch {
            val sessionId = repository.startSession(planId)
            onStarted(sessionId)
        }
    }
}
