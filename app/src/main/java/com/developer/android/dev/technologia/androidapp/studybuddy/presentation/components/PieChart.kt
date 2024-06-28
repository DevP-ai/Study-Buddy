package com.developer.android.dev.technologia.androidapp.studybuddy.presentation.components

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.developer.android.dev.technologia.androidapp.studybuddy.utils.PieChartDataPoint
import kotlinx.coroutines.launch


@SuppressLint("DefaultLocale")
@Composable
fun ChartSection(
    data:List<PieChartDataPoint>,
    studiedHours: String,
    goalHours: String
) {
    val totalValue = data.sumOf {it.value.toDouble()}.toFloat()
    val goal = ((goalHours.toFloat()/totalValue)*100)
    val studied = ((studiedHours.toFloat()/totalValue)*100)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        if(totalValue>0){
            PieChart(
                data = data,
                modifier = Modifier.size(150.dp)
            )
        }

        Box(
            modifier = Modifier
                .padding(top = 40.dp),
            contentAlignment = Alignment.Center
        ) {

            Row(
                modifier = Modifier
                    .width(250.dp)
                    .height(60.dp)
                    .clip(shape = RoundedCornerShape(12.dp))
                    .background(color = Color.LightGray),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center

            ) {

                Column {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(shape = CircleShape)
                                .background(
                                    Color.Green
                                )
                        ) {}
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Goal Study Hours: ${String.format("%.2f",goal)}%",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(shape = CircleShape)
                                .background(
                                    Color.Blue
                                )
                        ) {}
                        Spacer(modifier = Modifier.width(10.dp))

                        Text(
                            text = "Total Studied Hours: ${String.format("%.2f",studied)}% ",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                    }



                }


            }
        }
    }

}

@Composable
fun PieChart(
    data:List<PieChartDataPoint>,
    modifier: Modifier,
    innerRadiusFraction:Float = 0.3f
) {
    val totalValue = data.sumOf {it.value.toDouble()}.toFloat().takeIf { it>0 }?:1f

    val animatedAngles = remember(data) { List(data.size) { Animatable(0f) } }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect (data){
        data.forEachIndexed{index,slice->
            coroutineScope.launch {
                animatedAngles[index].animateTo(
                    targetValue = (slice.value/totalValue)*360f,
                    animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
                )
            }
        }
    }

    Canvas(modifier = modifier) {
        val centerX = size.width/2f
        val centerY = size.height/2f
        val outerRadius = size.width.coerceAtMost(size.height)/2f
        val innerRadius = outerRadius*innerRadiusFraction

        var startAngle = -90f

        data.forEachIndexed{index,slice->
            val sweepAngle = animatedAngles[index].value
            drawArc(
                color = slice.color,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = Offset(centerX-outerRadius,centerY-outerRadius),
                size = Size(outerRadius*2,outerRadius*2)
            )
            val angle = startAngle + sweepAngle / 2
            val labelRadius = outerRadius + 16.dp.toPx()

            val labelX = centerX + labelRadius * kotlin.math.cos(Math.toRadians(angle.toDouble())).toFloat()
            val labelY = centerY + labelRadius * kotlin.math.sin(Math.toRadians(angle.toDouble())).toFloat()

            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    "${slice.title} (${(slice.value / totalValue * 100).toInt()}%)",
                    labelX,
                    labelY,
                    android.graphics.Paint().apply {
                        textSize = 13.sp.toPx()
                        textAlign = android.graphics.Paint.Align.CENTER
                        color = android.graphics.Color.RED
                    }
                )
            }
            startAngle += sweepAngle
        }
        drawInnerCircle(centerX, centerY, innerRadius)
    }
}
private fun DrawScope.drawInnerCircle(centerX: Float, centerY: Float, radius: Float) {
    drawCircle(
        color = Color.White,
        radius = radius,
        center = Offset(centerX, centerY)
    )
}