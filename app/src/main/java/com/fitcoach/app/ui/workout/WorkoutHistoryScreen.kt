package com.fitcoach.app.ui.workout

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun WorkoutHistoryScreen(viewModel: WorkoutHistoryViewModel = hiltViewModel()) {
    val sessions by viewModel.sessions.collectAsState()
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("iw")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("היסטוריית אימונים", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        if (sessions.isEmpty()) {
            Text("עדיין אין אימונים שהושלמו.")
        } else {
            LazyColumn {
                items(sessions) { session ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Text(
                            dateFormat.format(Date(session.dateEpochMillis)),
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }
        }
    }
}
