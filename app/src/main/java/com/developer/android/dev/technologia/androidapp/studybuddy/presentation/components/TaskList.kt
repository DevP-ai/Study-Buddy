package com.developer.android.dev.technologia.androidapp.studybuddy.presentation.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.developer.android.dev.technologia.androidapp.studybuddy.R
import com.developer.android.dev.technologia.androidapp.studybuddy.domain.model.Task

fun LazyListScope.taskList(
    sectionTitle:String,
    tasks:List<Task>,
    emptyListText:String
){
    item {
        Text(
            text = sectionTitle,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(12.dp)
        )
    }
    if(tasks.isEmpty()){
        item {
            Column (
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Image(
                    painter = painterResource(id = R.drawable.img_tasks),
                    contentDescription = emptyListText,
                    modifier = Modifier.size(110.dp)
                )
                Spacer(modifier = Modifier.padding(12.dp))
                Text(
                    text = emptyListText,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
    items(tasks){task->
        TaskCard(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            task = task,
            onCheckBoxClick = { /*TODO*/ },
            onClick = {}
        )
    }
}

@Composable
private fun TaskCard(
    modifier: Modifier = Modifier,
    task: Task,
    onCheckBoxClick:()->Unit,
    onClick:()->Unit
) {
    ElevatedCard (
        modifier = modifier.clickable { onClick() }
    ){
        Row(
            modifier= Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            TaskCheckBox(
                isComplete = task.isComplete,
                borderColor = Color.Black,
                onCheckBoxClick = {onCheckBoxClick()}
            )
            Spacer(modifier = Modifier.padding(10.dp))

            Column {
                Text(
                    text = task.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if(task.isComplete){
                        TextDecoration.LineThrough
                    }else TextDecoration.None
                )
                Spacer(modifier = Modifier.padding(4.dp))

                Text(
                    text = "${task.dueDate}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
