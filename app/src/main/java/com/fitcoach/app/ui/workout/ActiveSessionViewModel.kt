package com.fitcoach.app.ui.workout

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitcoach.app.data.entity.SetLog
import com.fitcoach.app.domain.OverloadRecommendation
import com.fitcoach.app.repository.WorkoutExerciseItem
import com.fitcoach.app.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ExerciseLogState(
    val item: WorkoutExerciseItem,
    val loggedSets: List<SetLog> = emptyList(),
    val recommendation: OverloadRecommendation? = null
)

data class ActiveSessionUiState(
    val isLoading: Boolean = true,
    val sessionId: Long = 0L,
    val planName: String = "",
    val exerciseStates: List<ExerciseLogState> = emptyList(),
    val isFinished: Boolean = false
)

@HiltViewModel
class ActiveSessionViewModel @Inject constructor(
    private val repository: WorkoutRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val sessionId: Long = checkNotNull(savedStateHandle["sessionId"])

    private val _uiState = MutableStateFlow(ActiveSessionUiState(sessionId = sessionId))
    val uiState: StateFlow<ActiveSessionUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    private fun load() {
        viewModelScope.launch {
            val session = repository.getSession(sessionId) ?: return@launch
            val plan = repository.getPlan(session.planId) ?: return@launch
            val items = repository.getPlanExerciseItems(plan.id)
            val states = items.map { ExerciseLogState(item = it) }
            _uiState.value = _uiState.value.copy(isLoading = false, planName = plan.name, exerciseStates = states)
        }
    }

    fun logSet(exerciseId: Long, weightKg: Float, reps: Int, rpe: Float?) {
        viewModelScope.launch {
            val currentState = _uiState.value
            val exerciseState = currentState.exerciseStates.find { it.item.exercise.id == exerciseId } ?: return@launch
            val nextSetNumber = exerciseState.loggedSets.size + 1

            repository.logSet(sessionId, exerciseId, nextSetNumber, weightKg, reps, rpe)

            val updatedSets = repository.getSetLogsOnce(sessionId, exerciseId)
            val recommendation = repository.getOverloadRecommendation(
                exerciseId = exerciseId,
                currentSessionId = sessionId,
                targetRepsMax = exerciseState.item.plannedExercise.targetRepsMax
            )

            val newStates = currentState.exerciseStates.map {
                if (it.item.exercise.id == exerciseId) {
                    it.copy(loggedSets = updatedSets, recommendation = recommendation)
                } else it
            }
            _uiState.value = currentState.copy(exerciseStates = newStates)
        }
    }

    fun finishSession() {
        viewModelScope.launch {
            repository.completeSession(sessionId)
            _uiState.value = _uiState.value.copy(isFinished = true)
        }
    }
}
