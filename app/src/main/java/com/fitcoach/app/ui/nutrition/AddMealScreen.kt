package com.fitcoach.app.ui.nutrition

import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.fitcoach.app.data.entity.MealType

private val mealTypeLabels = mapOf(
    MealType.BREAKFAST to "בוקר",
    MealType.UNIVERSITY to "אוניברסיטה",
    MealType.PRE_WORKOUT to "לפני אימון",
    MealType.POST_WORKOUT to "אחרי אימון",
    MealType.DINNER to "ערב",
    MealType.BEFORE_BED to "לפני שינה",
    MealType.OTHER to "אחר"
)

@Composable
fun AddMealScreen(
    navController: NavController,
    viewModel: AddMealViewModel = hiltViewModel()
) {
    val templates by viewModel.templates.collectAsState()
    var showCustomForm by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("הוספת ארוחה", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(12.dp))

        if (showCustomForm) {
            CustomMealForm(
                onSave = { mealType, name, cal, prot, carbs, fat ->
                    viewModel.logCustom(mealType, name, cal, prot, carbs, fat) {
                        navController.popBackStack()
                    }
                },
                onCancel = { showCustomForm = false }
            )
        } else {
            OutlinedButton(onClick = { showCustomForm = true }, modifier = Modifier.fillMaxWidth()) {
                Text("הזנה ידנית")
            }
            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(modifier = Modifier.weight(1f)) {
                MealType.values().forEach { type ->
                    val typeTemplates = templates.filter { it.mealType == type }
                    if (typeTemplates.isNotEmpty()) {
                        item {
                            Text(
                                mealTypeLabels[type] ?: type.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)
                            )
                        }
                        items(typeTemplates) { template ->
                            Card(
                                onClick = {
                                    viewModel.logTemplate(template) { navController.popBackStack() }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(template.name, fontWeight = FontWeight.Bold)
                                    Text(template.ingredientsText, style = MaterialTheme.typography.bodySmall)
                                    Text("${template.estCalories} קלוריות · חלבון ${template.estProteinG} גרם")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CustomMealForm(
    onSave: (MealType, String, Int, Int, Int, Int) -> Unit,
    onCancel: () -> Unit
) {
    var selectedType by remember { mutableStateOf(MealType.OTHER) }
    var name by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var protein by remember { mutableStateOf("") }
    var carbs by remember { mutableStateOf("") }
    var fat by remember { mutableStateOf("") }

    Column {
        Text("סוג ארוחה", fontWeight = FontWeight.SemiBold)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MealType.values().forEach { type ->
                FilterChip(
                    selected = selectedType == type,
                    onClick = { selectedType = type },
                    label = { Text(mealTypeLabels[type] ?: type.name) }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = name, onValueChange = { name = it },
            label = { Text("שם הארוחה") }, modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = calories, onValueChange = { calories = it.filter { c -> c.isDigit() } },
            label = { Text("קלוריות") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = protein, onValueChange = { protein = it.filter { c -> c.isDigit() } },
            label = { Text("חלבון (גרם)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = carbs, onValueChange = { carbs = it.filter { c -> c.isDigit() } },
            label = { Text("פחמימות (גרם)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = fat, onValueChange = { fat = it.filter { c -> c.isDigit() } },
            label = { Text("שומן (גרם)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = onCancel) { Text("ביטול") }
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        onSave(
                            selectedType,
                            name,
                            calories.toIntOrNull() ?: 0,
                            protein.toIntOrNull() ?: 0,
                            carbs.toIntOrNull() ?: 0,
                            fat.toIntOrNull() ?: 0
                        )
                    }
                }
            ) {
                Text("שמור")
            }
        }
    }
}
