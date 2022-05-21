package com.example.ttanslateapp.presentation.core

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.ttanslateapp.R
import com.example.ttanslateapp.databinding.ViewRecordAudioBinding
import com.example.ttanslateapp.util.generateFileName
import com.example.ttanslateapp.util.getAudioPath
import com.example.ttanslateapp.util.lazySimple
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class RecordAudioBottomSheet : BottomSheetDialogFragment() {
    private var _binding: ViewRecordAudioBinding? = null
    private val binding get() = _binding!!

    var callbackListener: CallbackListener? = null

    val viewModel by lazy { ViewModelProvider(this)[RecordAudioViewModel::class.java] }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val modifiedFileName = arguments?.getString(MODIFIED_FILE_NAME)
        val word = arguments?.getString(WORD)
//        Timber.d("modifiedFileName ${modifiedFileName}")
        viewModel.setArguments(
            modifiedFileName = modifiedFileName,
            word = word
        )
    }

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
        setupListeners()
    }

    private fun setupListeners() = with(binding) {
        viewModel.isRecordExist.observe(viewLifecycleOwner) {
            Timber.d("duration ${viewModel._player?.duration}")
            Timber.d("isRecordExist ${it}")
            if (it) {
                recordingChronometer.isCountDown = true
                resetChronometer()
                prepareAppearsAudio()
            } else {
                deleteAudioFile()
                binding.recordingChronometer.base = SystemClock.elapsedRealtime()
            }
        }

        viewModel.isRecording.observe(viewLifecycleOwner) {
            Timber.d("isRecording: ${it}")
            if (it) {
                recordingChronometer.isCountDown = false
                recordingChronometer.base = SystemClock.elapsedRealtime()
                recordingChronometer.start()
                saveRecord.isEnabled = false
            } else {
                recordingChronometer.isCountDown = true
                saveRecord.isEnabled = true
                recordingChronometer.stop()
            }
        }

        viewModel.isPlaying.observe(viewLifecycleOwner) {
            if (it) {
                resetChronometer()
                recordingChronometer.start()
            } else {
                recordingChronometer.stop()
                resetChronometer()
            }
        }
    }


    private fun resetChronometer() {
        binding.recordingChronometer.base =
            SystemClock.elapsedRealtime() + (viewModel._player?.duration?.toLong() ?: 0)
    }


    private fun setupView() = with(binding) {
        wordValue.text = arguments?.getString(WORD)
//        listenRecord.isEnabled = arguments?.getString(MODIFIED_FILE_NAME) != null
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupClickListener() = with(binding) {
        deleteRecord.setOnClickListener {
            viewModel.deleteRecording()
            callbackListener?.saveAudio(null)
        }
        listenRecord.setOnClickListener {
            viewModel.startPlaying()
        }
        saveRecord.setOnClickListener {
            saveRecording()
        }

        handleButtonContainer.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
//                Timber.d("pressIn")
                startRecording()

            } else if (event.action == MotionEvent.ACTION_UP) {
//                Timber.d("pressOut")
                endRecording()
            }
            true
        }
    }


    private fun startRecording() = with(binding) {
        viewModel.startRecording()
        saveRecord.isEnabled = false
        binding.listenRecord.isEnabled = false
        handleButton.setImageResource(R.drawable.mic_active)

    }

    private fun endRecording() = with(binding) {
        viewModel.endRecording()
        binding.handleButton.setImageResource(R.drawable.mic_disable)
        binding.deleteRecord.setImageResource(R.drawable.delete_active)
        binding.listenRecord.isEnabled = true
    }

    private fun saveRecording() {
        Timber.d("viewModel.fileName ${viewModel.fileName}")
        viewModel.fileRecordedBuNotSaved = false
        callbackListener?.saveAudio(viewModel.fileName)
        this.dismiss()
    }

    private fun prepareAppearsAudio() = with(binding) {
        saveRecord.isEnabled = true
        listenRecord.isEnabled = true
        binding.deleteRecord.setImageResource(R.drawable.delete_active)
    }

    private fun deleteAudioFile() = with(binding) {
        deleteRecord.setImageResource(R.drawable.delete_disabled)
        saveRecord.isEnabled = false
        listenRecord.isEnabled = false
        handleButton.setImageResource(R.drawable.mic_disable)
    }


    // close bottom sheet
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        viewModel.clearRecording()
        if (viewModel.fileRecordedBuNotSaved) {
            viewModel.deleteRecording()
            if ( arguments?.getString(MODIFIED_FILE_NAME) != null) {
                callbackListener?.saveAudio(null)
            }
        }
    }

    interface CallbackListener {
        fun saveAudio(path: String?)
    }

    companion object {
        const val TAG = "RecordAudioBottomSheet"
        const val MODIFIED_FILE_NAME = "modifiedFileName"
        const val WORD = "word"
    }
}