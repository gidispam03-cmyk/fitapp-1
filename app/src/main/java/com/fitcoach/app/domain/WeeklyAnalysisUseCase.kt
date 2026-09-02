package com.fitcoach.app.domain

import com.fitcoach.app.data.entity.BodyMeasurement
import com.fitcoach.app.data.entity.Goal
import com.fitcoach.app.data.entity.RecommendationType
import com.fitcoach.app.data.entity.WeeklyCheckIn
import kotlin.math.abs

data class WeeklyAnalysisInput(
    val goal: Goal,
    /** מדידות גוף מהחלון האחרון (כ-3 שבועות), ממוינות מהישן לחדש. */
    val measurements: List<BodyMeasurement>,
    val targetWorkoutsPerWeek: Int,
    val completedWorkoutsThisWeek: Int,
    val avgDailyCaloriesThisWeek: Int?,
    val targetCalories: Int?,
    val avgDailyProteinThisWeek: Int?,
    val targetProteinG: Int?,
    val latestCheckIn: WeeklyCheckIn?
)

data class GeneratedRecommendation(val type: RecommendationType, val message: String)

/**
 * מנתח את נתוני השבוע/השבועות האחרונים ומייצר המלצות, בהתאם לעקרונות שהוגדרו במסמכי האפיון:
 * - לעולם לא הודעה שלילית גרידא - תמיד עם כיוון פעולה קונקרטי.
 * - השוואת מגמת משקל למטרה (Lean Bulk / ירידה / שמירה).
 * - עמידה ביעד חלבון, עקביות אימונים, ואיתותי התאוששות (שינה, כאב, תחושת עומס).
 */
object WeeklyAnalysisUseCase {

    fun analyze(input: WeeklyAnalysisInput): List<GeneratedRecommendation> {
        val recommendations = mutableListOf<GeneratedRecommendation>()

        recommendations += analyzeWeightTrend(input)
        recommendations += analyzeNutritionAdherence(input)
        recommendations += analyzeTrainingConsistency(input)
        recommendations += analyzeRecovery(input)

        if (recommendations.isEmpty()) {
            recommendations += GeneratedRecommendation(
                RecommendationType.GENERAL,
                "אין עדיין מספיק נתונים לניתוח שבועי מלא. המשך לתעד משקל, ארוחות ואימונים כדי לקבל המלצות מותאמות אישית."
            )
        }
        return recommendations
    }

    private fun analyzeWeightTrend(input: WeeklyAnalysisInput): List<GeneratedRecommendation> {
        if (input.measurements.size < 2) return emptyList()

        val first = input.measurements.first()
        val last = input.measurements.last()
        val weightDelta = last.weightKg - first.weightKg
        val waistDelta = if (first.waistCm != null && last.waistCm != null) last.waistCm - first.waistCm else null

        val results = mutableListOf<GeneratedRecommendation>()

        when (input.goal) {
            Goal.MUSCLE_GAIN, Goal.STRENGTH -> when {
                weightDelta < 0.1f -> results += GeneratedRecommendation(
                    RecommendationType.NUTRITION,
                    "לא הייתה עלייה משמעותית במשקל לאחרונה. מומלץ להוסיף 150-200 קלוריות ביום."
                )
                waistDelta != null && waistDelta > 2f && weightDelta > 1.5f -> results += GeneratedRecommendation(
                    RecommendationType.NUTRITION,
                    "המשקל עלה מהר והיקף המותניים עלה משמעותית. מומלץ להפחית מעט את העודף הקלורי."
                )
                else -> results += GeneratedRecommendation(
                    RecommendationType.GENERAL,
                    "עלית %.1f ק\"ג לאחרונה בקצב טוב. המשך באותה תזונה.".format(weightDelta)
                )
            }
            Goal.FAT_LOSS -> when {
                weightDelta > -0.1f -> results += GeneratedRecommendation(
                    RecommendationType.NUTRITION,
                    "לא הייתה ירידה משמעותית במשקל לאחרונה. מומלץ להפחית 150-200 קלוריות ביום."
                )
                weightDelta < -1.5f -> results += GeneratedRecommendation(
                    RecommendationType.NUTRITION,
                    "הירידה במשקל מהירה יחסית. מומלץ להוסיף מעט קלוריות כדי לשמר מסת שריר."
                )
                else -> results += GeneratedRecommendation(
                    RecommendationType.GENERAL,
                    "הירידה במשקל בקצב בריא. המשך באותה תזונה."
                )
            }
            Goal.MAINTENANCE, Goal.ATHLETICISM -> {
                if (abs(weightDelta) > 1.5f) {
                    results += GeneratedRecommendation(
                        RecommendationType.NUTRITION,
                        "המשקל השתנה יותר מהצפוי למטרת שמירה. כדאי לבדוק את הצריכה הקלורית היומית."
                    )
                }
            }
        }

        return results
    }

    private fun analyzeNutritionAdherence(input: WeeklyAnalysisInput): List<GeneratedRecommendation> {
        val target = input.targetProteinG ?: return emptyList()
        val avg = input.avgDailyProteinThisWeek ?: return emptyList()
        if (target <= 0) return emptyList()

        val percent = (avg.toFloat() / target.toFloat() * 100).toInt()
        if (percent >= 90) return emptyList()

        val gap = target - avg
        return listOf(
            GeneratedRecommendation(
                RecommendationType.NUTRITION,
                "עמדת ב-$percent% מיעד החלבון השבוע. כדי לתמוך במטרה שלך, נסה להוסיף עוד כ-$gap גרם חלבון ביום."
            )
        )
    }

    private fun analyzeTrainingConsistency(input: WeeklyAnalysisInput): List<GeneratedRecommendation> {
        if (input.targetWorkoutsPerWeek <= 0) return emptyList()
        val missed = input.targetWorkoutsPerWeek - input.completedWorkoutsThisWeek
        if (missed <= 0) return emptyList()

        return listOf(
            GeneratedRecommendation(
                RecommendationType.WORKOUT,
                "השבוע בוצעו ${input.completedWorkoutsThisWeek} מתוך ${input.targetWorkoutsPerWeek} אימונים מתוכננים. " +
                    "זה קורה - נסה לתכנן מראש את שני האימונים הקרובים כדי לחזור לקצב."
            )
        )
    }

    private fun analyzeRecovery(input: WeeklyAnalysisInput): List<GeneratedRecommendation> {
        val checkIn = input.latestCheckIn ?: return emptyList()
        val results = mutableListOf<GeneratedRecommendation>()

        if (checkIn.avgSleepHours < 6.5f) {
            results += GeneratedRecommendation(
                RecommendationType.RECOVERY,
                "שעות השינה השבוע היו נמוכות יחסית (${checkIn.avgSleepHours} שעות בממוצע). " +
                    "שינה איכותית משפרת התאוששות וביצועים - כדאי לנסות להקדים את השינה."
            )
        }

        if (checkIn.hasPain) {
            results += GeneratedRecommendation(
                RecommendationType.RECOVERY,
                "דיווחת על כאב לאחרונה. מומלץ לתת תשומת לב לאזור הרגיש, ולשקול הפחתת עומס זמנית או התייעצות עם איש מקצוע."
            )
        }

        if (checkIn.weightsFeltHeavy && checkIn.energyLevel <= 2) {
            results += GeneratedRecommendation(
                RecommendationType.RECOVERY,
                "המשקלים הרגישו כבדים והאנרגיה הייתה נמוכה השבוע. ייתכן שכדאי שבוע קליל יותר (דילוד) לפני חזרה להעמסה."
            )
        }

        return results
    }
}
