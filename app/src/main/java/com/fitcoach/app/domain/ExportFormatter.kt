package com.fitcoach.app.domain

import com.fitcoach.app.data.entity.BodyMeasurement
import com.fitcoach.app.data.entity.MealEntry
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

data class WorkoutSessionExportRow(
    val dateEpochMillis: Long,
    val planName: String,
    val completed: Boolean
)

/**
 * מפיק קובץ טקסט (CSV קריא, בעברית) מנתוני המשתמש - מדידות גוף, ארוחות, ואימונים.
 * לוגיקה טהורה, ללא תלות ב-Android, כדי שאפשר לבדוק אותה בנפרד מהאפליקציה עצמה.
 */
object ExportFormatter {

    fun buildExportText(
        measurements: List<BodyMeasurement>,
        mealEntries: List<MealEntry>,
        sessions: List<WorkoutSessionExportRow>
    ): String {
        val sb = StringBuilder()

        sb.appendLine("=== מדידות גוף ===")
        sb.appendLine("תאריך,משקל(ק\"ג),מותניים(ס\"מ),חזה(ס\"מ),יד(ס\"מ),ירך(ס\"מ),אחוז שומן")
        measurements.forEach { m ->
            sb.appendLine(
                "${LocalDate.ofEpochDay(m.dateEpochDay)},${m.weightKg},${m.waistCm ?: ""}," +
                    "${m.chestCm ?: ""},${m.armCm ?: ""},${m.thighCm ?: ""},${m.bodyFatPercent ?: ""}"
            )
        }

        sb.appendLine()
        sb.appendLine("=== ארוחות ===")
        sb.appendLine("תאריך,סוג ארוחה,שם,קלוריות,חלבון(ג),פחמימות(ג),שומן(ג)")
        mealEntries.forEach { e ->
            sb.appendLine(
                "${LocalDate.ofEpochDay(e.dateEpochDay)},${e.mealType},${e.name}," +
                    "${e.calories},${e.proteinG},${e.carbsG},${e.fatG}"
            )
        }

        sb.appendLine()
        sb.appendLine("=== אימונים ===")
        sb.appendLine("תאריך,תוכנית,הושלם")
        sessions.forEach { row ->
            val date = Instant.ofEpochMilli(row.dateEpochMillis).atZone(ZoneId.systemDefault()).toLocalDate()
            sb.appendLine("$date,${row.planName},${if (row.completed) "כן" else "לא"}")
        }

        return sb.toString()
    }
}
