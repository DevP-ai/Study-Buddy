package com.developer.android.dev.technologia.androidapp.studybuddy.presentation.session

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.NotificationCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import com.developer.android.dev.technologia.androidapp.studybuddy.utils.Constants.ACTION_SERVICE_CANCEL
import com.developer.android.dev.technologia.androidapp.studybuddy.utils.Constants.ACTION_SERVICE_START
import com.developer.android.dev.technologia.androidapp.studybuddy.utils.Constants.ACTION_SERVICE_STOP
import com.developer.android.dev.technologia.androidapp.studybuddy.utils.Constants.NOTIFICATION_CHANNEL_ID
import com.developer.android.dev.technologia.androidapp.studybuddy.utils.Constants.NOTIFICATION_CHANNEL_NAME
import com.developer.android.dev.technologia.androidapp.studybuddy.utils.Constants.NOTIFICATION_ID
import com.developer.android.dev.technologia.androidapp.studybuddy.utils.pad
import dagger.hilt.android.AndroidEntryPoint
import java.util.Timer
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.ZERO
import kotlin.time.Duration.Companion.seconds

@AndroidEntryPoint
class StudyTimerService:Service() {

    @Inject
    lateinit var  notificationManager: NotificationManager

    @Inject
    lateinit var notificationBuilder: NotificationCompat.Builder

    private val binder = StudySessionTimerBinder()

    private lateinit var timer:Timer

    var duration:Duration = Duration.ZERO
        private set

    var seconds = mutableStateOf("00")
        private set
    var minutes = mutableStateOf("00")
        private set

    var hours = mutableStateOf("00")
        private set

    var currentTimerState = mutableStateOf(TimerState.IDLE)
        private set

    var subjectId = mutableStateOf<Int?>(null)

    override fun onBind(p0: Intent?) = binder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        intent?.action.let { it->
            when(intent?.action){
                ACTION_SERVICE_START ->{
                    startForegroundService()
                    startTimer{hours,minute,seconds->
                        updateNotification(hours,minute,seconds)
                    }
                }

                ACTION_SERVICE_STOP ->{
                    stopTimer()
                }

                ACTION_SERVICE_CANCEL ->{
                    stopTimer()
                    cancelTimer()
                    stopForegroundService()
                }
//            }
        }
        return START_STICKY
    }



    @SuppressLint("ForegroundServiceType")
    private fun startForegroundService() {
        createNotificationChannel()
        startForeground(NOTIFICATION_ID,notificationBuilder.build())
    }

