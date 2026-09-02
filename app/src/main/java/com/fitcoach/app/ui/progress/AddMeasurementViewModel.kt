package com.fitcoach.app.ui.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitcoach.app.repository.BodyTrackingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddMeasurementViewModel @Inject constructor(
    private val repository: BodyTrackingRepository
) : ViewModel() {

    fun save(
        weightKg: Float,
        waistCm: Float?,
        chestCm: Float?,
        armCm: Float?,
        thighCm: Float?,
        bodyFatPercent: Float?,
        onDone: () -> Unit
    ) {
        viewModelScope.launch {
            repository.logMeasurement(weightKg, waistCm, chestCm, armCm, thighCm, bodyFatPercent)
            onDone()
        }
    }
}
