package com.fitcoach.app.ui.progress

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.fitcoach.app.data.entity.BodyMeasurement

/**
 * גרף קו פשוט למגמת משקל, מצויר ישירות עם Canvas.
 * נבחר במכוון על פני ספריית גרפים חיצונית כדי לא להוסיף תלות חדשה שלא ניתן לבדוק
 * בפועל בסביבה הנוכחית (אין כאן Android SDK להרצת build אמיתי).
 */
@Composable
fun WeightTrendChart(measurements: List<BodyMeasurement>, modifier: Modifier = Modifier) {
    if (measurements.size < 2) return

    // measurements מגיעות מהחדש לישן (observeMeasurements ממוין DESC) - הופכים לכיוון כרונולוגי לגרף
    val chronological = measurements.sortedBy { it.dateEpochDay }
    val weights = chronological.map { it.weightKg }
    val minWeight = weights.min()
    val maxWeight = weights.max()
    val range = (maxWeight - minWeight).coerceAtLeast(0.5f)

    val lineColor = MaterialTheme.colorScheme.primary

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(140.dp)
    ) {
        val stepX = size.width / (weights.size - 1).coerceAtLeast(1)
        val points = weights.mapIndexed { index, weight ->
            val x = index * stepX
            val normalized = (weight - minWeight) / range
            val y = size.height - (normalized * size.height)
            Offset(x, y)
        }

        for (i in 0 until points.size - 1) {
            drawLine(
                color = lineColor,
                start = points[i],
                end = points[i + 1],
                strokeWidth = 6f,
                cap = StrokeCap.Round
            )
        }
        points.forEach { point ->
            drawCircle(color = lineColor, radius = 8f, center = point)
        }
    }
}
