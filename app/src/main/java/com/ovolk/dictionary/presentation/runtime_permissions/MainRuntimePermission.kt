package com.ovolk.dictionary.presentation.runtime_permissions

import android.Manifest
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ovolk.dictionary.R
import com.ovolk.dictionary.presentation.MainActivity

@Composable
fun MainRuntimePermission() {
    val appSettingsRepository = MainActivity.getMainActivity().appSettingsRepository
    fun setPermissionWasRequestedOnce() {
        appSettingsRepository.setAppSettings().apply {
            addPermissionWasRequestedOnce(Manifest.permission.POST_NOTIFICATIONS)
            update()
        }
    }

    val permissionList = listOf(
        RequiredPermission(
            permission = Manifest.permission.POST_NOTIFICATIONS,
            title = stringResource(id = R.string.runtime_permission_reminder_push_title),
            description = stringResource(id = R.string.runtime_permission_reminder_push_message),
            requiredVersion = Build.VERSION_CODES.TIRAMISU,
            onRejected = { setPermissionWasRequestedOnce() },
            onDecline = { setPermissionWasRequestedOnce() },
            onGranted = { setPermissionWasRequestedOnce() }
        ),
    ).filter { !appSettingsRepository.getAppSettings().permissionsWasRequestedOnce.contains(it.permission) && Build.VERSION.SDK_INT >= it.requiredVersion }

    RuntimePermissions(permissionList = permissionList)

}