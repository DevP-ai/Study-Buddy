package com.developer.android.dev.technologia.androidapp.studybuddy.presentation.session

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.WorkManager
import com.developer.android.dev.technologia.androidapp.studybuddy.MainActivity
import com.developer.android.dev.technologia.androidapp.studybuddy.utils.Constants.CLICK_REQUEST_CODE

object ServiceHelper {

    fun clickPendingIntent(context: Context):PendingIntent{
        val deepLinkIntent = Intent(
            Intent.ACTION_VIEW,
            "study_buddy://dashboard/session".toUri(),
            context,
            MainActivity::class.java
        )
        return TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(deepLinkIntent)
            getPendingIntent(
                CLICK_REQUEST_CODE,
                PendingIntent.FLAG_IMMUTABLE
            )
        }
    }
    @SuppressLint("RestrictedApi")
    fun triggerForegroundService(context: Context, action:String){
        Intent(context,StudyTimerService::class.java).apply {
            this.action = action
            ContextCompat.startForegroundService(context,this)
        }
    }
}