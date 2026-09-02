package com.fitcoach.app.ui.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitcoach.app.data.entity.WorkoutSession
import com.fitcoach.app.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class WorkoutHistoryViewModel @Inject constructor(
    repository: WorkoutRepository
) : ViewModel() {

    val sessions: StateFlow<List<WorkoutSession>> = repository.observeSessionHistory()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
}
