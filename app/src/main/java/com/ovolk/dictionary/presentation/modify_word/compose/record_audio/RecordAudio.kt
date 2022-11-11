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
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RecordAudio(
    word: String,
    recordState: RecordAudioState,
    onAction: (RecordAudioAction) -> Unit,
) {
    val deleteIconColor =
        if (recordState.isTempRecordExist) colorResource(id = R.color.red) else colorResource(id = R.color.grey)

    val micIconColor =
        if (recordState.isRecording) colorResource(id = R.color.blue) else colorResource(id = R.color.black)

    val listenIsDisabled =
        !recordState.isTempRecordExist || recordState.isRecordPlaying
    val listenColor =
        if (listenIsDisabled) colorResource(id = R.color.grey) else colorResource(id = R.color.blue)

    val saveIsDisabled =
        recordState.isRecordPlaying || recordState.isRecording || (!recordState.isRecordExist && !recordState.isTempRecordExist)
    val savedColor =
        if (saveIsDisabled) colorResource(id = R.color.grey) else colorResource(id = R.color.blue)

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.record_animation))

    var recordDuration by remember { mutableStateOf(recordState.existingRecordDuration / 1000) }
    var timer by remember { mutableStateOf(0) }

    LaunchedEffect(recordState.isRecording) {
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

    LaunchedEffect(recordState.existingRecordDuration) {
        recordDuration = (recordState.existingRecordDuration / 1000).toInt()
        timer = recordDuration
    }

    Column(
        Modifier
            .fillMaxWidth()
            .height(300.dp)
            .withoutEffectClick { },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(modifier = Modifier.padding(bottom = 30.dp, top = 20.dp), text = word)

        ConstraintLayout(Modifier.fillMaxWidth()) {
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
                contentDescription = stringResource(id = R.string.modify_word_record_delete_cd),
                modifier = Modifier
                    .constrainAs(delete) {
                        start.linkTo(listen.start)
                        end.linkTo(listen.end)
                        top.linkTo(parent.top)
                    }
                    .opacityClick(isDisabled = !recordState.isTempRecordExist) {
                        onAction(RecordAudioAction.DeleteRecord)
                    },
                tint = deleteIconColor,
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
                modifier = Modifier
                    .constrainAs(mic) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top, margin = 60.dp)
                    }
                    .pointerInteropFilter {
                        when (it.action) {
                            MotionEvent.ACTION_DOWN -> onAction(RecordAudioAction.StartRecording)
                            MotionEvent.ACTION_UP -> onAction(RecordAudioAction.StopRecording)
                            MotionEvent.ACTION_CANCEL -> onAction(RecordAudioAction.StopRecording)
                            else -> {}
                        }
                        true
                    },
            ) {
                Column(
                    modifier = Modifier
                        .background(colorResource(id = R.color.yellow))
                        .padding(20.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.mic_active),
                        contentDescription = stringResource(id = R.string.modify_word_record_cd),
                        modifier = Modifier.size(50.dp),
                        tint = micIconColor
                    )
                }
            }

            Text(
                text = stringResource(id = R.string.modify_word_record_listen).uppercase(),
                color = listenColor,
                modifier = Modifier
                    .constrainAs(listen) {
                        top.linkTo(mic.top)
                        bottom.linkTo(mic.bottom)
                        start.linkTo(mic.end, margin = 30.dp)
                    }
                    .opacityClick(isDisabled = listenIsDisabled) {
                        onAction(RecordAudioAction.ListenRecord)
                    })

            Text(
                text = stringResource(id = R.string.save).uppercase(),
                color = savedColor,
                modifier = Modifier
                    .constrainAs(save) {
                        top.linkTo(mic.bottom, margin = 30.dp)
                        start.linkTo(mic.start)
                        end.linkTo(mic.end)
                    }
                    .opacityClick(isDisabled = saveIsDisabled) {
                        onAction(RecordAudioAction.SaveRecord)
                    })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecordAudioPreview() {
    RecordAudio(
        word = "Word Example",
        recordState = RecordAudioState(),
        onAction = {},
    )
}