package com.developer.android.dev.technologia.androidapp.studybuddy.presentation.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.developer.android.dev.technologia.androidapp.studybuddy.R
import com.developer.android.dev.technologia.androidapp.studybuddy.domain.model.Session
import com.developer.android.dev.technologia.androidapp.studybuddy.domain.model.Subject
import com.developer.android.dev.technologia.androidapp.studybuddy.domain.model.Task
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.components.CountCard
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.components.SubjectCard
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.components.studySessionList
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.components.taskList


@Composable
fun DashboardScreen(

) {

    val subjects = listOf(
        Subject(subjectId = 0, name = "English", goalHours = 10f, colors = Subject.subjectColors[0]),
        Subject(subjectId = 0,name = "English", goalHours = 10f, colors = Subject.subjectColors[1]),
        Subject(subjectId = 0,name = "English", goalHours = 10f, colors = Subject.subjectColors[2]),
        Subject(subjectId = 0,name = "English", goalHours = 10f, colors = Subject.subjectColors[3]),
        Subject(subjectId = 0,name = "English", goalHours = 10f, colors = Subject.subjectColors[4])
    )

    val tasks = listOf(
        Task(
            taskId = 1,
            taskSubjectId = 0,
            title = "Prepare Note",
            description = "",
            dueDate = 0L,
            priority = 0,
            relatedToSubject = "",
            isComplete = false
        ),
        Task(
            taskId = 1,
            taskSubjectId = 0,
            title = "Prepare Note",
            description = "",
            dueDate = 0L,
            priority = 1,
            relatedToSubject = "",
            isComplete = true
        ),
        Task(
            taskId = 1,
            taskSubjectId = 0,
            title = "Prepare Note",
            description = "",
            dueDate = 0L,
            priority = 2,
            relatedToSubject = "",
            isComplete = true
        ),
        Task(
            taskId = 1,
            taskSubjectId = 0,
            title = "Prepare Note",
            description = "",
            dueDate = 0L,
            priority = 0,
            relatedToSubject = "",
            isComplete = true
        ),
        Task(
            taskId = 1,
            taskSubjectId = 0,
            title = "Prepare Note",
            description = "",
            dueDate = 0L,
            priority = 1,
            relatedToSubject = "",
            isComplete = true
        ),
        Task(
            taskId = 1,
            taskSubjectId = 0,
            title = "Prepare Note",
            description = "",
            dueDate = 0L,
            priority = 2,
            relatedToSubject = "",
            isComplete = true
        ),
        Task(
            taskId = 1,
            taskSubjectId = 0,
            title = "Prepare Note",
            description = "",
            dueDate = 0L,
            priority = 1,
            relatedToSubject = "",
            isComplete = true
        )
    )

    val sessions = listOf(
        Session(0,"Physics",0L,0L,1),
        Session(0,"Physics",0L,0L,1),
        Session(0,"Physics",0L,0L,1),
        Session(0,"Physics",0L,0L,1),
        Session(0,"Physics",0L,0L,1),
        Session(0,"Physics",0L,0L,1),
        Session(0,"Physics",0L,0L,1),
        Session(0,"Physics",0L,0L,1)
    )
    Scaffold(
        topBar = {
            DashboardTopAppBar()
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item{
                CountCardSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    subjectCount = 10,
                    studiedHours = "5",
                    goalHours = "5"
                )
            }

            item{
                SubjectCardSection(
                    modifier =  Modifier.fillMaxWidth(),
                    subjectList =subjects
                )
            }

            item{
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 48.dp, vertical = 20.dp),
                    onClick = { /*TODO*/ }) {
                    Text(text = "Start Study Session")
                }
            }
            taskList(
                sectionTitle = "UPCOMING TASKS",
                tasks = tasks,
                emptyListText = "You don't have any upcoming tasks.\nClick the + button in subject screen to add new task.",
                onTaskCardClick = {},
                onCheckBoxClick = {}
            )
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
            studySessionList(
                sectionTitle = "RECENT STUDY SESSION",
                sessions = sessions,
                emptyListText = "You don't have any recent study sessions.\nStart a study session to begin recording your progress.",
                onDeleteClick = {}
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardTopAppBar() {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Study Buddy",
                style = MaterialTheme.typography.headlineMedium
            )
        }
    )
}

@Composable
private fun CountCardSection(
    modifier: Modifier,
    subjectCount:Int,
    studiedHours:String,
    goalHours:String
) {
    Row (modifier = modifier){
        CountCard(
            modifier =Modifier.weight(1f),
            headingText = "Subject Count",
            count ="$subjectCount"
        )

        Spacer(modifier = Modifier.width(10.dp))

        CountCard(
            modifier =Modifier.weight(1f),
            headingText ="Studied Hours",
            count =studiedHours
        )

        Spacer(modifier = Modifier.width(10.dp))

        CountCard(
            modifier =Modifier.weight(1f),
            headingText ="Goal Study Hour",
            count =goalHours
        )

    }

}

@Composable
private fun SubjectCardSection(
    modifier: Modifier,
    subjectList:List<Subject>,
    emptyListText:String="You don't have any subjects.\nClick + button to add new subhject."
) {
    Column(modifier=modifier){
        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text = "SUBJECTS",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 12.dp)
            )
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Subject"
                )
            }
        }

        if(subjectList.isEmpty()){
            Image(
                painter = painterResource(id = R.drawable.img_books),
                contentDescription = emptyListText,
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = emptyListText,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        LazyRow (
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(start = 12.dp, end = 12.dp)
        ){
            items(subjectList){subject->
                SubjectCard(
                    onClick = {  },
                    gradientColors = subject.colors,
                    subjectName = subject.name
                )
            }
        }
    }
}