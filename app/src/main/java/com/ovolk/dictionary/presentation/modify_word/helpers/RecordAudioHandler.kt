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
import javax.inject.Inject


class RecordAudioHandler @Inject constructor(
    private val application: Application,
) {
    var listener: RecordAudioListener? = null
    var recordState by mutableStateOf(RecordAudioState())
        private set

    private val audioManager = application.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private val oldVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

    private var modifiedFileName: String? = null
    private var tempFileName: String? = null
    private var modifiedFileNameMarkDelete: Boolean = false

    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null

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
                isTempRecordExist = true,
                existingRecordDuration = player!!.duration
            )
        } catch (e: Exception) {
            Timber.e("Error endRecording $e")
            recorder = null
            deleteTempRecordingAndMarkToDeleteFile()
        }
    }

    fun startRecording() {
        deleteTempRecordingAndMarkToDeleteFile()
        tempFileName = generateFileName()
        val file = File(getAudioPath(application, tempFileName!!))
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
                Timber.e("prepare() failed: $e")
            }
        }
    }

    fun prepareToSave() {
        val saveableFileName = getSaveableFile()
        modifiedFileName = saveableFileName
        listener?.saveAudio(saveableFileName)
        resetStateToInitial()
    }

    fun prepareToOpen(passedModifiedFileNamed: String?) {
        modifiedFileName = passedModifiedFileNamed
        tempFileName = if (passedModifiedFileNamed == null) generateFileName() else null
        resetStateToInitial()
    }

    fun openBottomSheet() {
        // TODO make request
        recordState = recordState.copy(isModalOpen = true)
    }

    fun onPressDelete() = deleteTempRecordingAndMarkToDeleteFile()

    fun closeBottomSheet() {
        deleteLastTempRecord()
        resetStateToInitial()
    }

    private fun resetStateToInitial() {
        tempFileName = null // clear tempFileName to avoid deleting one after closing bottomSheet
        modifiedFileNameMarkDelete = false
        modifiedFileName?.let { fileName ->
            val file = File(getAudioPath(application, fileName))
            val isRecordExist = file.exists()
            if (isRecordExist) {
                setupPlayer()
            }
            val existingRecordDuration = if (isRecordExist) player!!.duration else 0
            recordState =
                RecordAudioState(
                    isTempRecordExist = isRecordExist,
                    isRecordExist = isRecordExist,
                    existingRecordDuration = existingRecordDuration,
                    isModalOpen = false
                )
        } ?: run {
            recordState =
                RecordAudioState(
                    isTempRecordExist = false,
                    isRecordExist = false,
                    existingRecordDuration = 0,
                    isModalOpen = false
                )
        }
    }

    // call when start record new audio and must delete old temp or mark to delete previous saved audio
    private fun deleteTempRecordingAndMarkToDeleteFile() {
        try {
            clearRecording()
            if (tempFileName == null) {
                modifiedFileNameMarkDelete = true
            } else {
                deleteAudioFile(getAudioPath(application, tempFileName!!))
            }
            tempFileName = null
        } catch (e: Exception) {
            Timber.e("Deletion failed. $e")
        }
    }


    // call on close bottomSheet to avoid useless audio file in phone storage
    private fun deleteLastTempRecord() {
        tempFileName?.let { deleteAudioFile(getAudioPath(application, it)) }
    }

    private fun getSaveableFile(): String? {
        if (tempFileName != null) {
            modifiedFileName?.let { deleteAudioFile(getAudioPath(application, it)) }
            return tempFileName
        }

        if (!modifiedFileNameMarkDelete) {
            return modifiedFileName
        }

        // there are not saveable file and existing file mark to delete. So delete one and return null for updating db
        deleteAudioFile(getAudioPath(application, modifiedFileName!!))
        return null
    }

    private fun setupPlayer() {
        player = MediaPlayer().apply {
            try {
                reset()
                if (tempFileName != null) {
                    setDataSource(getAudioPath(application, tempFileName!!))
                } else {
                    setDataSource(getAudioPath(application, modifiedFileName!!))
                }

                prepare()
            } catch (e: IOException) {
                Timber.e("prepare() failed $e")
            }
        }
    }

    private fun clearRecording() {
        recordState =
            recordState.copy(
                isRecording = false,
                isTempRecordExist = false,
                existingRecordDuration = 0
            )
        recorder?.apply {
            stop()
        }
        recorder = null
        player?.stop()
        player = null
    }

    private fun deleteAudioFile(audioPath: String) {
        val file = File(audioPath)
        file.delete()
    }

    interface RecordAudioListener {
        fun soundMarkAsExistButIsNoTrue()
        fun saveAudio(savableFile: String?)
    }

}