package com.fitcoach.app.repository

import com.fitcoach.app.data.NutritionSeedData
import com.fitcoach.app.data.dao.NutritionDao
import com.fitcoach.app.data.entity.MealEntry
import com.fitcoach.app.data.entity.MealTemplate
import com.fitcoach.app.data.entity.MealType
import com.fitcoach.app.data.entity.ShoppingListItem
import com.fitcoach.app.data.entity.UserProfile
import com.fitcoach.app.domain.NutritionCalculator
import com.fitcoach.app.domain.NutritionTargets
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NutritionRepository @Inject constructor(
    private val dao: NutritionDao
) {

    /** אידמפוטנטי - מוסיף קטלוג ארוחות ורשימת קניות ברירת מחדל רק בפעם הראשונה. */
    suspend fun seedIfNeeded() {
        if (dao.mealTemplateCount() == 0) {
            dao.insertMealTemplates(NutritionSeedData.mealTemplates)
        }
        if (dao.shoppingItemCount() == 0) {
            dao.insertShoppingItems(NutritionSeedData.defaultShoppingList)
        }
    }

    fun calculateTargets(profile: UserProfile): NutritionTargets =
        NutritionCalculator.calculateTargets(
            weightKg = profile.weightKg,
            heightCm = profile.heightCm,
            age = profile.age,
            sex = profile.sex,
            activityLevel = profile.activityLevel,
            goal = profile.primaryGoal
        )

    fun observeAllTemplates(): Flow<List<MealTemplate>> = dao.observeAllTemplates()

    fun observeEntriesForDay(epochDay: Long): Flow<List<MealEntry>> = dao.observeEntriesForDay(epochDay)

    suspend fun getEntriesSince(sinceEpochDay: Long): List<MealEntry> = dao.getEntriesSince(sinceEpochDay)

    suspend fun logMealFromTemplate(epochDay: Long, template: MealTemplate) {
        dao.insertMealEntry(
            MealEntry(
                dateEpochDay = epochDay,
                mealType = template.mealType,
                name = template.name,
                calories = template.estCalories,
                proteinG = template.estProteinG,
                carbsG = template.estCarbsG,
                fatG = template.estFatG,
                timeEpochMillis = System.currentTimeMillis()
            )
        )
    }

    suspend fun logCustomMeal(
        epochDay: Long,
        mealType: MealType,
        name: String,
        calories: Int,
        proteinG: Int,
        carbsG: Int,
        fatG: Int
    ) {
        dao.insertMealEntry(
            MealEntry(
                dateEpochDay = epochDay,
                mealType = mealType,
                name = name,
                calories = calories,
                proteinG = proteinG,
                carbsG = carbsG,
                fatG = fatG,
                timeEpochMillis = System.currentTimeMillis()
            )
        )
    }

    suspend fun deleteEntry(entry: MealEntry) = dao.deleteMealEntry(entry)

    fun observeShoppingList(): Flow<List<ShoppingListItem>> = dao.observeShoppingList()

    suspend fun toggleShoppingItem(item: ShoppingListItem) {
        dao.updateShoppingItem(item.copy(checked = !item.checked))
    }

    suspend fun regenerateShoppingList() {
        dao.clearShoppingList()
        dao.insertShoppingItems(NutritionSeedData.defaultShoppingList)
    }
}
