package com.ovolk.dictionary.presentation.core

import android.app.Application
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ovolk.dictionary.util.generateFileName
import com.ovolk.dictionary.util.getAudioPath
import timber.log.Timber
import java.io.File
import java.io.IOException

class RecordAudioViewModel(private val applicationContext: Application) :
    AndroidViewModel(applicationContext) {
    private var modifiedFileName: String? = null
    private var word: String? = null
    private val TAG = "RecordAudioViewModel"

    private val audioManager = applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private val oldVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

    val fileName by lazy { modifiedFileName ?: generateFileName() }
    private val audioPath by lazy { getAudioPath(applicationContext, fileName) }

    private var recorder: MediaRecorder? = null

    var player: MediaPlayer? = null

    private val _isRecordExist = MutableLiveData<Boolean>()
    val isRecordExist: LiveData<Boolean> = _isRecordExist

    private val _isRecording = MutableLiveData<Boolean>()
    val isRecording: LiveData<Boolean> = _isRecording

    private val _isPlaying = MutableLiveData<Boolean>()
    val isPlaying: LiveData<Boolean> = _isPlaying

    var fileRecordedBuNotSaved = false


    fun setArguments(modifiedFileName: String?, word: String?) {
        this.modifiedFileName = modifiedFileName
        this.word = word
        if (modifiedFileName != null) {
            setupPlayer()
            _isRecordExist.value = true
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
                _isRecording.value = true
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    VibrationEffect.createOneShot(100L, VibrationEffect.DEFAULT_AMPLITUDE)
                } else {
                    (applicationContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator).vibrate(
                        100
                    )
                }
            } catch (e: IOException) {
                Timber.tag(TAG).e("prepare() failed: $e")
            }
        }
    }

    fun endRecording() {
        try {
            recorder?.apply {
                stop()
                release()
            }
            recorder = null
            setupPlayer()
            _isRecording.value = false
            _isRecordExist.value = true
            fileRecordedBuNotSaved = true
        } catch (e: Exception) {
            Timber.tag(TAG).e("Error endRecording $e")
            recorder = null
            deleteRecording()
        }
    }

    fun deleteRecording() {
        try {
            clearRecording()
            _isRecordExist.value = false
            deleteAudioFile()
            fileRecordedBuNotSaved = false
        } catch (e: Exception) {
            Timber.tag(TAG).e("Deletion failed. $e")
        }
    }

    fun clearRecording() {
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

    fun startPlaying() {
        audioManager.setStreamVolume(
            AudioManager.STREAM_MUSIC,
            audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
            0
        );

        _isPlaying.value = true
        player?.start()
        player?.setOnCompletionListener {
            audioManager.setStreamVolume(
                AudioManager.STREAM_MUSIC,
                oldVolume,
                0
            );
            it.seekTo(0)
            _isPlaying.value = false
        }
    }

    var callbackListener: CallbackListener? = null

    override fun onCleared() {
        super.onCleared()
        clearRecording()
        if (fileRecordedBuNotSaved) {
            deleteRecording()
            if (modifiedFileName != null) {
                callbackListener?.deleteRecordingButNotSaveAudio()
            }
        }
    }

    interface CallbackListener {
        fun deleteRecordingButNotSaveAudio()
    }
}