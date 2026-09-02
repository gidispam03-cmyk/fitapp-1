package com.fitcoach.app.domain

import com.fitcoach.app.data.entity.ActivityLevel
import com.fitcoach.app.data.entity.Goal
import com.fitcoach.app.data.entity.Sex
import kotlin.math.roundToInt

data class NutritionTargets(
    val calories: Int,
    val proteinG: Int,
    val carbsG: Int,
    val fatG: Int
)

/**
 * חישוב BMR (נוסחת Mifflin-St Jeor - הנוסחה הנפוצה והמדויקת ביותר כיום להערכת BMR),
 * TDEE, ויעדים תזונתיים בהתאם למטרת המשתמש.
 *
 * הערה: זהו קירוב מבוסס נוסחאות מקובלות בתחום הכושר/תזונה, ואינו תחליף לייעוץ תזונאי/רפואי.
 */
object NutritionCalculator {

    private const val KCAL_PER_G_PROTEIN = 4
    private const val KCAL_PER_G_CARB = 4
    private const val KCAL_PER_G_FAT = 9

    /** מקדמי TDEE סטנדרטיים (Harris-Benedict activity factors) לפי רמת פעילות יומית. */
    private fun activityMultiplier(level: ActivityLevel): Double = when (level) {
        ActivityLevel.LOW -> 1.375
        ActivityLevel.MEDIUM -> 1.55
        ActivityLevel.HIGH -> 1.725
    }

    fun calculateBmr(weightKg: Float, heightCm: Int, age: Int, sex: Sex): Double {
        val base = 10 * weightKg + 6.25 * heightCm - 5 * age
        return if (sex == Sex.MALE) base + 5 else base - 161
    }

    fun calculateTdee(bmr: Double, activityLevel: ActivityLevel): Double =
        bmr * activityMultiplier(activityLevel)

    /**
     * התאמת קלוריות למטרה. לדוגמה: Lean Bulk = עודף קטן ונקי של כ-200-300 קלוריות,
     * בהתאם לעקרון שהוגדר במסמך האפיון.
     */
    private fun calorieAdjustment(goal: Goal): Int = when (goal) {
        Goal.MUSCLE_GAIN -> 275
        Goal.STRENGTH -> 150
        Goal.FAT_LOSS -> -400
        Goal.MAINTENANCE -> 0
        Goal.ATHLETICISM -> 0
    }

    /** חלבון בין 1.6-2.2 גרם לק"ג לפי מסמך האפיון - כאן נבחר ערך קבוע בטווח לכל מטרה. */
    private fun proteinPerKg(goal: Goal): Double = when (goal) {
        Goal.MUSCLE_GAIN, Goal.STRENGTH -> 2.0
        Goal.FAT_LOSS -> 2.0 // חלבון גבוה בגירעון קלורי שומר על מסת שריר
        Goal.MAINTENANCE, Goal.ATHLETICISM -> 1.8
    }

    fun calculateTargets(
        weightKg: Float,
        heightCm: Int,
        age: Int,
        sex: Sex,
        activityLevel: ActivityLevel,
        goal: Goal
    ): NutritionTargets {
        val bmr = calculateBmr(weightKg, heightCm, age, sex)
        val tdee = calculateTdee(bmr, activityLevel)
        val calories = (tdee + calorieAdjustment(goal)).roundToInt()

        val proteinG = (weightKg * proteinPerKg(goal)).roundToInt()
        val proteinCalories = proteinG * KCAL_PER_G_PROTEIN

        val fatCalories = (calories * 0.25).roundToInt()
        val fatG = fatCalories / KCAL_PER_G_FAT

        val remainingCalories = (calories - proteinCalories - fatCalories).coerceAtLeast(0)
        val carbsG = remainingCalories / KCAL_PER_G_CARB

        return NutritionTargets(
            calories = calories,
            proteinG = proteinG,
            carbsG = carbsG,
            fatG = fatG
        )
    }
}
