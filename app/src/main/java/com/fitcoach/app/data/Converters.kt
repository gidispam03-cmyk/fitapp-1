package com.fitcoach.app.data

import androidx.room.TypeConverter
import com.fitcoach.app.data.entity.ActivityLevel
import com.fitcoach.app.data.entity.DietaryPreference
import com.fitcoach.app.data.entity.Goal
import com.fitcoach.app.data.entity.MealType
import com.fitcoach.app.data.entity.RecommendationType
import com.fitcoach.app.data.entity.Sex
import com.fitcoach.app.data.entity.ShoppingCategory
import com.fitcoach.app.data.entity.TrainingExperience
import com.fitcoach.app.data.entity.WorkoutPlanKey

/**
 * Room לא יודע לשמור enum ישירות - ממירים ל-String ובחזרה.
 * שיטה זו (name/valueOf) עמידה יותר לשינויים עתידיים מאשר שמירת ordinal.
 */
class Converters {

    @TypeConverter
    fun fromSex(value: Sex): String = value.name

    @TypeConverter
    fun toSex(value: String): Sex = Sex.valueOf(value)

    @TypeConverter
    fun fromActivityLevel(value: ActivityLevel): String = value.name

    @TypeConverter
    fun toActivityLevel(value: String): ActivityLevel = ActivityLevel.valueOf(value)

    @TypeConverter
    fun fromTrainingExperience(value: TrainingExperience): String = value.name

    @TypeConverter
    fun toTrainingExperience(value: String): TrainingExperience = TrainingExperience.valueOf(value)

    @TypeConverter
    fun fromGoal(value: Goal): String = value.name

    @TypeConverter
    fun toGoal(value: String): Goal = Goal.valueOf(value)

    @TypeConverter
    fun fromDietaryPreference(value: DietaryPreference): String = value.name

    @TypeConverter
    fun toDietaryPreference(value: String): DietaryPreference = DietaryPreference.valueOf(value)

    @TypeConverter
    fun fromWorkoutPlanKey(value: WorkoutPlanKey): String = value.name

    @TypeConverter
    fun toWorkoutPlanKey(value: String): WorkoutPlanKey = WorkoutPlanKey.valueOf(value)

    @TypeConverter
    fun fromMealType(value: MealType): String = value.name

    @TypeConverter
    fun toMealType(value: String): MealType = MealType.valueOf(value)

    @TypeConverter
    fun fromShoppingCategory(value: ShoppingCategory): String = value.name

    @TypeConverter
    fun toShoppingCategory(value: String): ShoppingCategory = ShoppingCategory.valueOf(value)

    @TypeConverter
    fun fromRecommendationType(value: RecommendationType): String = value.name

    @TypeConverter
    fun toRecommendationType(value: String): RecommendationType = RecommendationType.valueOf(value)
}
