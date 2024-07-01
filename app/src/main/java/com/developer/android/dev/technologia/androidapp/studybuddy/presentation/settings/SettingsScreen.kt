package com.developer.android.dev.technologia.androidapp.studybuddy.presentation.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.developer.android.dev.technologia.androidapp.studybuddy.presentation.session.SessionScreenTopBar
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@com.ramcosta.composedestinations.annotation.Destination
@Composable
fun SettingsScreenRoute(navigator: DestinationsNavigator) {
    SettingsScreen(onBackButtonClick = { navigator.navigateUp() })
}

@Composable
fun SettingsScreen(
    onBackButtonClick: () -> Unit
) {
    val context = LocalContext.current
    val settingsItems = listOf(
        Triple(
            "Privacy Policy",
            Icons.Outlined.Warning,
            "https://sites.google.com/view/studytracker/privacy-policy"
        ),
        Triple("Help", Icons.Outlined.Build, "https://sites.google.com/view/studytracker/home"),
        Triple("About", Icons.Outlined.Info, "https://sites.google.com/view/studytracker/contact")

    )
    Scaffold(
        topBar = {
            SettingsScreenTopBar(
                onBackButtonClick = onBackButtonClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            settingsItems.forEach { (title, icon, url) ->
                SettingsItem(
                    title = title,
                    icon = icon,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { openUrl(context, url) }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Developed by Devajit Patar",
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Version: 1.0.0",
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )


        }
    }
}

fun openUrl(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenTopBar(
    onBackButtonClick: () -> Unit
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onBackButtonClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back Navigation"
                )
            }
        },
        title = {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineSmall
            )
        }
    )
}

@Composable
private fun SettingsItem(
    title: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    description: String? = null,
) {
    Row(modifier = modifier) {
        Icon(
            modifier = Modifier
                .wrapContentSize()
                .padding(16.dp),
            imageVector = icon,
            contentDescription = null,
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterVertically),
        ) {
            Text(text = title)
            if (description?.isNotBlank() == true) {
                Text(text = description, style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}