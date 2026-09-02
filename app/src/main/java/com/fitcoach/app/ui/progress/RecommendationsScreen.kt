package com.fitcoach.app.ui.progress

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fitcoach.app.data.entity.RecommendationType

private val typeLabels = mapOf(
    RecommendationType.NUTRITION to "תזונה",
    RecommendationType.WORKOUT to "אימונים",
    RecommendationType.RECOVERY to "התאוששות",
    RecommendationType.GENERAL to "כללי"
)

@Composable
fun RecommendationsScreen(viewModel: RecommendationsViewModel = hiltViewModel()) {
    val recommendations by viewModel.recommendations.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("המלצות", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(12.dp))

        if (recommendations.isEmpty()) {
            Text("אין עדיין המלצות. מלא Check-in שבועי במסך \"מעקב גוף\" כדי לקבל המלצות מותאמות אישית.")
        } else {
            LazyColumn {
                items(recommendations) { rec ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                typeLabels[rec.type] ?: rec.type.name,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(rec.message)
                        }
                    }
                }
            }
        }
    }
}
