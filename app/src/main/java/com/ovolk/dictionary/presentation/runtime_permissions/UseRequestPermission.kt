package com.ovolk.dictionary.presentation.runtime_permissions

import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import com.ovolk.dictionary.util.compose.OnLifecycleEvent

@Composable
fun UseRequestPermission(
    permission: String,
    onResume: (isGranted: Boolean) -> Unit = {},
    onCheckGranted: () -> Unit = {},
    onCheckReject: (d: ManagedActivityResultLauncher<String, Boolean>) -> Unit = {},
    onGranted: () -> Unit = {},
    onReject: () -> Unit = {},
    onRejectPermanent: () -> Unit = {},
) {
    val context = LocalContext.current

    OnLifecycleEvent { owner, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                val permissionCheckResult =
                    ContextCompat.checkSelfPermission(
                        context,
                        permission
                    )
                onResume(permissionCheckResult == PackageManager.PERMISSION_GRANTED)
            }

            else -> {}
        }
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            onGranted()
        }

        if (!isGranted) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(
                    context as Activity,
                    permission
                )
            ) {
                onRejectPermanent()
            } else {
                onReject()
            }
        }
    }


    LaunchedEffect(Unit) {
        when {
            ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED -> {
                onCheckGranted()
            }

            else -> {
                // First time or when declined permanent
                onCheckReject(requestPermissionLauncher)
            }
        }
    }

    // Without component preview crashes
    Box {

    }
}

