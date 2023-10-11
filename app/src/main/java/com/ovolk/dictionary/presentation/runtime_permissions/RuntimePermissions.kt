package com.ovolk.dictionary.presentation.runtime_permissions

import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import com.ovolk.dictionary.presentation.MainActivity
import com.ovolk.dictionary.presentation.core.dialog.confirm_dialog.ConfirmDialog

data class RequiredPermission(
    val permission: String, // Manifest
    val title: String,
    val description: String,
    val requiredVersion: Int,
    val onGranted: () -> Unit = {},
    val onRejected: () -> Unit = {},
    val onRejectedPermanent: () -> Unit = {},
    val onDecline: () -> Unit = {},
)


@Composable
fun RuntimePermissions(
    permissionList: List<RequiredPermission>
) {
    val context = LocalContext.current
    var currentPermissionIndex by remember {
        mutableStateOf(0)
    }
    var isDialogOpen by remember { mutableStateOf(false) }
    fun isEnd() = currentPermissionIndex > (permissionList.size - 1)


    LaunchedEffect(currentPermissionIndex) {
        if (isEnd()) {
            return@LaunchedEffect
        }
        when {
            ContextCompat.checkSelfPermission(
                context,
                permissionList[currentPermissionIndex].permission
            ) == PackageManager.PERMISSION_GRANTED -> {
                currentPermissionIndex += 1
            }

            shouldShowRequestPermissionRationale(
                MainActivity.getMainActivity(),
                permissionList[currentPermissionIndex].permission
            ) -> {
                // second time
                isDialogOpen = true
            }

            else -> {
                // First time or when declined permanent
                isDialogOpen = true
            }
        }
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            permissionList[currentPermissionIndex].onGranted()
        } else {
            permissionList[currentPermissionIndex].onRejected()
        }

        if (!isGranted && !shouldShowRequestPermissionRationale(
                context as Activity,
                permissionList[currentPermissionIndex].permission
            )
        ) {
            permissionList[currentPermissionIndex].onRejectedPermanent()
        }

        currentPermissionIndex += 1
        isDialogOpen = false
    }

    if (isDialogOpen && !isEnd()) {
        val currentPermission = permissionList[currentPermissionIndex]
        ConfirmDialog(
            title = currentPermission.title,
            description = currentPermission.description,
            onAcceptClick = {
                requestPermissionLauncher.launch(currentPermission.permission)
            },
            onDeclineClick = {
                currentPermission.onDecline()
                isDialogOpen = false
                currentPermissionIndex += 1
            },
        )
    }
}