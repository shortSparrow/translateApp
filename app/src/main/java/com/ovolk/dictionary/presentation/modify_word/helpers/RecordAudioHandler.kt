package com.ovolk.dictionary.presentation.modify_word.helpers

import android.app.Application
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.ovolk.dictionary.presentation.modify_word.RecordAudioState
import com.ovolk.dictionary.util.helpers.generateFileName
import com.ovolk.dictionary.util.helpers.getAudioPath
import timber.log.Timber
import java.io.File
import java.io.IOException

private val TAG = "RecordAudioHandler"

class RecordAudioHandler(
    private val application: Application,
) {
    var recordState by mutableStateOf(RecordAudioState())
        private set

    private var modifiedFileName: String? = null
    private val audioManager = application.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private val oldVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

    val fileName by lazy { modifiedFileName ?: generateFileName() }
    private val audioPath by lazy { getAudioPath(application, fileName) }

    private var tempPath: String? = null
    private var savablePath: String? = null

    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null

    fun setModifiedFileName(name: String) {
        modifiedFileName = name
        setupPlayer()
        recordState =
            recordState.copy(isRecordExist = true, existingRecordDuration = player!!.duration)
    }

    fun startPlaying() {
        audioManager.setStreamVolume(
            AudioManager.STREAM_MUSIC,
            audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
            0
        )

        recordState = recordState.copy(isRecordPlaying = true)
        player?.start()
        player?.setOnCompletionListener {
            audioManager.setStreamVolume(
                AudioManager.STREAM_MUSIC,
                oldVolume,
                0
            )
            it.seekTo(0)
            recordState = recordState.copy(isRecordPlaying = false)
        }
    }

    private fun setupPlayer() {
        player = MediaPlayer().apply {
            try {
                reset()
                setDataSource(audioPath)
                prepare()
            } catch (e: IOException) {
                Timber.tag(TAG).e("prepare() failed $e")
            }
        }
    }

    fun stopRecording() {
        try {
            recorder?.apply {
                stop()
                release()
            }
            recorder = null
            setupPlayer()
            recordState = recordState.copy(
                isRecording = false,
                isRecordExist = true,
                existingRecordDuration = player!!.duration
            )
        } catch (e: Exception) {
            Timber.tag(TAG).e("Error endRecording $e")
            recorder = null
            deleteRecording()
        }
    }

    fun startRecording() {
        val file = File(audioPath)
        if (!file.exists()) {
            file.createNewFile()
        }
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(file.absolutePath)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            try {
                prepare()
                start()
                recordState = recordState.copy(isRecording = true, existingRecordDuration = 0)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    VibrationEffect.createOneShot(100L, VibrationEffect.DEFAULT_AMPLITUDE)
                } else {
                    (application.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator).vibrate(
                        100
                    )
                }
            } catch (e: IOException) {
                Timber.tag(TAG).e("prepare() failed: $e")
            }
        }
    }

    // mark as delete on delete and remove on save
    fun deleteTempRecord() {}
    fun deleteSavedRecord() {}

    fun deleteRecording() {
        try {
            clearRecording()
            deleteAudioFile()
        } catch (e: Exception) {
            Timber.tag(TAG).e("Deletion failed. $e")
        }
    }

    private fun clearRecording() {
        recordState =
            recordState.copy(isRecording = false, isRecordExist = false, existingRecordDuration = 0)
        recorder?.apply {
            stop()
        }
        recorder = null
        player?.stop()
        player = null
    }

    private fun deleteAudioFile() {
        val file = File(audioPath)
        file.delete()
    }

}