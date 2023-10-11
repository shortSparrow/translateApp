package com.ovolk.dictionary.domain.use_case.exam_reminder_permisison

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationManagerCompat
import com.ovolk.dictionary.data.workers.AlarmReceiver
import com.ovolk.dictionary.presentation.DictionaryApp
import com.ovolk.dictionary.presentation.MainActivity


class ExamReminderPermission {
    fun goToPushSettings() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra(
                "android.provider.extra.APP_PACKAGE",
                DictionaryApp.applicationContext().packageName
            )
            MainActivity.getMainActivity().startActivity(intent)
        } else {
            val intent = Intent()
            intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
            intent.putExtra("app_package", DictionaryApp.applicationContext().packageName)
            intent.putExtra("app_uid", DictionaryApp.applicationContext().applicationInfo.uid)
        }
    }


    fun goToPushChanelSettings(chanelId: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS).apply {
                putExtra(Settings.EXTRA_APP_PACKAGE, DictionaryApp.applicationContext().packageName)
                putExtra(Settings.EXTRA_CHANNEL_ID, chanelId)
            }
            MainActivity.getMainActivity().startActivity(intent)
        }
    }


    fun isChannelEnable(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = DictionaryApp.applicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val chanel: NotificationChannel? =
                notificationManager.getNotificationChannel(AlarmReceiver.CHANNEL_ID)

            // chanel not created yet (user was not receive first push notification)
            if (chanel == null) {
                return true
            }

            val isChannelEnable = chanel.importance != NotificationManager.IMPORTANCE_NONE
            isChannelEnable
        } else {
            true
        }
    }

    fun isNotificationEnableBeforeAndroid13(): Boolean {
        val areNotificationsEnabled =
            NotificationManagerCompat.from(DictionaryApp.applicationContext())
                .areNotificationsEnabled()
        if (!areNotificationsEnabled) return false
        return true
    }
}