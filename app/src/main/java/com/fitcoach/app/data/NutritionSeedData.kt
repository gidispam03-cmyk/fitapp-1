package com.fitcoach.app.data

import com.fitcoach.app.data.entity.MealTemplate
import com.fitcoach.app.data.entity.MealType
import com.fitcoach.app.data.entity.ShoppingCategory
import com.fitcoach.app.data.entity.ShoppingListItem

/**
 * ערכים תזונתיים משוערים (הערכה סבירה לפי הרכיבים שתוארו במסמכים, לא מבוסס על מאגר חיצוני).
 * המשתמש תמיד יכול לערוך/להוסיף ארוחה ידנית עם ערכים מדויקים יותר.
 */
object NutritionSeedData {

    val mealTemplates = listOf(
        // --- בוקר ---
        MealTemplate(
            name = "ביצים עם לחם מלא וטחינה",
            mealType = MealType.BREAKFAST,
            ingredientsText = "3 ביצים, 2 פרוסות לחם מלא, טחינה/אבוקדו, ירקות",
            estCalories = 550, estProteinG = 35, estCarbsG = 45, estFatG = 25
        ),
        MealTemplate(
            name = "יוגורט יווני עם גרנולה ופירות",
            mealType = MealType.BREAKFAST,
            ingredientsText = "יוגורט יווני, גרנולה, פירות, אגוזים",
            estCalories = 450, estProteinG = 30, estCarbsG = 55, estFatG = 12
        ),
        MealTemplate(
            name = "טוסט גבינה וביצה",
            mealType = MealType.BREAKFAST,
            ingredientsText = "טוסט עם גבינה, ביצה, ירקות",
            estCalories = 400, estProteinG = 25, estCarbsG = 35, estFatG = 18
        ),
        MealTemplate(
            name = "שייק חלבון עם בננה ושיבולת שועל",
            mealType = MealType.BREAKFAST,
            ingredientsText = "חלב, בננה, שיבולת שועל, חמאת בוטנים",
            estCalories = 500, estProteinG = 30, estCarbsG = 65, estFatG = 12
        ),

        // --- אוניברסיטה ---
        MealTemplate(
            name = "כריך חזה הודו",
            mealType = MealType.UNIVERSITY,
            ingredientsText = "לחם/פיתה, 120-150 גרם חזה הודו, ירקות, טחינה",
            estCalories = 450, estProteinG = 35, estCarbsG = 45, estFatG = 12
        ),
        MealTemplate(
            name = "כריך טונה",
            mealType = MealType.UNIVERSITY,
            ingredientsText = "טונה, טחינה/מיונז, ירקות",
            estCalories = 420, estProteinG = 32, estCarbsG = 40, estFatG = 14
        ),
        MealTemplate(
            name = "כריך חביתה",
            mealType = MealType.UNIVERSITY,
            ingredientsText = "ביצים, גבינה, ירקות",
            estCalories = 400, estProteinG = 28, estCarbsG = 35, estFatG = 16
        ),
        MealTemplate(
            name = "כריך סלמון מעושן",
            mealType = MealType.UNIVERSITY,
            ingredientsText = "לחם מלא, סלמון מעושן, גבינה",
            estCalories = 430, estProteinG = 30, estCarbsG = 38, estFatG = 18
        ),

        // --- לפני אימון ---
        MealTemplate(
            name = "בננה",
            mealType = MealType.PRE_WORKOUT,
            ingredientsText = "בננה אחת",
            estCalories = 105, estProteinG = 1, estCarbsG = 27, estFatG = 0
        ),
        MealTemplate(
            name = "2-3 תמרים",
            mealType = MealType.PRE_WORKOUT,
            ingredientsText = "2-3 תמרים",
            estCalories = 140, estProteinG = 1, estCarbsG = 36, estFatG = 0
        ),

        // --- אחרי אימון ---
        MealTemplate(
            name = "שייק אחרי אימון",
            mealType = MealType.POST_WORKOUT,
            ingredientsText = "250 מ\"ל חלב, בננה, 25-35 גרם שיבולת שועל, כף חמאת בוטנים, דבש",
            estCalories = 550, estProteinG = 25, estCarbsG = 75, estFatG = 15
        ),

        // --- ערב ---
        MealTemplate(
            name = "חזה עוף + אורז + ירקות",
            mealType = MealType.DINNER,
            ingredientsText = "200 גרם חזה עוף, אורז, ירקות, שמן זית",
            estCalories = 600, estProteinG = 50, estCarbsG = 60, estFatG = 12
        ),
        MealTemplate(
            name = "פרגית + תפוח אדמה",
            mealType = MealType.DINNER,
            ingredientsText = "פרגית, תפוחי אדמה, ירקות",
            estCalories = 650, estProteinG = 45, estCarbsG = 55, estFatG = 22
        ),
        MealTemplate(
            name = "סלמון + אורז",
            mealType = MealType.DINNER,
            ingredientsText = "סלמון, אורז, ירקות",
            estCalories = 620, estProteinG = 40, estCarbsG = 55, estFatG = 25
        ),
        MealTemplate(
            name = "קציצות בקר ביתיות + אורז",
            mealType = MealType.DINNER,
            ingredientsText = "בשר טחון, אורז, ירקות",
            estCalories = 680, estProteinG = 42, estCarbsG = 60, estFatG = 28
        ),

        // --- לפני שינה ---
        MealTemplate(
            name = "יוגורט עם אגוזים",
            mealType = MealType.BEFORE_BED,
            ingredientsText = "יוגורט, אגוזים, פרי",
            estCalories = 300, estProteinG = 20, estCarbsG = 20, estFatG = 15
        )
    )