    private fun stopForegroundService(){
        notificationManager.cancel(NOTIFICATION_ID)
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun createNotificationChannel(){
        val notificationChannel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(notificationChannel)
    }

    private fun updateNotification(hours:String,minutes:String,second:String){
        notificationManager.notify(
            NOTIFICATION_ID,
            notificationBuilder
                .setContentText("$hours:$minutes:$second")
                .build()
        )
    }

    private fun startTimer(
        onTick:(h:String,m:String,s:String) -> Unit
    ){
        currentTimerState.value = TimerState.STARTED
        var previousSecond:Duration = ZERO
        timer = fixedRateTimer(initialDelay = 1000L, period = 1000L){
            duration = duration.plus(1.seconds)
            updateTimeUnits()
            if(duration!=previousSecond){
                previousSecond = duration
                onTick(hours.value,minutes.value,seconds.value)
            }
        }
    }

    private fun stopTimer(){
        if(this::timer.isInitialized){
            timer.cancel()
        }
        currentTimerState.value = TimerState.STOPPED
    }

    private fun cancelTimer(){
        duration = ZERO
        updateTimeUnits()
        currentTimerState.value = TimerState.IDLE
    }

    private fun updateTimeUnits(){
        duration.toComponents { hours, minutes, seconds, _ ->
            this@StudyTimerService.hours.value = hours.toInt().pad()
            this@StudyTimerService.minutes.value = minutes.pad()
            this@StudyTimerService.seconds.value = seconds.pad()
        }
    }

    inner class StudySessionTimerBinder:Binder(){
        fun getService():StudyTimerService = this@StudyTimerService
    }

}

enum class TimerState{
    IDLE,
    STARTED,
    STOPPED
}



//@AndroidEntryPoint
//class StudyTimerService : Service() {
//
//    @Inject
//    lateinit var notificationManager: NotificationManager
//
//    @Inject
//    lateinit var notificationBuilder: NotificationCompat.Builder
//
//    private val binder = StudySessionTimerBinder()
//
//    private lateinit var timer: Timer
//
//    var duration: Duration = Duration.ZERO
//        private set
//
//    var seconds = mutableStateOf("00")
//        private set
//    var minutes = mutableStateOf("00")
//        private set
//    var hours = mutableStateOf("00")
//        private set
//
//    var currentTimerState = mutableStateOf(TimerState.IDLE)
//        private set
//
//    override fun onBind(p0: Intent?) = binder
//
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        intent?.action.let { action ->
//            when (action) {
//                ACTION_SERVICE_START -> {
//                    startForegroundService()
//                    startTimer { hours, minute, seconds ->
//                        updateNotification(hours, minute, seconds)
//                    }
//                }
//                ACTION_SERVICE_STOP -> {
//                    stopTimer()
//                }
//                ACTION_SERVICE_CANCEL -> {
//                    stopTimer()
//                    cancelTimer()
//                    stopForegroundService()
//                }
//                ACTION_SERVICE_FINISH -> {
//                    stopTimer()
//                    stopForegroundService()
//                }
//                ACTION_SERVICE_RESUME -> {
//                    resumeTimer { hours, minute, seconds ->
//                        updateNotification(hours, minute, seconds)
//                    }
//                }
//            }
//        }
//        return super.onStartCommand(intent, flags, startId)
//    }
//
//    @SuppressLint("ForegroundServiceType")
//    private fun startForegroundService() {
//        createNotificationChannel()
//        startForeground(NOTIFICATION_ID, buildNotification())
//    }
//
//    private fun stopForegroundService() {
//        notificationManager.cancel(NOTIFICATION_ID)
//        stopForeground(STOP_FOREGROUND_REMOVE)
//        stopSelf()
//    }
//
//    private fun createNotificationChannel() {
//        val notificationChannel = NotificationChannel(
//            NOTIFICATION_CHANNEL_ID,
//            NOTIFICATION_CHANNEL_NAME,
//            NotificationManager.IMPORTANCE_LOW
//        )
//        notificationManager.createNotificationChannel(notificationChannel)
//    }
//
//    private fun updateNotification(hours: String, minutes: String, second: String) {
//        notificationManager.notify(
//            NOTIFICATION_ID,
//            buildNotification().setContentText("$hours:$minutes:$second")
//        )
//    }
//
//    private fun buildNotification(): NotificationCompat.Builder {
//        val stopIntent = Intent(this, StudyTimerService::class.java).apply {
//            action = ACTION_SERVICE_STOP
//        }
//        val stopPendingIntent = PendingIntent.getService(this, 0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT)
//
//        val cancelIntent = Intent(this, StudyTimerService::class.java).apply {
//            action = ACTION_SERVICE_CANCEL
//        }
//        val cancelPendingIntent = PendingIntent.getService(this, 0, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT)
//
//        val finishIntent = Intent(this, StudyTimerService::class.java).apply {
//            action = ACTION_SERVICE_FINISH
//        }
//        val finishPendingIntent = PendingIntent.getService(this, 0, finishIntent, PendingIntent.FLAG_UPDATE_CURRENT)
//
//        val resumeIntent = Intent(this, StudyTimerService::class.java).apply {
//            action = ACTION_SERVICE_RESUME
//        }
//        val resumePendingIntent = PendingIntent.getService(this, 0, resumeIntent, PendingIntent.FLAG_UPDATE_CURRENT)
//
//        return notificationBuilder
//            .addAction(R.drawable.ic_stop, "Stop", stopPendingIntent)
//            .addAction(R.drawable.ic_cancel, "Cancel", cancelPendingIntent)
//            .addAction(R.drawable.ic_finish, "Finish", finishPendingIntent)
//            .addAction(R.drawable.ic_resume, "Resume", resumePendingIntent)
//    }
//
//    private fun startTimer(onTick: (h: String, m: String, s: String) -> Unit) {
//        currentTimerState.value = TimerState.STARTED
//        timer = fixedRateTimer(initialDelay = 1000L, period = 1000L) {
//            duration = duration.plus(1.seconds)
//            updateTimeUnits()
//            onTick(hours.value, minutes.value, seconds.value)
//        }
//    }
//
//    private fun stopTimer() {
//        if (this::timer.isInitialized) {
//            timer.cancel()
//        }
//        currentTimerState.value = TimerState.STOPPED
//    }
//
//    private fun cancelTimer() {
//        duration = Duration.ZERO
//        updateTimeUnits()
//        currentTimerState.value = TimerState.IDLE
//    }
//
//    private fun resumeTimer(onTick: (h: String, m: String, s: String) -> Unit) {
//        currentTimerState.value = TimerState.STARTED
//        timer = fixedRateTimer(initialDelay = 1000L, period = 1000L) {
//            duration = duration.plus(1.seconds)
//            updateTimeUnits()
//            onTick(hours.value, minutes.value, seconds.value)
//        }
//    }
//
//    private fun updateTimeUnits() {
//        duration.toComponents { hours, minutes, seconds, _ ->
//            this@StudyTimerService.hours.value = hours.toInt().pad()
//            this@StudyTimerService.minutes.value = minutes.pad()
//            this@StudyTimerService.seconds.value = seconds.pad()
//        }
//    }
//
//    inner class StudySessionTimerBinder : Binder() {
//        fun getService(): StudyTimerService = this@StudyTimerService
//    }
//}
//
//enum class TimerState {
//    IDLE,
//    STARTED,
//    STOPPED
//}
