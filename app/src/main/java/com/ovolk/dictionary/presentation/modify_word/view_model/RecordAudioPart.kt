package com.ovolk.dictionary.presentation.modify_word.view_model

import android.app.Application
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import com.ovolk.dictionary.domain.model.modify_word.WordAudio
import com.ovolk.dictionary.domain.use_case.modify_word.ModifyWordUseCase
import com.ovolk.dictionary.presentation.modify_word.ComposeState
import com.ovolk.dictionary.presentation.modify_word.RecordAudioAction
import com.ovolk.dictionary.presentation.modify_word.RecordAudioState
import com.ovolk.dictionary.util.helpers.generateFileName
import com.ovolk.dictionary.util.helpers.getAudioPath
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.io.IOException
import javax.inject.Inject


abstract class ViewModelRecordAudioSlice {
    lateinit var viewModelScope: CoroutineScope
    lateinit var globalState: MutableStateFlow<ComposeState>

    operator fun invoke(
        viewModelScope: CoroutineScope,
        globalState: MutableStateFlow<ComposeState>
    ) {
        this.viewModelScope = viewModelScope
        this.globalState = globalState
        afterInit()
    }

    open fun afterInit() {}
}


class RecordAudioPart @Inject constructor(
    private val modifyWordUseCase: ModifyWordUseCase,
    val application: Application
) : ViewModelRecordAudioSlice() {
    val recordState = MutableStateFlow(RecordAudioState())

    private val audioManager = application.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private val oldVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

    private var modifiedFileName: String? = null
    private var tempFileName: String? = null
    private var modifiedFileNameMarkDelete: Boolean = false

    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null

    fun onAction(action: RecordAudioAction) {
        when (action) {
            RecordAudioAction.DeleteRecord -> deleteTempRecordingAndMarkToDeleteFile()
            RecordAudioAction.ListenRecord -> startPlaying()
            RecordAudioAction.SaveRecord -> prepareToSave()
            RecordAudioAction.StartRecording -> startRecording()
            RecordAudioAction.StopRecording -> stopRecording()
            RecordAudioAction.HideBottomSheet -> closeBottomSheet()
            RecordAudioAction.OpenBottomSheet -> openBottomSheet()
        }
    }

    private fun updateAudio(fileName: String?) {
        viewModelScope.launch {
            globalState.value.editableWordId?.let {
                val sound = if (fileName == null) {
                    null
                } else WordAudio(fileName = fileName)
                modifyWordUseCase.modifyOnlySound(it, sound = sound)
            }
            globalState.update {
                it.copy(soundFileName = fileName)
            }
        }
    }

    private fun startPlaying() {
        audioManager.setStreamVolume(
            AudioManager.STREAM_MUSIC,
            audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
            0
        )

        recordState.update { it.copy(isRecordPlaying = true) }
        player?.start()
        player?.setOnCompletionListener {
            audioManager.setStreamVolume(
                AudioManager.STREAM_MUSIC,
                oldVolume,
                0
            )
            it.seekTo(0)
            recordState.update { it.copy(isRecordPlaying = false) }
        }
    }

    private fun stopRecording() {
        try {
            recorder?.apply {
                stop()
                release()
            }
            recorder = null
            setupPlayer()
            recordState.update {
                it.copy(
                    isRecording = false,
                    isTempRecordExist = true,
                    existingRecordDuration = player!!.duration,
                )
            }
        } catch (e: Exception) {
            Timber.e("Error endRecording $e")
            recorder = null
            deleteTempRecordingAndMarkToDeleteFile()
        }
    }

    private fun startRecording() {
        if (recordState.value.isRecordPlaying) return
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
                recordState.update { it.copy(isRecording = true, existingRecordDuration = 0) }
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

    private fun prepareToSave() {
        val saveableFileName = getSaveableFile()
        modifiedFileName = saveableFileName
        updateAudio(saveableFileName)
        resetStateToInitial()
    }

    fun prepareToOpen(passedModifiedFileNamed: String?) {
        modifiedFileName = passedModifiedFileNamed
        tempFileName = if (passedModifiedFileNamed == null) generateFileName() else null
        resetStateToInitial()
    }

    private fun openBottomSheet() {
        recordState.update { it.copy(isModalOpen = true) }
    }

    private fun closeBottomSheet() {
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
            recordState.value = RecordAudioState(
                isTempRecordExist = isRecordExist,
                isRecordExist = isRecordExist,
                existingRecordDuration = existingRecordDuration,
                isModalOpen = false,
                isChangesExist = false
            )

        } ?: run {
            recordState.value = RecordAudioState(
                isTempRecordExist = false,
                isRecordExist = false,
                existingRecordDuration = 0,
                isModalOpen = false,
                isChangesExist = false
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
        recordState.update {
            it.copy(
                isRecording = false,
                isTempRecordExist = false,
                existingRecordDuration = 0,
                isChangesExist = true
            )
        }

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
}