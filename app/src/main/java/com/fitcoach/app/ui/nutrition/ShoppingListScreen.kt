package com.fitcoach.app.ui.nutrition

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fitcoach.app.data.entity.ShoppingCategory

private val categoryLabels = mapOf(
    ShoppingCategory.PROTEIN to "חלבונים",
    ShoppingCategory.CARB to "פחמימות",
    ShoppingCategory.FAT to "שומנים איכותיים",
    ShoppingCategory.VEGETABLE_FRUIT to "ירקות ופירות"
)

@Composable
fun ShoppingListScreen(viewModel: ShoppingListViewModel = hiltViewModel()) {
    val items by viewModel.items.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("רשימת קניות", style = MaterialTheme.typography.headlineMedium)
            TextButton(onClick = { viewModel.regenerate() }) { Text("אפס רשימה") }
        }

        LazyColumn(modifier = Modifier.weight(1f)) {
            ShoppingCategory.values().forEach { category ->
                val categoryItems = items.filter { it.category == category }
                if (categoryItems.isNotEmpty()) {
                    item {
                        Text(
                            categoryLabels[category] ?: category.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)
                        )
                    }
                    items(categoryItems) { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(checked = item.checked, onCheckedChange = { viewModel.toggle(item) })
                            Column {
                                Text(item.name, fontWeight = FontWeight.Medium)
                                Text(item.quantityText, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }
    }
}
