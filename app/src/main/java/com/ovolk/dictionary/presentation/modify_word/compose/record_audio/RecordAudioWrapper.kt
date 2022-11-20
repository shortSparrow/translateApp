package com.ovolk.dictionary.presentation.modify_word.compose.record_audio

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import com.ovolk.dictionary.R
import com.ovolk.dictionary.presentation.DictionaryApp
import com.ovolk.dictionary.presentation.core.dialog.ConfirmDialog
import com.ovolk.dictionary.presentation.core.dialog.ConfirmDialogType
import com.ovolk.dictionary.presentation.modify_word.RecordAudioAction
import com.ovolk.dictionary.presentation.modify_word.RecordAudioState
import com.ovolk.dictionary.util.compose.OnLifecycleEvent
import com.ovolk.dictionary.util.compose.click_effects.clickWithoutFeedback
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun RecordAudioWrapper(
    word: String,
    recordState: RecordAudioState,
    onAction: (RecordAudioAction) -> Unit
) {
    val context = LocalContext.current
    var isPermissionDeniedDialog by remember {
        mutableStateOf(false)
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            onAction(RecordAudioAction.OpenBottomSheet)
        } else {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(
                    context as Activity,
                    Manifest.permission.RECORD_AUDIO
                )
            ) {
                // PERMISSION DENIED FOREVER
                isPermissionDeniedDialog = true
            }
        }
    }


    OnLifecycleEvent { owner, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                val permissionCheckResult =
                    ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
                if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                    isPermissionDeniedDialog = false
                }
            }
            else -> {}
        }
    }

    val bottomSheetScaffoldState by remember { mutableStateOf(BottomSheetState(BottomSheetValue.Collapsed)) }
    val coroutineScope = rememberCoroutineScope()
    var isSlideUpComplete by remember { mutableStateOf(false) }

    fun closeBottomSheetModal() {
        coroutineScope.launch {
            bottomSheetScaffoldState.collapse()
            delay(100) // time for slide down bottomSheet
            onAction(RecordAudioAction.HideBottomSheet)
            isSlideUpComplete = false
        }
    }

    fun checkAndRequestCameraPermission(
        context: Context,
        permission: String,
        launcher: ManagedActivityResultLauncher<String, Boolean>
    ) {
        val permissionCheckResult = ContextCompat.checkSelfPermission(context, permission)
        if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
            onAction(RecordAudioAction.OpenBottomSheet)
        } else {
            launcher.launch(permission)
        }
    }

    fun closePermissionDeniedDialog() {
        isPermissionDeniedDialog = false
    }

    fun goToSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.flags = FLAG_ACTIVITY_NEW_TASK
        val uri: Uri =
            Uri.fromParts("package", DictionaryApp.applicationContext().packageName, null)
        intent.data = uri
        DictionaryApp.applicationContext().startActivity(intent);
    }

    LaunchedEffect(recordState.isModalOpen) {
        if (recordState.isModalOpen) {
            coroutineScope.launch {
                delay(10)
                bottomSheetScaffoldState.expand()
                isSlideUpComplete = true
            }
        }
    }

    if (isPermissionDeniedDialog) {
        ConfirmDialog(
            question = stringResource(id = R.string.modify_word_enable_audio_permission),
            onAcceptClick = ::goToSettings,
            onDismissRequest = ::closePermissionDeniedDialog,
            onDeclineClick = ::closePermissionDeniedDialog,
            confirmButtonText = "GO TO SETTINGS",
            type = ConfirmDialogType.NO_RED
        )
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = dimensionResource(id = R.dimen.gutter))
    ) {
        if (recordState.isRecordExist) {
            Image(
                painter = painterResource(id = R.drawable.mic_successful),
                contentDescription = stringResource(id = R.string.modify_word_change_record_cd),
                modifier = Modifier
                    .size(60.dp)
                    .clickWithoutFeedback { onAction(RecordAudioAction.OpenBottomSheet) }
            )
            Text(
                text = stringResource(id = R.string.audio_has_been_added),
                color = colorResource(id = R.color.green_2)
            )
        } else {
            Icon(
                painter = painterResource(id = R.drawable.mic_active),
                contentDescription = stringResource(id = R.string.modify_word_add_new_record_cd),
                tint = colorResource(id = R.color.blue),
                modifier = Modifier
                    .size(50.dp)
                    .clickWithoutFeedback {
                        checkAndRequestCameraPermission(
                            context,
                            Manifest.permission.RECORD_AUDIO,
                            launcher
                        )
                    }
            )
        }
    }


    if (recordState.isModalOpen) {
        Dialog(
            onDismissRequest = { closeBottomSheetModal() },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clickWithoutFeedback { closeBottomSheetModal() },
            ) {
                BottomSheetScaffold(
                    backgroundColor = Color.Transparent,
                    scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = bottomSheetScaffoldState),
                    modifier = Modifier.padding(top = 70.dp),
                    sheetContent = {
                        if (bottomSheetScaffoldState.isCollapsed && isSlideUpComplete) {
                            closeBottomSheetModal()
                        }

                        RecordAudio(
                            word = word,
                            recordState = recordState,
                            onAction = onAction,
                        )

                    },
                    sheetPeekHeight = 0.dp
                ) {
                    Column {}
                }
            }
        }
    }
}

@Preview
@Composable
fun RecordAudioWrapperPreview() {
    RecordAudioWrapper(
        word = "Estimate",
        recordState = RecordAudioState(isTempRecordExist = false),
        onAction = {},
    )
}

@Preview
@Composable
fun RecordAudioWrapperPreview2() {
    RecordAudioWrapper(
        word = "Estimate",
        recordState = RecordAudioState(isTempRecordExist = true),
        onAction = {},
    )
}