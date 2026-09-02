package com.fitcoach.app.ui.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitcoach.app.data.entity.Recommendation
import com.fitcoach.app.repository.CoachRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class RecommendationsViewModel @Inject constructor(
    repository: CoachRepository
) : ViewModel() {

    val recommendations: StateFlow<List<Recommendation>> = repository.observeRecommendations()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
}
