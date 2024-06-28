package com.developer.android.dev.technologia.androidapp.studybuddy.presentation.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

@Composable
fun TimerSection(
    modifier: Modifier = Modifier,
    hours: String,
    minutes: String,
    seconds: String,
    size: Dp,
    fontSize: TextUnit,
    textStyle: TextStyle,
    stroke: Dp,
    progressColor: Color,
    isUseCenter:Boolean
) {
    val progress = seconds.toFloat()/60f

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing),
        label = ""
    )

    val surfaceVariantColor = MaterialTheme.colorScheme.surfaceVariant

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            drawCircle(
                color = surfaceVariantColor,
                style = Stroke(width = stroke.toPx())
            )
            drawArc(
                color = progressColor,
                startAngle = -90f,
                sweepAngle = 360 * animatedProgress,
                useCenter = isUseCenter,
                style = Stroke(width = stroke.toPx())
            )
        }
        Text(
            text = "$hours:$minutes:$seconds",
            style = textStyle.copy(
                fontSize = fontSize,
                fontWeight = FontWeight.Bold
            )
        )
    }
}