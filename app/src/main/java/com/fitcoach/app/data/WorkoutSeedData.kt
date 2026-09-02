package com.fitcoach.app.data

import com.fitcoach.app.data.entity.Exercise
import com.fitcoach.app.data.entity.PlannedExercise
import com.fitcoach.app.data.entity.WorkoutPlanKey

/**
 * נתוני ברירת המחדל לתוכניות A/B, מבוססים ישירות על מסמך האפיון.
 * ה-Repository בודק אם טבלת exercises ריקה, ואם כן - מזין את הנתונים האלו פעם אחת בלבד.
 */
object WorkoutSeedData {

    data class ExerciseSeed(val name: String, val muscleGroup: String, val substituteNote: String? = null)

    data class PlannedItemSeed(
        val exerciseName: String,
        val targetSets: Int,
        val targetRepsMin: Int,
        val targetRepsMax: Int,
        val repsNote: String? = null
    )

    val exerciseSeeds = listOf(
        // תוכנית A - גב + רגליים + יד קדמית
        ExerciseSeed("Box Jump", "רגליים"),
        ExerciseSeed("Back Squat", "רגליים"),
        ExerciseSeed("Pull Up / Lat Pulldown", "גב"),
        ExerciseSeed("Romanian Deadlift", "שרשרת אחורית"),
        ExerciseSeed("Chest Supported Row", "גב"),
        ExerciseSeed("Bulgarian Split Squat", "רגליים"),
        ExerciseSeed("Face Pull", "כתפיים אחוריות"),
        ExerciseSeed("Barbell Curl / Hammer Curl", "יד קדמית"),
        ExerciseSeed("Hanging Leg Raise", "בטן"),
        ExerciseSeed("Pallof Press", "core"),
        // תוכנית B - חזה + כתפיים + יד אחורית
        ExerciseSeed("Explosive Push Up", "חזה", "אפשרות החלפה: Medicine Ball Chest Pass"),
        ExerciseSeed("Dumbbell Bench Press", "חזה"),
        ExerciseSeed("Standing Overhead Press", "כתפיים"),
        ExerciseSeed("Incline Dumbbell Press", "חזה"),
        ExerciseSeed("Dips", "חזה / יד אחורית", "אפשרות החלפה (כאב בשורש כף היד): Chest Press Machine"),
        ExerciseSeed("Dumbbell Lateral Raise", "כתפיים"),
        ExerciseSeed("Rope Triceps Pushdown", "יד אחורית"),
        ExerciseSeed("Ab Wheel Rollout", "בטן", "אפשרות החלפה: Dead Bug")
        // שימו לב: Face Pull כבר מוגדר למעלה ומשמש גם בתוכנית B - אין כפילות
    )

    val planAItems = listOf(
        PlannedItemSeed("Box Jump", 3, 5, 5),
        PlannedItemSeed("Back Squat", 4, 5, 5),
        PlannedItemSeed("Pull Up / Lat Pulldown", 4, 6, 8),
        PlannedItemSeed("Romanian Deadlift", 3, 6, 8),
        PlannedItemSeed("Chest Supported Row", 3, 8, 10),
        PlannedItemSeed("Bulgarian Split Squat", 3, 8, 8, "לכל רגל"),
        PlannedItemSeed("Face Pull", 3, 12, 15),
        PlannedItemSeed("Barbell Curl / Hammer Curl", 3, 8, 10),
        PlannedItemSeed("Hanging Leg Raise", 3, 10, 15),
        PlannedItemSeed("Pallof Press", 3, 12, 12, "לכל צד")
    )

    val planBItems = listOf(
        PlannedItemSeed("Explosive Push Up", 3, 5, 5),
        PlannedItemSeed("Dumbbell Bench Press", 4, 6, 6),
        PlannedItemSeed("Standing Overhead Press", 4, 5, 6),
        PlannedItemSeed("Incline Dumbbell Press", 3, 8, 10),
        PlannedItemSeed("Dips", 3, 8, 10),
        PlannedItemSeed("Dumbbell Lateral Raise", 3, 12, 15),
        PlannedItemSeed("Face Pull", 3, 12, 15),
        PlannedItemSeed("Rope Triceps Pushdown", 3, 10, 12),
        PlannedItemSeed("Ab Wheel Rollout", 3, 8, 12)
    )

    fun planKeyName(key: WorkoutPlanKey): String = when (key) {
        WorkoutPlanKey.A -> "גב + רגליים + יד קדמית"
        WorkoutPlanKey.B -> "חזה + כתפיים + יד אחורית"
    }

    fun toPlannedExercises(
        planId: Long,
        seeds: List<PlannedItemSeed>,
        exerciseByName: Map<String, Exercise>
    ): List<PlannedExercise> =
        seeds.mapIndexedNotNull { index, seed ->
            val exercise = exerciseByName[seed.exerciseName] ?: return@mapIndexedNotNull null
            PlannedExercise(
                planId = planId,
                exerciseId = exercise.id,
                orderIndex = index,
                targetSets = seed.targetSets,
                targetRepsMin = seed.targetRepsMin,
                targetRepsMax = seed.targetRepsMax,
                repsNote = seed.repsNote
            )
        }
}
