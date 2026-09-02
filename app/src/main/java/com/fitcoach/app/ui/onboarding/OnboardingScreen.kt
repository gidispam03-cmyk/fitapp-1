package com.fitcoach.app.ui.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.fitcoach.app.data.entity.ActivityLevel
import com.fitcoach.app.data.entity.Goal
import com.fitcoach.app.data.entity.Sex
import com.fitcoach.app.data.entity.TrainingExperience

private const val TOTAL_STEPS = 3

@Composable
fun OnboardingScreen(viewModel: OnboardingViewModel) {
    val state by viewModel.formState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        LinearProgressIndicator(
            progress = { (state.currentStep + 1) / TOTAL_STEPS.toFloat() },
            modifier = Modifier.fillMaxWidth()
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(top = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (state.currentStep) {
                0 -> PersonalDataStep(state, viewModel)
                1 -> ActivityStep(state, viewModel)
                2 -> GoalStep(state, viewModel)
            }

            state.errorMessage?.let {
                Text(it, color = androidx.compose.material3.MaterialTheme.colorScheme.error)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (state.currentStep > 0) {
                OutlinedButton(onClick = { viewModel.previousStep() }) {
                    Text("הקודם")
                }
            } else {
                androidx.compose.foundation.layout.Spacer(modifier = Modifier.size(1.dp))
            }

            if (state.currentStep < TOTAL_STEPS - 1) {
                Button(onClick = { viewModel.nextStep() }) {
                    Text("הבא")
                }
            } else {
                Button(onClick = { viewModel.submit() }) {
                    Text("סיום והתחלה")
                }
            }
        }
    }
}

@Composable
private fun PersonalDataStep(state: OnboardingFormState, vm: OnboardingViewModel) {
    Text("נתונים אישיים", style = androidx.compose.material3.MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)

    OutlinedTextField(
        value = state.age,
        onValueChange = vm::updateAge,
        label = { Text("גיל") },
        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth()
    )
    OutlinedTextField(
        value = state.heightCm,
        onValueChange = vm::updateHeight,
        label = { Text("גובה (ס\"מ)") },
        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth()
    )
    OutlinedTextField(
        value = state.weightKg,
        onValueChange = vm::updateWeight,
        label = { Text("משקל (ק\"ג)") },
        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Decimal),
        modifier = Modifier.fillMaxWidth()
    )
    OutlinedTextField(
        value = state.bodyFatPercent,
        onValueChange = vm::updateBodyFat,
        label = { Text("אחוז שומן (אופציונלי)") },
        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Decimal),
        modifier = Modifier.fillMaxWidth()
    )

    Text("מין", fontWeight = FontWeight.SemiBold)
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        FilterChip(selected = state.sex == Sex.MALE, onClick = { vm.updateSex(Sex.MALE) }, label = { Text("זכר") })
        FilterChip(selected = state.sex == Sex.FEMALE, onClick = { vm.updateSex(Sex.FEMALE) }, label = { Text("נקבה") })
    }
}

@Composable
private fun ActivityStep(state: OnboardingFormState, vm: OnboardingViewModel) {
    Text("רמת פעילות ואימונים", style = androidx.compose.material3.MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)

    Text("רמת פעילות יומית", fontWeight = FontWeight.SemiBold)
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        FilterChip(selected = state.activityLevel == ActivityLevel.LOW, onClick = { vm.updateActivityLevel(ActivityLevel.LOW) }, label = { Text("נמוכה") })
        FilterChip(selected = state.activityLevel == ActivityLevel.MEDIUM, onClick = { vm.updateActivityLevel(ActivityLevel.MEDIUM) }, label = { Text("בינונית") })
        FilterChip(selected = state.activityLevel == ActivityLevel.HIGH, onClick = { vm.updateActivityLevel(ActivityLevel.HIGH) }, label = { Text("גבוהה") })
    }

    OutlinedTextField(
        value = state.workoutsPerWeek,
        onValueChange = vm::updateWorkoutsPerWeek,
        label = { Text("מספר אימונים בשבוע") },
        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth()
    )
    OutlinedTextField(
        value = state.avgSleepHours,
        onValueChange = vm::updateSleepHours,
        label = { Text("שעות שינה ממוצעות") },
        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Decimal),
        modifier = Modifier.fillMaxWidth()
    )

    Text("ניסיון באימוני כוח", fontWeight = FontWeight.SemiBold)
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        FilterChip(selected = state.trainingExperience == TrainingExperience.BEGINNER, onClick = { vm.updateExperience(TrainingExperience.BEGINNER) }, label = { Text("מתחיל") })
        FilterChip(selected = state.trainingExperience == TrainingExperience.INTERMEDIATE, onClick = { vm.updateExperience(TrainingExperience.INTERMEDIATE) }, label = { Text("בינוני") })
        FilterChip(selected = state.trainingExperience == TrainingExperience.ADVANCED, onClick = { vm.updateExperience(TrainingExperience.ADVANCED) }, label = { Text("מתקדם") })
    }
}

@Composable
private fun GoalStep(state: OnboardingFormState, vm: OnboardingViewModel) {
    Text("מה המטרה שלך?", style = androidx.compose.material3.MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)

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
            onClick = { vm.updateGoal(goal) },
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
