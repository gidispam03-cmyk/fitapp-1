package com.fitcoach.app.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.fitcoach.app.data.entity.MealEntry
import com.fitcoach.app.data.entity.MealTemplate
import com.fitcoach.app.data.entity.MealType
import com.fitcoach.app.data.entity.ShoppingListItem
import kotlinx.coroutines.flow.Flow

@Dao
interface NutritionDao {

    // --- Meal templates (קטלוג ארוחות מוכן) ---
    @Insert
    suspend fun insertMealTemplates(templates: List<MealTemplate>)

    @Query("SELECT COUNT(*) FROM meal_templates")
    suspend fun mealTemplateCount(): Int

    @Query("SELECT * FROM meal_templates WHERE mealType = :type ORDER BY id")
    suspend fun getTemplatesForType(type: MealType): List<MealTemplate>

    @Query("SELECT * FROM meal_templates ORDER BY mealType, id")
    fun observeAllTemplates(): Flow<List<MealTemplate>>

    // --- Meal entries (מה שבפועל תועד) ---
    @Insert
    suspend fun insertMealEntry(entry: MealEntry): Long

    @Delete
    suspend fun deleteMealEntry(entry: MealEntry)

    @Query("SELECT * FROM meal_entries WHERE dateEpochDay = :epochDay ORDER BY timeEpochMillis")
    fun observeEntriesForDay(epochDay: Long): Flow<List<MealEntry>>

    @Query("SELECT * FROM meal_entries WHERE dateEpochDay >= :sinceEpochDay")
    suspend fun getEntriesSince(sinceEpochDay: Long): List<MealEntry>

    // --- Shopping list ---
    @Insert
    suspend fun insertShoppingItems(items: List<ShoppingListItem>)

    @Query("SELECT COUNT(*) FROM shopping_list_items")
    suspend fun shoppingItemCount(): Int

    @Query("SELECT * FROM shopping_list_items ORDER BY category, name")
    fun observeShoppingList(): Flow<List<ShoppingListItem>>

    @Update
    suspend fun updateShoppingItem(item: ShoppingListItem)

    @Query("DELETE FROM shopping_list_items")
    suspend fun clearShoppingList()
}
