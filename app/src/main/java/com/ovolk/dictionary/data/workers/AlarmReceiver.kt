package com.ovolk.dictionary.data.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.ContentResolver.SCHEME_ANDROID_RESOURCE
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.net.toUri
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.ExamReminder
import com.ovolk.dictionary.presentation.DictionaryApp
import com.ovolk.dictionary.presentation.MainActivity
import com.ovolk.dictionary.presentation.navigation.graph.MainTabRotes
import com.ovolk.dictionary.util.DEEP_LINK_BASE
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {
    @Inject
    lateinit var examReminder: ExamReminder

    private var soundUri =
        Uri.parse("$SCHEME_ANDROID_RESOURCE://${DictionaryApp.applicationContext().packageName}/${R.raw.reminder_sound}");

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            val notificationManager = getSystemService(
                context,
                NotificationManager::class.java
            ) as NotificationManager
            createNotificationChannel(notificationManager)

            val deepLinkIntent = Intent(
                Intent.ACTION_VIEW,
                "${DEEP_LINK_BASE}/${MainTabRotes.EXAM}".toUri(),
                context,
                MainActivity::class.java
            )
            val pendingIntent: PendingIntent? = TaskStackBuilder.create(context).run {
                addNextIntentWithParentStack(deepLinkIntent)
                getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
            }

            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(context.getString(R.string.reminder_push_exam_title))
                .setContentText(context.getString(R.string.reminder_push_exam_description))
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(context.getString(R.string.reminder_push_exam_description))
                )
                .setSmallIcon(R.drawable.ic_notification_round)
                .setColor(context.getColor(R.color.light_blue))
                .setAutoCancel(true)
                .setSound(soundUri)
                .setLights(Color.WHITE, 5000, 5000)
                .setContentIntent(pendingIntent)
                .build()

            notificationManager.notify(NOTIFICATION_ID, notification)
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
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()

            notificationChanel.enableLights(true)
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