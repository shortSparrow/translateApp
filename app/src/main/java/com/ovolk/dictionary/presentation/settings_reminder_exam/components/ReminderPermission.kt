package com.ovolk.dictionary.presentation.settings_reminder_exam.components

import android.Manifest
import android.os.Build
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.material.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import com.ovolk.dictionary.R
import com.ovolk.dictionary.data.workers.AlarmReceiver
import com.ovolk.dictionary.domain.snackbar.GlobalSnackbarManger
import com.ovolk.dictionary.domain.use_case.exam_reminder_permisison.ExamReminderPermission
import com.ovolk.dictionary.presentation.DictionaryApp
import com.ovolk.dictionary.presentation.core.dialog.info_dialog.InfoDialog
import com.ovolk.dictionary.presentation.core.no_permission.NoPermission
import com.ovolk.dictionary.presentation.core.snackbar.SnackBarAlert
import com.ovolk.dictionary.presentation.runtime_permissions.UseRequestPermission
import com.ovolk.dictionary.util.compose.OnLifecycleEvent


@Composable
fun ReminderPermission() {
    val examReminderPermission = remember {
        ExamReminderPermission()
    }
    var isHelpModalOpen by remember {
        mutableStateOf(false)
    }
    var isReminderPermissionGranted: Boolean? by remember {
        mutableStateOf(null)
    }
    var isReminderPushChanelDisabled: Boolean? by remember {
        mutableStateOf(null)
    }
    var request: ManagedActivityResultLauncher<String, Boolean>? by remember {
        mutableStateOf(null)
    }

    fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            request?.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            examReminderPermission.goToPushSettings()
        }
    }

    fun checkPermissionForAboveAndroid13() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            val isNotificationEnable = examReminderPermission.isNotificationEnableBeforeAndroid13()
            val isChannelEnable = examReminderPermission.isChannelEnable()

            isReminderPermissionGranted = isNotificationEnable
            isReminderPushChanelDisabled = !isChannelEnable
        }
    }

    OnLifecycleEvent { owner, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                checkPermissionForAboveAndroid13()
            }

            else -> {}
        }
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        UseRequestPermission(
            permission = Manifest.permission.POST_NOTIFICATIONS,
            onResume = { isGranted ->
                if (isGranted) {
                    GlobalSnackbarManger.hideGlobalSnackbar()
                    isReminderPermissionGranted = true
                    isReminderPushChanelDisabled = !examReminderPermission.isChannelEnable()
                }
            },
            onCheckGranted = {
                isReminderPermissionGranted = true
                isReminderPushChanelDisabled = !examReminderPermission.isChannelEnable()
            },
            onCheckReject = { requestPermissionLauncher ->
                isReminderPermissionGranted = false
                request = requestPermissionLauncher
            },
            onGranted = { isReminderPermissionGranted = true },
            onRejectPermanent = {
                GlobalSnackbarManger.showGlobalSnackbar(
                    duration = SnackbarDuration.Long,
                    data = SnackBarAlert(
                        message = DictionaryApp.applicationContext()
                            .getString(R.string.exam_reminder_no_permission_permanent_message),
                        action = { examReminderPermission.goToPushSettings() },
                        actionTitle = DictionaryApp.applicationContext()
                            .getString(R.string.exam_reminder_no_permission_permanent_action),
                    ),
                )
            },
        )
    }

    if (isHelpModalOpen) {
        InfoDialog(
            onDismissRequest = { isHelpModalOpen = false },
            message = stringResource(id = R.string.exam_reminder_no_permission_help_dialog_title),
            description = stringResource(id = R.string.exam_reminder_no_permission_help_dialog_message),
            buttonText = stringResource(id = R.string.ok),
            onClick = { isHelpModalOpen = false }
        )
    }

    if (isReminderPermissionGranted == false) {
        NoPermission(
            message = stringResource(id = R.string.exam_reminder_no_permission_title),
            buttonText = stringResource(id = R.string.exam_reminder_no_permission_request_button),
            onProvidePermissionClick = { requestPermission() },
            withHelpIcon = true,
            onHelpIconClick = { isHelpModalOpen = true },
        )
    }

    if (isReminderPermissionGranted == true && isReminderPushChanelDisabled == true) {
        NoPermission(
            message = stringResource(id = R.string.exam_reminder_push_chanel_disable_title),
            buttonText = stringResource(id = R.string.exam_reminder_push_chanel_disable_button),
            onProvidePermissionClick = { examReminderPermission.goToPushChanelSettings(AlarmReceiver.CHANNEL_ID) },
            withHelpIcon = true,
            onHelpIconClick = { isHelpModalOpen = true },
        )
    }
}


@Preview(showBackground = true)
@Composable
fun ReminderPermissionPreview() {
    ReminderPermission()
}
