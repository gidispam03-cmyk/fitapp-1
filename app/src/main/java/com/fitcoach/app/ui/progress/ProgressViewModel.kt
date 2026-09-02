package com.fitcoach.app.ui.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitcoach.app.data.entity.BodyMeasurement
import com.fitcoach.app.repository.BodyTrackingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ProgressViewModel @Inject constructor(
    repository: BodyTrackingRepository
) : ViewModel() {

    val measurements: StateFlow<List<BodyMeasurement>> = repository.observeMeasurements()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
}
