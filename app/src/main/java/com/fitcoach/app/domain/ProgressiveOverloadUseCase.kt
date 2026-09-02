package com.fitcoach.app.domain

import com.fitcoach.app.data.entity.SetLog

/**
 * המלצת Progressive Overload בעקבות סט/סטים שבוצעו.
 */
sealed class OverloadRecommendation {
    data class IncreaseWeight(val suggestedAddKg: Float) : OverloadRecommendation()
    object MaintainWeight : OverloadRecommendation()
    object ConsiderDeload : OverloadRecommendation()
    object NotEnoughData : OverloadRecommendation()
}

/**
 * לוגיקת ההתקדמות לפי מסמך האפיון:
 * - אם המשתמש הגיע לטווח החזרות העליון בכל הסטים ו-RPE נמוך -> להעלות משקל (ברירת מחדל 2.5 ק"ג).
 * - אם יש ירידה בביצועים לעומת הפעם הקודמת (משקל/חזרות נמוכים יותר) -> להציע הורדת עומס/מנוחה
 *   ולבדוק שינה ותזונה (ההודעה בפועל נבנית ב-UI).
 * - אחרת -> להמשיך באותו משקל.
 */
object ProgressiveOverloadUseCase {
    private const val DEFAULT_INCREMENT_KG = 2.5f
    private const val LOW_RPE_THRESHOLD = 7.5f

    fun evaluate(
        currentSets: List<SetLog>,
        targetRepsMax: Int,
        previousSets: List<SetLog>
    ): OverloadRecommendation {
        if (currentSets.isEmpty()) return OverloadRecommendation.NotEnoughData

        val allSetsReachedTop = currentSets.all { it.reps >= targetRepsMax }
        val recordedRpes = currentSets.mapNotNull { it.rpe }
        val avgRpe = if (recordedRpes.isEmpty()) null else recordedRpes.average().toFloat()
        val rpeIsLow = avgRpe == null || avgRpe <= LOW_RPE_THRESHOLD

        if (allSetsReachedTop && rpeIsLow) {
            return OverloadRecommendation.IncreaseWeight(DEFAULT_INCREMENT_KG)
        }

        if (previousSets.isNotEmpty()) {
            val prevAvgWeight = previousSets.map { it.weightKg }.average()
            val currentAvgWeight = currentSets.map { it.weightKg }.average()
            val prevAvgReps = previousSets.map { it.reps }.average()
            val currentAvgReps = currentSets.map { it.reps }.average()

            val performanceDropped = currentAvgWeight < prevAvgWeight || currentAvgReps < prevAvgReps - 1
            if (performanceDropped) {
                return OverloadRecommendation.ConsiderDeload
            }
        }

        return OverloadRecommendation.MaintainWeight
    }
}
