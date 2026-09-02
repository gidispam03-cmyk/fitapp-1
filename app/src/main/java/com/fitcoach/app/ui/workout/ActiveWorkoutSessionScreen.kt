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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fitcoach.app.domain.OverloadRecommendation

@Composable
fun ActiveWorkoutSessionScreen(
    navController: NavController,
    viewModel: ActiveSessionViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    if (state.isFinished) {
        LaunchedEffect(Unit) { navController.popBackStack() }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(state.planName, style = MaterialTheme.typography.headlineMedium)

        if (state.isLoading) {
            Spacer(modifier = Modifier.height(24.dp))
            CircularProgressIndicator()
            return@Column
        }

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(state.exerciseStates) { exState ->
                ExerciseLogCard(
                    exState = exState,
                    onLogSet = { weight, reps, rpe ->
                        viewModel.logSet(exState.item.exercise.id, weight, reps, rpe)
                    }
                )
            }
        }

        Button(onClick = { viewModel.finishSession() }, modifier = Modifier.fillMaxWidth()) {
            Text("סיים אימון")
        }
    }
}

@Composable
private fun ExerciseLogCard(
    exState: ExerciseLogState,
    onLogSet: (Float, Int, Float?) -> Unit
) {
    var weightText by remember { mutableStateOf("") }
    var repsText by remember { mutableStateOf("") }
    var rpeText by remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(exState.item.exercise.name, fontWeight = FontWeight.Bold)
            Text(
                "יעד: ${exState.item.plannedExercise.targetSets} × " +
                    "${exState.item.plannedExercise.targetRepsMin}-${exState.item.plannedExercise.targetRepsMax}"
            )

            exState.loggedSets.forEach { set ->
                Text(
                    "סט ${set.setNumber}: ${set.weightKg} ק\"ג × ${set.reps} חזרות" +
                        (set.rpe?.let { " (RPE $it)" } ?: "")
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 8.dp)) {
                OutlinedTextField(
                    value = weightText,
                    onValueChange = { weightText = it.filter { c -> c.isDigit() || c == '.' } },
                    label = { Text("ק\"ג") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = repsText,
                    onValueChange = { repsText = it.filter { c -> c.isDigit() } },
                    label = { Text("חזרות") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = rpeText,
                    onValueChange = { rpeText = it.filter { c -> c.isDigit() || c == '.' } },
                    label = { Text("RPE") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f)
                )
            }

            Button(
                onClick = {
                    val weight = weightText.toFloatOrNull()
                    val reps = repsText.toIntOrNull()
                    if (weight != null && reps != null) {
                        onLogSet(weight, reps, rpeText.toFloatOrNull())
                        weightText = ""
                        repsText = ""
                        rpeText = ""
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text("שמור סט")
            }

            exState.recommendation?.let { rec ->
                val message = when (rec) {
                    is OverloadRecommendation.IncreaseWeight ->
                        "באימון הבא ניתן להעלות משקל ב-${rec.suggestedAddKg} ק\"ג"
                    OverloadRecommendation.ConsiderDeload ->
                        "ירידה בביצועים לעומת הפעם הקודמת - כדאי לשקול הורדת עומס, מנוחה, ולבדוק שינה ותזונה"
                    OverloadRecommendation.MaintainWeight ->
                        "המשך באותו משקל"
                    OverloadRecommendation.NotEnoughData -> null
                }
                message?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(it, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
