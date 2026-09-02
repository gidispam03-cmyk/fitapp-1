package com.fitcoach.app.ui.progress

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

@Composable
fun ProgressScreen(
    navController: NavController,
    viewModel: ProgressViewModel = hiltViewModel()
) {
    val measurements by viewModel.measurements.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("מעקב גוף", style = MaterialTheme.typography.headlineMedium)
            TextButton(onClick = { navController.navigate(Routes.PROGRESS_RECOMMENDATIONS) }) {
                Text("המלצות")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = { navController.navigate(Routes.PROGRESS_ADD_MEASUREMENT) },
                modifier = Modifier.weight(1f)
            ) { Text("הוסף מדידה") }
            OutlinedButton(
                onClick = { navController.navigate(Routes.PROGRESS_CHECKIN) },
                modifier = Modifier.weight(1f)
            ) { Text("Check-in שבועי") }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (measurements.isEmpty()) {
            Text("עדיין אין מדידות. הוסף מדידה ראשונה כדי להתחיל לעקוב אחר ההתקדמות.")
        } else {
            val latest = measurements.first()
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("מדידה אחרונה", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text("משקל: ${latest.weightKg} ק\"ג")
                    latest.waistCm?.let { Text("היקף מותניים: $it ס\"מ") }
                    latest.bodyFatPercent?.let { Text("אחוז שומן: $it%") }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (measurements.size >= 2) {
                Text("מגמת משקל", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Card(modifier = Modifier.fillMaxWidth()) {
                    WeightTrendChart(
                        measurements = measurements,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            Text("היסטוריה", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(measurements) { measurement ->
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
                            Text(LocalDate.ofEpochDay(measurement.dateEpochDay).format(dateFormatter))
                            Text("${measurement.weightKg} ק\"ג")
                        }
                    }
                }
            }
        }
    }
}
