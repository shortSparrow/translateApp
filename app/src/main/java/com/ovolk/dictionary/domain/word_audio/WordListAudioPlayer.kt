package com.ovolk.dictionary.domain.word_audio

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import com.ovolk.dictionary.domain.model.modify_word.WordRV
import com.ovolk.dictionary.presentation.DictionaryApp
import com.ovolk.dictionary.util.helpers.getAudioPath
import timber.log.Timber
import java.io.IOException

// TODO figure out why composable preview can't be render if put his into @Composable function
class WordListAudioPlayer {
    val application = DictionaryApp.applicationContext()

    private val player = MediaPlayer()
    private val audioManager = application.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private val oldVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

    fun playAudio(
        onStartListener: () -> Unit,
        word: WordRV,
        onCompletionListener: () -> Unit,
    ) {
        if (player.isPlaying) return
        onStartListener()

        audioManager.setStreamVolume(
            AudioManager.STREAM_MUSIC,
            audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
            0
        )

        player.apply {
            try {
                reset()
                setDataSource(getAudioPath(application, word.sound!!.fileName))
                prepare()
                start()
            } catch (e: IOException) {
                Timber.e("prepare() failed $e")
            }
        }
        player.setOnCompletionListener {
            onCompletionListener()
            audioManager.setStreamVolume(
                AudioManager.STREAM_MUSIC,
                oldVolume,
                0
            )
        }
    }
}