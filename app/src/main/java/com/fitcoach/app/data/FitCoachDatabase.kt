package com.fitcoach.app.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.fitcoach.app.data.dao.NutritionDao
import com.fitcoach.app.data.dao.TrackingDao
import com.fitcoach.app.data.dao.UserProfileDao
import com.fitcoach.app.data.dao.WorkoutDao
import com.fitcoach.app.data.entity.BodyMeasurement
import com.fitcoach.app.data.entity.Exercise
import com.fitcoach.app.data.entity.MealEntry
import com.fitcoach.app.data.entity.MealTemplate
import com.fitcoach.app.data.entity.PlannedExercise
import com.fitcoach.app.data.entity.Recommendation
import com.fitcoach.app.data.entity.SetLog
import com.fitcoach.app.data.entity.ShoppingListItem
import com.fitcoach.app.data.entity.UserProfile
import com.fitcoach.app.data.entity.WeeklyCheckIn
import com.fitcoach.app.data.entity.WorkoutPlan
import com.fitcoach.app.data.entity.WorkoutSession

/**
 * בסיס הנתונים המקומי של האפליקציה. עובד לחלוטין אופליין.
 *
 * הערה לגבי הפיתוח הנוכחי: כל עוד האפליקציה בשלבי פיתוח (לא שוחררה לגרסה 1.0),
 * שינויי סכימה מלווים בהעלאת version + fallbackToDestructiveMigration (ב-AppModule)
 * במקום Migration ידני, כדי לא להיתקע על סכימות שלא נבדקו בפועל. לפני שחרור אמיתי
 * יש להחליף זאת ב-Migration-ים מפורשים כדי לא לאבד נתוני משתמשים.
 */
@Database(
    entities = [
        UserProfile::class,
        Exercise::class, WorkoutPlan::class, PlannedExercise::class, WorkoutSession::class, SetLog::class,
        MealTemplate::class, MealEntry::class, ShoppingListItem::class,
        BodyMeasurement::class, WeeklyCheckIn::class, Recommendation::class
    ],
    version = 4,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class FitCoachDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun nutritionDao(): NutritionDao
    abstract fun trackingDao(): TrackingDao
}
