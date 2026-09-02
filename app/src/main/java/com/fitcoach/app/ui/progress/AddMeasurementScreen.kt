package com.fitcoach.app.ui.progress

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun AddMeasurementScreen(
    navController: NavController,
    viewModel: AddMeasurementViewModel = hiltViewModel()
) {
    var weight by remember { mutableStateOf("") }
    var waist by remember { mutableStateOf("") }
    var chest by remember { mutableStateOf("") }
    var arm by remember { mutableStateOf("") }
    var thigh by remember { mutableStateOf("") }
    var bodyFat by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("הוספת מדידה", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = weight,
            onValueChange = { weight = it.filter { c -> c.isDigit() || c == '.' } },
            label = { Text("משקל (ק\"ג) *") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = waist,
            onValueChange = { waist = it.filter { c -> c.isDigit() || c == '.' } },
            label = { Text("היקף מותניים (ס\"מ, אופציונלי)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = chest,
            onValueChange = { chest = it.filter { c -> c.isDigit() || c == '.' } },
            label = { Text("היקף חזה (ס\"מ, אופציונלי)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = arm,
            onValueChange = { arm = it.filter { c -> c.isDigit() || c == '.' } },
            label = { Text("היקף יד (ס\"מ, אופציונלי)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = thigh,
            onValueChange = { thigh = it.filter { c -> c.isDigit() || c == '.' } },
            label = { Text("היקף ירך (ס\"מ, אופציונלי)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = bodyFat,
            onValueChange = { bodyFat = it.filter { c -> c.isDigit() || c == '.' } },
            label = { Text("אחוז שומן (אופציונלי)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )

        error?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                val w = weight.toFloatOrNull()
                if (w == null) {
                    error = "נא להזין משקל תקין"
                    return@Button
                }
                viewModel.save(
                    weightKg = w,
                    waistCm = waist.toFloatOrNull(),
                    chestCm = chest.toFloatOrNull(),
                    armCm = arm.toFloatOrNull(),
                    thighCm = thigh.toFloatOrNull(),
                    bodyFatPercent = bodyFat.toFloatOrNull()
                ) { navController.popBackStack() }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("שמור מדידה")
        }
    }
}