    val defaultShoppingList = listOf(
        ShoppingListItem(category = ShoppingCategory.PROTEIN, name = "חזה עוף", quantityText = "1 ק\"ג"),
        ShoppingListItem(category = ShoppingCategory.PROTEIN, name = "ביצים", quantityText = "30 יחידות"),
        ShoppingListItem(category = ShoppingCategory.PROTEIN, name = "טונה בקופסה", quantityText = "4 קופסאות"),
        ShoppingListItem(category = ShoppingCategory.PROTEIN, name = "יוגורט יווני", quantityText = "6 יחידות"),
        ShoppingListItem(category = ShoppingCategory.PROTEIN, name = "קוטג'", quantityText = "2 יחידות"),

        ShoppingListItem(category = ShoppingCategory.CARB, name = "אורז", quantityText = "1 ק\"ג"),
        ShoppingListItem(category = ShoppingCategory.CARB, name = "לחם מלא", quantityText = "2 יחידות"),
        ShoppingListItem(category = ShoppingCategory.CARB, name = "שיבולת שועל", quantityText = "500 גרם"),
        ShoppingListItem(category = ShoppingCategory.CARB, name = "בטטה", quantityText = "1 ק\"ג"),

        ShoppingListItem(category = ShoppingCategory.FAT, name = "טחינה גולמית", quantityText = "1 יחידה"),
        ShoppingListItem(category = ShoppingCategory.FAT, name = "שמן זית", quantityText = "1 יחידה"),
        ShoppingListItem(category = ShoppingCategory.FAT, name = "אגוזים מעורבים", quantityText = "200 גרם"),
        ShoppingListItem(category = ShoppingCategory.FAT, name = "חמאת בוטנים", quantityText = "1 יחידה"),

        ShoppingListItem(category = ShoppingCategory.VEGETABLE_FRUIT, name = "ירקות מעורבים", quantityText = "לפי טעם"),
        ShoppingListItem(category = ShoppingCategory.VEGETABLE_FRUIT, name = "בננות", quantityText = "1 ק\"ג"),
        ShoppingListItem(category = ShoppingCategory.VEGETABLE_FRUIT, name = "עגבניות ומלפפונים", quantityText = "לפי טעם")
    )
}
