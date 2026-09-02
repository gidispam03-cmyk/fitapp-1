package com.fitcoach.app.ui.navigation

/**
 * כל מסלולי הניווט באפליקציה במקום אחד - מונע typos ומקל על הרחבה.
 */
object Routes {
    const val ONBOARDING = "onboarding"
    const val DASHBOARD = "dashboard"
    const val WORKOUT = "workout"
    const val WORKOUT_SESSION = "workout_session/{sessionId}"
    const val WORKOUT_HISTORY = "workout_history"
    const val NUTRITION = "nutrition"
    const val NUTRITION_ADD_MEAL = "nutrition_add_meal"
    const val NUTRITION_SHOPPING_LIST = "nutrition_shopping_list"
    const val PROGRESS = "progress"
    const val PROGRESS_ADD_MEASUREMENT = "progress_add_measurement"
    const val PROGRESS_CHECKIN = "progress_checkin"
    const val PROGRESS_RECOMMENDATIONS = "progress_recommendations"
    const val SETTINGS = "settings"
    const val SETTINGS_EDIT_PROFILE = "settings_edit_profile"

    fun workoutSession(sessionId: Long) = "workout_session/$sessionId"
}

data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)
