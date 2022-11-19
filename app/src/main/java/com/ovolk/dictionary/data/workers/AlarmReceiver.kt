package com.ovolk.dictionary.data.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.NavDeepLinkBuilder
import com.ovolk.dictionary.R
import com.ovolk.dictionary.presentation.MainActivity
import com.ovolk.dictionary.presentation.exam.ExamReminder
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {
    @Inject
    lateinit var examReminder: ExamReminder
    private val soundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            val notificationManager = getSystemService(
                context,
                NotificationManager::class.java
            ) as NotificationManager
            createNotificationChannel(notificationManager)

//            val pendingIntent = NavDeepLinkBuilder(context)
//                .setComponentName(MainActivity::class.java)
//                .setGraph(R.navigation.app_navigation)
//                .setDestination(R.id.examKnowledgeWordsFragment)
//                .createPendingIntent()

//            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
//                .setContentTitle(context.getString(R.string.reminder_push_exam_title))
//                .setContentText(context.getString(R.string.reminder_push_exam_description))
//                .setStyle(
//                    NotificationCompat.BigTextStyle()
//                        .bigText(context.getString(R.string.reminder_push_exam_description))
//                )
//                .setSmallIcon(R.drawable.ic_notification_round)
//                .setColor(context.getColor(R.color.light_blue))
//                .setAutoCancel(true)
//                .setSound(soundUri)
//                .setVibrate(longArrayOf(0, 500, 100))
//                .setContentIntent(pendingIntent)
//                .build()
//
//            notificationManager.notify(NOTIFICATION_ID, notification)

//            // TODO maybe change on default repeat method
//            examReminder.repeatReminder()
        }
    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChanel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()

            notificationChanel.setSound(soundUri, audioAttributes)
            notificationManager.createNotificationChannel(notificationChanel)
        }
    }

    companion object {
        const val CHANNEL_ID = "exam_reminder_id"
        private const val CHANNEL_NAME = "Exam reminder"
        private const val NOTIFICATION_ID = 100

        fun newIntent(context: Context): Intent {
            return Intent(context, AlarmReceiver::class.java)
        }
    }
}