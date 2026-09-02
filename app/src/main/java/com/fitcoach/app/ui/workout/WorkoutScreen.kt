package com.fitcoach.app.ui.workout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
fun WorkoutScreen(
    navController: NavController,
    viewModel: WorkoutViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("האימון היום", style = MaterialTheme.typography.headlineMedium)
            TextButton(onClick = { navController.navigate(Routes.WORKOUT_HISTORY) }) {
                Text("היסטוריה")
            }
        }

        if (state.isLoading) {
            Spacer(modifier = Modifier.height(24.dp))
            CircularProgressIndicator()
            return@Column
        }

        if (state.exercises.isEmpty()) {
            Spacer(modifier = Modifier.height(24.dp))
            Text("לא נמצאה תוכנית אימונים. נסה לרענן.")
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { viewModel.loadTodayPlan() }) { Text("רענן") }
            return@Column
        }

        Text(state.planName, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(state.exercises) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(item.exercise.name, fontWeight = FontWeight.Bold)
                        val repsRange = if (item.plannedExercise.targetRepsMin == item.plannedExercise.targetRepsMax) {
                            "${item.plannedExercise.targetRepsMin}"
                        } else {
                            "${item.plannedExercise.targetRepsMin}-${item.plannedExercise.targetRepsMax}"
                        }
                        Text("${item.plannedExercise.targetSets} × $repsRange ${item.plannedExercise.repsNote ?: ""}".trim())
                        item.exercise.substituteNote?.let {
                            Text(it, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }

        Button(
            onClick = {
                viewModel.startSession { sessionId ->
                    navController.navigate(Routes.workoutSession(sessionId))
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("התחל אימון")
        }
    }
}
