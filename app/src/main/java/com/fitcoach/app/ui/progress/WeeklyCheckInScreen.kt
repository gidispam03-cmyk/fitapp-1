package com.fitcoach.app.ui.progress

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fitcoach.app.ui.navigation.Routes

@Composable
fun WeeklyCheckInScreen(
    navController: NavController,
    viewModel: WeeklyCheckInViewModel = hiltViewModel()
) {
    var sleepHours by remember { mutableStateOf("7") }
    var energyLevel by remember { mutableStateOf(3) }
    var hungerLevel by remember { mutableStateOf(3) }
    var hasPain by remember { mutableStateOf(false) }
    var painNote by remember { mutableStateOf("") }
    var missedWorkouts by remember { mutableStateOf("0") }
    var weightsFeltHeavy by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Check-in שבועי", style = MaterialTheme.typography.headlineMedium)
        Text(
            "כמה דקות של כנות עוזרות למאמן הדיגיטלי להתאים לך המלצות מדויקות יותר.",
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text("כמה שעות ישנת בממוצע השבוע?", fontWeight = FontWeight.SemiBold)
        OutlinedTextField(
            value = sleepHours,
            onValueChange = { sleepHours = it.filter { c -> c.isDigit() || c == '.' } },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text("רמת אנרגיה כללית (1 נמוכה - 5 גבוהה)", fontWeight = FontWeight.SemiBold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            (1..5).forEach { level ->
                FilterChip(
                    selected = energyLevel == level,
                    onClick = { energyLevel = level },
                    label = { Text("$level") }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("רמת רעב כללית (1 נמוך - 5 גבוה)", fontWeight = FontWeight.SemiBold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            (1..5).forEach { level ->
                FilterChip(
                    selected = hungerLevel == level,
                    onClick = { hungerLevel = level },
                    label = { Text("$level") }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = hasPain, onCheckedChange = { hasPain = it })
            Text("הרגשת כאבים לאחרונה?")
        }
        if (hasPain) {
            OutlinedTextField(
                value = painNote,
                onValueChange = { painNote = it },
                label = { Text("איפה? (אופציונלי)") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("כמה אימונים פספסת השבוע?", fontWeight = FontWeight.SemiBold)
        OutlinedTextField(
            value = missedWorkouts,
            onValueChange = { missedWorkouts = it.filter { c -> c.isDigit() } },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = weightsFeltHeavy, onCheckedChange = { weightsFeltHeavy = it })
            Text("המשקלים הרגישו כבדים מהרגיל השבוע?")
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                viewModel.submit(
                    avgSleepHours = sleepHours.toFloatOrNull() ?: 7f,
                    energyLevel = energyLevel,
                    hungerLevel = hungerLevel,
                    hasPain = hasPain,
                    painNote = painNote.ifBlank { null },
                    missedWorkouts = missedWorkouts.toIntOrNull() ?: 0,
                    weightsFeltHeavy = weightsFeltHeavy
                ) {
                    navController.navigate(Routes.PROGRESS_RECOMMENDATIONS)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("שלח ועדכן המלצות")
        }
    }
}
