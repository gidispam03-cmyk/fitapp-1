package com.fitcoach.app.ui.nutrition

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
fun NutritionScreen(
    navController: NavController,
    viewModel: NutritionViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("תזונה היום", style = MaterialTheme.typography.headlineMedium)
            TextButton(onClick = { navController.navigate(Routes.NUTRITION_SHOPPING_LIST) }) {
                Text("רשימת קניות")
            }
        }

        val targets = state.targets
        if (targets == null) {
            Spacer(modifier = Modifier.height(24.dp))
            Text("השלם קודם את פרטי הפרופיל (מסך הגדרות) כדי לחשב יעד תזונתי.")
        } else {
            Spacer(modifier = Modifier.height(12.dp))
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    MacroRow("קלוריות", state.consumedCalories, targets.calories, "")
                    MacroRow("חלבון", state.consumedProtein, targets.proteinG, "גרם")
                    MacroRow("פחמימות", state.consumedCarbs, targets.carbsG, "גרם")
                    MacroRow("שומן", state.consumedFat, targets.fatG, "גרם")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("ארוחות היום", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)

        if (state.entries.isEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text("עדיין לא תועדו ארוחות היום.")
        }

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(state.entries) { entry ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(entry.name, fontWeight = FontWeight.Bold)
                            Text("${entry.calories} קלוריות · חלבון ${entry.proteinG} גרם")
                        }
                        IconButton(onClick = { viewModel.deleteEntry(entry) }) {
                            Icon(Icons.Filled.Delete, contentDescription = "מחק ארוחה")
                        }
                    }
                }
            }
        }

        Button(
            onClick = { navController.navigate(Routes.NUTRITION_ADD_MEAL) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("הוסף ארוחה")
        }
    }
}

@Composable
private fun MacroRow(label: String, consumed: Int, target: Int, unit: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label)
        Text("$consumed / $target $unit".trim())
    }
    val progress = if (target > 0) (consumed.toFloat() / target.toFloat()).coerceIn(0f, 1f) else 0f
    LinearProgressIndicator(
        progress = { progress },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    )
}
