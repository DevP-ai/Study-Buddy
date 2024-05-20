package com.developer.android.dev.technologia.androidapp.studybuddy.presentation.components

import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.developer.android.dev.technologia.androidapp.studybuddy.domain.model.Subject

@Composable
fun CountCard(
    modifier: Modifier,
    headingText:String,
    count:String
) {
    ElevatedCard(modifier = modifier) {
        Column (
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(Subject.subjectColors[5]),
                    shape = MaterialTheme.shapes.small
                )
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = headingText,
                color = Color.White,
                style = MaterialTheme.typography.labelMedium
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = count,
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 35.sp)
            )
        }
    }
}