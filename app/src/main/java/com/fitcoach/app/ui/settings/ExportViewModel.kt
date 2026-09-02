package com.fitcoach.app.ui.settings

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitcoach.app.domain.ExportFormatter
import com.fitcoach.app.domain.WorkoutSessionExportRow
import com.fitcoach.app.repository.BodyTrackingRepository
import com.fitcoach.app.repository.NutritionRepository
import com.fitcoach.app.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ExportViewModel @Inject constructor(
    private val bodyTrackingRepository: BodyTrackingRepository,
    private val nutritionRepository: NutritionRepository,
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    /**
     * אוסף את כל נתוני המשתמש (מדידות, ארוחות, אימונים), כותב אותם לקובץ טקסט
     * זמני בתיקיית ה-cache של האפליקציה, ומחזיר Uri (דרך FileProvider) שאפשר
     * להעביר ל-Intent.ACTION_SEND כדי לשתף/לשמור את הקובץ.
     */
    fun exportToFile(context: Context, onReady: (Uri) -> Unit) {
        viewModelScope.launch {
            val measurements = bodyTrackingRepository.getMeasurementsSince(0L)
            val mealEntries = nutritionRepository.getEntriesSince(0L)
            val sessions = workoutRepository.getCompletedSessionsSince(0L)

            val sessionRows = sessions.map { session ->
                val planName = workoutRepository.getPlan(session.planId)?.name ?: "לא ידוע"
                WorkoutSessionExportRow(
                    dateEpochMillis = session.dateEpochMillis,
                    planName = planName,
                    completed = session.completed
                )
            }

            val text = ExportFormatter.buildExportText(measurements, mealEntries, sessionRows)

            val uri = withContext(Dispatchers.IO) {
                val exportDir = File(context.cacheDir, "exports").apply { mkdirs() }
                val file = File(exportDir, "fitcoach_export.csv")
                file.writeText(text)
                FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
            }

            onReady(uri)
        }
    }
}
