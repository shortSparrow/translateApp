package com.ovolk.dictionary.presentation.modify_word.compose.record_audio

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.ovolk.dictionary.R
import com.ovolk.dictionary.presentation.modify_word.RecordAudioAction
import com.ovolk.dictionary.presentation.modify_word.RecordAudioState
import com.ovolk.dictionary.util.compose.click_effects.withoutEffectClick
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun RecordAudioWrapper(
    word: String,
    recordState: RecordAudioState,
    onAction: (RecordAudioAction) -> Unit
) {
    val bottomSheetScaffoldState by remember { mutableStateOf(BottomSheetState(BottomSheetValue.Collapsed)) }
    val coroutineScope = rememberCoroutineScope()
    var isSlideUpComplete by remember { mutableStateOf(false) }

    fun closeModal() {
        coroutineScope.launch {
            onAction(RecordAudioAction.HideBottomSheet)
            bottomSheetScaffoldState.collapse()
            isSlideUpComplete = false

        }
    }

    LaunchedEffect(recordState.isModalOpen) {
        if (recordState.isModalOpen) {
            coroutineScope.launch {
                delay(100)
                bottomSheetScaffoldState.expand()
                isSlideUpComplete = true
            }
        }
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
                contentDescription = "change record",
                modifier = Modifier
                    .size(60.dp)
                    .withoutEffectClick { onAction(RecordAudioAction.OpenBottomSheet) }
            )
            Text(
                text = stringResource(id = R.string.audio_has_been_added),
                color = colorResource(id = R.color.green_2)
            )
        } else {
            Icon(
                painter = painterResource(id = R.drawable.mic_active),
                contentDescription = "add record",
                tint = colorResource(id = R.color.blue),
                modifier = Modifier
                    .size(50.dp)
                    .withoutEffectClick { onAction(RecordAudioAction.OpenBottomSheet) },
                )
        }
    }


    if (recordState.isModalOpen) {
        Dialog(
            onDismissRequest = { closeModal() },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .withoutEffectClick { closeModal() },
            ) {
                BottomSheetScaffold(
                    backgroundColor = Color.Transparent,
                    scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = bottomSheetScaffoldState),
                    modifier = Modifier.padding(top = 70.dp),
                    sheetContent = {
                        if (bottomSheetScaffoldState.isCollapsed && isSlideUpComplete) {
                            closeModal()
                        }

                        RecordAudio(
                            word = word,
                            recordState = recordState,
                            onAction = onAction,
                            closeModal = ::closeModal
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