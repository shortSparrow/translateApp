package com.example.ttanslateapp.presentation.core

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.example.ttanslateapp.R
import com.example.ttanslateapp.databinding.ViewRecordAudioBinding
import com.example.ttanslateapp.util.lazySimple
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class RecordAudioBottomSheet(private val modifiedPath: String?, private val word: String?) :
    BottomSheetDialogFragment() {
    private var _binding: ViewRecordAudioBinding? = null
    private val binding get() = _binding!!

    var callbackListener: CallbackListener? = null

    private val fileName by lazySimple {
        val formatter = SimpleDateFormat("yyyy_MM_dd_hh_mm_ss")
        val date = Date()
        "Recording_" + formatter.format(date) + ".3gp"
    }
    private val dirName = "/storage/emulated/0/Android/data/com.example.ttanslateapp/files" // FIXME add correct path
    private val path = modifiedPath ?: "$dirName/$fileName"

    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null

    private var isPlaying = false
    private var isFilePathSaved = modifiedPath != null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ViewRecordAudioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupClickListener()
    }

    private fun setupView() = with(binding) {
        wordValue.text = word
        if (modifiedPath != null) {
            listenRecord.isEnabled = true
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupClickListener() = with(binding) {
        deleteRecord.setOnClickListener {
            deleteRecording()
        }
        listenRecord.setOnClickListener {
            startPlaying()
        }
        saveRecord.setOnClickListener {
            saveRecording()
        }

        handleButtonContainer.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                Timber.d("pressIn")
                isPlaying = true
                handleButton.setImageResource(R.drawable.mic_active)
                startRecording()
                saveRecord.isEnabled = false
                binding.listenRecord.isEnabled = false
            } else if (event.action == MotionEvent.ACTION_UP) {
                Timber.d("pressOut")
                isPlaying = false
                binding.handleButton.setImageResource(R.drawable.mic_disable)
                binding.deleteRecord.setImageResource(R.drawable.delete_active)
                binding.listenRecord.isEnabled = true
                endRecording()
            }
            true
        }
    }


    private fun startRecording() {
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(path)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            try {
                prepare()
                binding.recordingChronometer.base = SystemClock.elapsedRealtime()
                binding.recordingChronometer.start()
            } catch (e: IOException) {
                Timber.e("prepare() failed: $e")
            }

            start()
        }
    }

    private fun endRecording() {
        try {
            recorder?.apply {
                stop()
                release()
            }
            recorder = null
            binding.recordingChronometer.stop()
            binding.saveRecord.isEnabled = true
        } catch (e: Exception) {
            Timber.e("Error endRecording $e")
            recorder = null
            deleteRecording()
        }
    }

    private fun saveRecording() {
        callbackListener?.saveAudio(path)
        isFilePathSaved = true
        this.dismiss()
    }

    private fun clearRecording() {
        recorder?.apply {
            stop()
        }
        recorder = null

        player?.stop()
        player = null
    }

    private fun resetChronometer() {
        binding.recordingChronometer.base = SystemClock.elapsedRealtime()
        binding.recordingChronometer.stop()
    }

    private fun deleteAudioFile() {
        val file = File(path)
        file.delete()
        with(binding) {
            deleteRecord.setImageResource(R.drawable.delete_disabled)
            saveRecord.isEnabled = false
            listenRecord.isEnabled = false
            handleButton.setImageResource(R.drawable.mic_disable)
        }
    }

    private fun deleteRecording() {
        try {
            clearRecording()
            resetChronometer()
            deleteAudioFile()

            isFilePathSaved = false
            Timber.d("Deletion succeeded.")

        } catch (e: Exception) {
            Timber.d("Deletion failed. $e")
        }
    }

    private fun startPlaying() {
        player = MediaPlayer().apply {
            try {
                setDataSource(path)
                prepare()
                start()
            } catch (e: IOException) {
                Timber.e("prepare() failed $e")
            }
        }
    }

    // close bottom sheet
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        Timber.d("isFilePathSaved $isFilePathSaved $modifiedPath")
        if (!isFilePathSaved) {
            deleteRecording()
            callbackListener?.saveAudio(null)
        }
    }

    interface CallbackListener {
        fun saveAudio(path: String?)
    }

    companion object {
        const val TAG = "RecordAudioBottomSheet"
    }
}