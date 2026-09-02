package com.fitcoach.app.ui.settings

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fitcoach.app.ui.dashboard.DashboardViewModel
import com.fitcoach.app.ui.navigation.Routes

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: DashboardViewModel = hiltViewModel(),
    exportViewModel: ExportViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val profile = state.profile
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("הגדרות", style = MaterialTheme.typography.headlineMedium)
        Text("\nגיל: ${profile?.age ?: "--"}")
        Text("גובה: ${profile?.heightCm ?: "--"} ס\"מ")
        Text("משקל: ${profile?.weightKg ?: "--"} ק\"ג")
        Text("רמת ניסיון: ${profile?.trainingExperience ?: "--"}")

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(
            onClick = { navController.navigate(Routes.SETTINGS_EDIT_PROFILE) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("ערוך פרופיל")
        }

        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = {
                exportViewModel.exportToFile(context) { uri ->
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/csv"
                        putExtra(Intent.EXTRA_STREAM, uri)
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "שיתוף/שמירת הנתונים"))
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("ייצוא נתונים")
        }

        Text("\nהעדפות תזונה (כשרות וכו') יתווספו במסך זה בשלבים הבאים.")
    }
}
