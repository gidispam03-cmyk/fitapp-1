package com.fitcoach.app.ui.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fitcoach.app.ui.navigation.Routes

@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val profile = state.profile
    val targets = state.targets

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("שלום, מתאמן", style = MaterialTheme.typography.headlineMedium)

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("משקל נוכחי: ${profile?.weightKg ?: "--"} ק\"ג")
                Text("מטרה: ${profile?.primaryGoal ?: "--"}")
                Text("אימונים בשבוע: ${profile?.workoutsPerWeek ?: "--"}")

                if (targets != null) {
                    Text("\nיעד קלורי יומי: ${targets.calories} קל'", fontWeight = FontWeight.SemiBold)
                    Text("נצרך היום: ${state.consumedCalories} / ${targets.calories} קל'")
                    val calProgress = if (targets.calories > 0) {
                        (state.consumedCalories.toFloat() / targets.calories.toFloat()).coerceIn(0f, 1f)
                    } else 0f
                    LinearProgressIndicator(progress = { calProgress }, modifier = Modifier.fillMaxWidth())

                    Text("\nחלבון: ${state.consumedProtein} / ${targets.proteinG} גרם", fontWeight = FontWeight.SemiBold)
                    val protProgress = if (targets.proteinG > 0) {
                        (state.consumedProtein.toFloat() / targets.proteinG.toFloat()).coerceIn(0f, 1f)
                    } else 0f
                    LinearProgressIndicator(progress = { protProgress }, modifier = Modifier.fillMaxWidth())
                }
            }
        }

        AnimatedVisibility(visible = state.latestRecommendation != null, modifier = Modifier.fillMaxWidth()) {
            state.latestRecommendation?.let { recommendation ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("המלצה יומית", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(recommendation.message)
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = state.checkInDue,
            enter = fadeIn() + expandVertically(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "הגיע הזמן ל-Check-in השבועי - כמה דקות שיעזרו לכוון את התוכנית שלך.",
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { navController.navigate(Routes.PROGRESS_CHECKIN) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("מלא Check-in שבועי")
                    }
                }
            }
        }
    }
}
