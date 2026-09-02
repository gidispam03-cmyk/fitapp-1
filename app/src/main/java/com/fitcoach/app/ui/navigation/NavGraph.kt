package com.fitcoach.app.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.fitcoach.app.ui.dashboard.DashboardScreen
import com.fitcoach.app.ui.nutrition.AddMealScreen
import com.fitcoach.app.ui.nutrition.NutritionScreen
import com.fitcoach.app.ui.nutrition.ShoppingListScreen
import com.fitcoach.app.ui.onboarding.OnboardingScreen
import com.fitcoach.app.ui.onboarding.OnboardingViewModel
import com.fitcoach.app.ui.progress.AddMeasurementScreen
import com.fitcoach.app.ui.progress.ProgressScreen
import com.fitcoach.app.ui.progress.RecommendationsScreen
import com.fitcoach.app.ui.progress.WeeklyCheckInScreen
import com.fitcoach.app.ui.settings.EditProfileScreen
import com.fitcoach.app.ui.settings.SettingsScreen
import com.fitcoach.app.ui.workout.ActiveWorkoutSessionScreen
import com.fitcoach.app.ui.workout.WorkoutHistoryScreen
import com.fitcoach.app.ui.workout.WorkoutScreen

private val bottomNavItems = listOf(
    BottomNavItem(Routes.DASHBOARD, "בית", Icons.Filled.Home),
    BottomNavItem(Routes.WORKOUT, "אימון", Icons.Filled.FitnessCenter),
    BottomNavItem(Routes.NUTRITION, "תזונה", Icons.Filled.Restaurant),
    BottomNavItem(Routes.PROGRESS, "התקדמות", Icons.Filled.ShowChart),
    BottomNavItem(Routes.SETTINGS, "הגדרות", Icons.Filled.Settings)
)

/**
 * שורש הניווט: מציג את ה-Onboarding אם לא הושלם, אחרת את המסך הראשי עם Bottom Nav.
 * isOnboardingCompleted == null => עדיין טוען מה-DB (אפשר להוסיף מסך Splash בעתיד).
 */
@Composable
fun FitCoachNavRoot() {
    val onboardingViewModel: OnboardingViewModel = hiltViewModel()
    val isOnboardingCompleted by onboardingViewModel.isOnboardingCompleted.collectAsState()

    when (isOnboardingCompleted) {
        null -> Unit
        false -> OnboardingScreen(viewModel = onboardingViewModel)
        true -> MainAppScaffold()
    }
}

@Composable
private fun MainAppScaffold() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                bottomNavItems.forEach { item ->
                    val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.DASHBOARD,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.DASHBOARD) { DashboardScreen(navController = navController) }
            composable(Routes.WORKOUT) { WorkoutScreen(navController = navController) }
            composable(
                route = Routes.WORKOUT_SESSION,
                arguments = listOf(navArgument("sessionId") { type = NavType.LongType })
            ) {
                ActiveWorkoutSessionScreen(navController = navController)
            }
            composable(Routes.WORKOUT_HISTORY) { WorkoutHistoryScreen() }
            composable(Routes.NUTRITION) { NutritionScreen(navController = navController) }
            composable(Routes.NUTRITION_ADD_MEAL) { AddMealScreen(navController = navController) }
            composable(Routes.NUTRITION_SHOPPING_LIST) { ShoppingListScreen() }
            composable(Routes.PROGRESS) { ProgressScreen(navController = navController) }
            composable(Routes.PROGRESS_ADD_MEASUREMENT) { AddMeasurementScreen(navController = navController) }
            composable(Routes.PROGRESS_CHECKIN) { WeeklyCheckInScreen(navController = navController) }
            composable(Routes.PROGRESS_RECOMMENDATIONS) { RecommendationsScreen() }
            composable(Routes.SETTINGS) { SettingsScreen(navController = navController) }
            composable(Routes.SETTINGS_EDIT_PROFILE) { EditProfileScreen(navController = navController) }
        }
    }
}
