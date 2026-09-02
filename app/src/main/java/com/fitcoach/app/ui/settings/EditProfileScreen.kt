package com.fitcoach.app.ui.settings

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fitcoach.app.data.entity.ActivityLevel
import com.fitcoach.app.data.entity.Goal
import com.fitcoach.app.data.entity.Sex
import com.fitcoach.app.data.entity.TrainingExperience

@Composable
fun EditProfileScreen(
    navController: NavController,
    viewModel: EditProfileViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    if (state.saved) {
        LaunchedEffect(Unit) { navController.popBackStack() }
        return
    }

    if (state.isLoading) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("עריכת פרופיל", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.age, onValueChange = viewModel::updateAge,
            label = { Text("גיל") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = state.heightCm, onValueChange = viewModel::updateHeight,
            label = { Text("גובה (ס\"מ)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = state.weightKg, onValueChange = viewModel::updateWeight,
            label = { Text("משקל (ק\"ג)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text("מין", fontWeight = FontWeight.SemiBold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(selected = state.sex == Sex.MALE, onClick = { viewModel.updateSex(Sex.MALE) }, label = { Text("זכר") })
            FilterChip(selected = state.sex == Sex.FEMALE, onClick = { viewModel.updateSex(Sex.FEMALE) }, label = { Text("נקבה") })
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("רמת פעילות יומית", fontWeight = FontWeight.SemiBold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(selected = state.activityLevel == ActivityLevel.LOW, onClick = { viewModel.updateActivityLevel(ActivityLevel.LOW) }, label = { Text("נמוכה") })
            FilterChip(selected = state.activityLevel == ActivityLevel.MEDIUM, onClick = { viewModel.updateActivityLevel(ActivityLevel.MEDIUM) }, label = { Text("בינונית") })
            FilterChip(selected = state.activityLevel == ActivityLevel.HIGH, onClick = { viewModel.updateActivityLevel(ActivityLevel.HIGH) }, label = { Text("גבוהה") })
        }

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = state.workoutsPerWeek, onValueChange = viewModel::updateWorkoutsPerWeek,
            label = { Text("אימונים בשבוע") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = state.avgSleepHours, onValueChange = viewModel::updateSleepHours,
            label = { Text("שעות שינה ממוצעות") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text("ניסיון באימוני כוח", fontWeight = FontWeight.SemiBold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(selected = state.trainingExperience == TrainingExperience.BEGINNER, onClick = { viewModel.updateExperience(TrainingExperience.BEGINNER) }, label = { Text("מתחיל") })
            FilterChip(selected = state.trainingExperience == TrainingExperience.INTERMEDIATE, onClick = { viewModel.updateExperience(TrainingExperience.INTERMEDIATE) }, label = { Text("בינוני") })
            FilterChip(selected = state.trainingExperience == TrainingExperience.ADVANCED, onClick = { viewModel.updateExperience(TrainingExperience.ADVANCED) }, label = { Text("מתקדם") })
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("מטרה עיקרית", fontWeight = FontWeight.SemiBold)
        val goals = listOf(
            Goal.MUSCLE_GAIN to "עלייה במסת שריר",
            Goal.FAT_LOSS to "ירידה באחוז שומן",
            Goal.MAINTENANCE to "שמירה על משקל",
            Goal.STRENGTH to "שיפור כוח",
            Goal.ATHLETICISM to "שיפור אתלטיות"
        )
        goals.forEach { (goal, label) ->
            FilterChip(
                selected = state.primaryGoal == goal,
                onClick = { viewModel.updateGoal(goal) },
                label = { Text(label) },
                modifier = Modifier.fillMaxWidth()
            )
        }

        state.errorMessage?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { viewModel.save() }, modifier = Modifier.fillMaxWidth()) {
            Text("שמור שינויים")
        }
    }
}
