package com.ovolk.dictionary.presentation.modify_word.compose.record_audio

import android.view.MotionEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.ovolk.dictionary.R
import com.ovolk.dictionary.presentation.modify_word.RecordAudioAction
import com.ovolk.dictionary.presentation.modify_word.RecordAudioState
import com.ovolk.dictionary.util.compose.click_effects.opacityClick
import com.ovolk.dictionary.util.compose.click_effects.withoutEffectClick
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun RecordAudio(
    word: String,
    recordState: RecordAudioState,
    onAction: (RecordAudioAction) -> Unit
) {
    var recordDuration by remember {
        mutableStateOf(recordState.existingRecordDuration / 1000)
    }
    var timer by remember {
        mutableStateOf(0)
    }

//    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
//        bottomSheetState = BottomSheetState(BottomSheetValue.Expanded)
//    )

    val bottomSheetScaffoldState by remember {
        mutableStateOf(BottomSheetState(BottomSheetValue.Collapsed))
    }
    val coroutineScope = rememberCoroutineScope()

    var isOpenModal by remember {
        mutableStateOf(false)
    }
    var isSlideUpComplete by remember {
        mutableStateOf(false)
    }

    fun closeModal() {
        coroutineScope.launch {
            bottomSheetScaffoldState.collapse()
            isOpenModal = false
            isSlideUpComplete = false
        }
    }

    LaunchedEffect(recordState.isRecording) {
//        if (recordState.isRecording) {
//            recordDuration = 0
//            timer = 0
//        }
        while (recordState.isRecording) {
            delay(1000)
            recordDuration++
            timer = recordDuration
        }
    }
    LaunchedEffect(recordState.isRecordPlaying) {
        while (recordState.isRecordPlaying) {
            delay(1000)
            timer -= 1
        }
        timer = recordDuration
    }

//    LaunchedEffect(recordState.isRecordExist) {
//        if (!recordState.isRecordExist) {
//            recordDuration = 0
//            timer = 0
//        }
//    }

    LaunchedEffect(recordState.existingRecordDuration) {
        recordDuration = (recordState.existingRecordDuration / 1000).toInt()
        timer = recordDuration

    }


    LaunchedEffect(isOpenModal) {
        if (isOpenModal) {
            coroutineScope.launch {
                delay(100)
                bottomSheetScaffoldState.expand()
                isSlideUpComplete = true
            }
        }
    }



    Button(onClick = { isOpenModal = true }) {
        Text(text = "Record")
    }

    if (isOpenModal) {
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
                        if (
                            bottomSheetScaffoldState.isCollapsed && isSlideUpComplete
                        ) {
                            closeModal()
                        }
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                                .withoutEffectClick { },
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "the Word")

                            val iconColor = if (recordState.isRecordExist) {
                                colorResource(id = R.color.red)
                            } else {
                                colorResource(id = R.color.grey)
                            }
                            ConstraintLayout(
                                Modifier
                                    .fillMaxWidth()
//                                    .background(Color.Red)
                            ) {
                                val (timerRef, delete, listen, save, mic, lottieAnim) = createRefs()

                                Text(
                                    text = timer.toString(),
                                    modifier = Modifier.constrainAs(timerRef) {
                                        start.linkTo(mic.start)
                                        end.linkTo(mic.end)
                                        top.linkTo(parent.top)
                                    },
                                )
                                Icon(
                                    painter = painterResource(id = R.drawable.delete_active),
                                    contentDescription = "delete",
                                    modifier = Modifier
                                        .constrainAs(delete) {
                                            start.linkTo(listen.start)
                                            end.linkTo(listen.end)
                                            top.linkTo(parent.top)
                                        }
                                        .opacityClick(isDisabled = !recordState.isRecordExist) {
                                            onAction(RecordAudioAction.DeleteRecord)
                                        },
                                    tint = iconColor,
                                )

                                val micIconColor = if (recordState.isRecording) {
                                    colorResource(id = R.color.blue)
                                } else {
                                    colorResource(id = R.color.black)
                                }

                                val textColor =
                                    if (recordState.isRecordExist && !recordState.isRecording) {
                                        colorResource(id = R.color.blue)
                                    } else {
                                        colorResource(id = R.color.grey)
                                    }

                                val composition by rememberLottieComposition(
                                    LottieCompositionSpec.RawRes(
                                        R.raw.record_animation
                                    )
                                )






                                if (recordState.isRecording) {
                                    LottieAnimation(
                                        composition = composition,
                                        iterations = LottieConstants.IterateForever,
                                        modifier = Modifier
                                            .size(150.dp)
                                            .constrainAs(lottieAnim) {
                                                start.linkTo(mic.start)
                                                end.linkTo(mic.end)
                                                bottom.linkTo(mic.bottom)
                                                top.linkTo(mic.top)
                                            }
                                    )
                                }

                                Surface(
                                    shape = CircleShape,
                                    modifier = Modifier.constrainAs(mic) {
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                        top.linkTo(parent.top, margin = 70.dp)
                                    },
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .background(Color.Yellow)
                                            .padding(20.dp)

                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.mic_active),
                                            contentDescription = "delete",
                                            modifier = Modifier
                                                .size(50.dp)
                                                .pointerInteropFilter {
                                                    when (it.action) {
                                                        MotionEvent.ACTION_DOWN -> {
                                                            onAction(RecordAudioAction.StartRecording)
                                                        }
                                                        MotionEvent.ACTION_UP -> {
                                                            onAction(RecordAudioAction.StopRecording)
                                                        }
                                                        else -> false
                                                    }
                                                    true
                                                },
                                            tint = micIconColor

                                        )
                                    }
                                }

                                Text(
                                    text = "LISTEN",
                                    color = textColor,
                                    modifier = Modifier
                                        .constrainAs(listen) {
                                            top.linkTo(mic.top)
                                            bottom.linkTo(mic.bottom)
                                            start.linkTo(mic.end, margin = 30.dp)
                                        }
                                        .opacityClick(isDisabled = !recordState.isRecordExist || recordState.isRecordPlaying) {
                                            onAction(
                                                RecordAudioAction.ListenRecord
                                            )
                                        })

                                Text(
                                    text = "SAVE",
                                    color = textColor,
                                    modifier = Modifier
                                        .constrainAs(save) {
                                            top.linkTo(mic.bottom, margin = 30.dp)
                                            start.linkTo(mic.start)
                                            end.linkTo(mic.end)
                                        }
                                        .opacityClick(isDisabled = !recordState.isRecordExist || recordState.isRecordPlaying) {
                                            onAction(RecordAudioAction.SaveRecord)
                                            closeModal()
                                        })
                            }
                        }

                    },
                    sheetPeekHeight = 0.dp
                ) {
                    Column() {

                    }

                }
            }
        }
    }
}