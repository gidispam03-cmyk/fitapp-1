package com.fitcoach.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class MealType { BREAKFAST, UNIVERSITY, PRE_WORKOUT, POST_WORKOUT, DINNER, BEFORE_BED, OTHER }

enum class ShoppingCategory { PROTEIN, CARB, FAT, VEGETABLE_FRUIT }

/** ארוחה "לדוגמה" מהתפריט - קטלוג קבוע שממנו אפשר לבחור ולתעד ארוחה בלחיצה אחת. */
@Entity(tableName = "meal_templates")
data class MealTemplate(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val mealType: MealType,
    val ingredientsText: String,
    val estCalories: Int,
    val estProteinG: Int,
    val estCarbsG: Int,
    val estFatG: Int
)

/**
 * ארוחה שתועדה בפועל על ידי המשתמש. הערכים התזונתיים נשמרים כאן "קפואים" בזמן התיעוד,
 * כך שעריכה עתידית של MealTemplate לא תשנה רטרואקטיבית היסטוריה שכבר נרשמה.
 */
@Entity(tableName = "meal_entries")
data class MealEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    /** LocalDate.toEpochDay() - קיבוץ לפי יום קלנדרי, ללא תלות באזור זמן/שעה מדויקת */
    val dateEpochDay: Long,
    val mealType: MealType,
    val name: String,
    val calories: Int,
    val proteinG: Int,
    val carbsG: Int,
    val fatG: Int,
    val timeEpochMillis: Long
)

@Entity(tableName = "shopping_list_items")
data class ShoppingListItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val category: ShoppingCategory,
    val name: String,
    val quantityText: String,
    val checked: Boolean = false
)
