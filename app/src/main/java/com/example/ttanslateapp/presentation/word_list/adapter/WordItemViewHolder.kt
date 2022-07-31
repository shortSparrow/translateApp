package com.example.ttanslateapp.presentation.word_list.adapter

import android.app.Application
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.example.ttanslateapp.R
import com.example.ttanslateapp.databinding.ItemWordRvBinding
import com.example.ttanslateapp.domain.model.WordRV
import com.example.ttanslateapp.presentation.modify_word.adapter.translate.TranslateAdapter
import com.example.ttanslateapp.util.getAudioPath
import timber.log.Timber
import java.io.IOException


class WordItemViewHolder(
    val binding: ItemWordRvBinding,
    private val player: MediaPlayer,
    private val playingList: MutableMap<Long, Boolean>,
    private val expandedList: HashMap<Long, Boolean>,
    private val application: Application,
) : RecyclerView.ViewHolder(binding.root) {
    private val audioManager = application.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private val oldVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

    private fun setBaseExpanded(word: WordRV) {
        if (word.description.isEmpty()) {
            expandedList[word.id] = false
        }

        if (expandedList[word.id] == null) {
            expandedList[word.id] = false
        }

    }

    private fun setExpandedStyle(isExpanded: Boolean) = with(binding) {
        if (isExpanded) {
            showMoreContent.visibility = View.VISIBLE
            showMore.text = binding.root.context.getString(R.string.show_less)
        } else {
            showMoreContent.visibility = View.GONE
            showMore.text = binding.root.context.getString(R.string.show_more)
        }
    }

    fun bind(word: WordRV, onClickListener: WordListAdapter.OnClickListener?) = with(binding) {
        setBaseExpanded(word = word)
        val isExpanded = expandedList[word.id]!!
        setExpandedStyle(isExpanded)

        langFrom.text = word.langFrom
        englishWord.text = word.value
        langTo.text = word.langTo
        transcription.text = word.transcription


        if (word.description.isNotEmpty()) {
            descriptionValue.text = word.description
            showMore.visibility = View.VISIBLE
        } else {
            descriptionValue.text = ""
            showMore.visibility = View.GONE
        }

        showMore.setOnClickListener {
            val newIsExpanded = !expandedList[word.id]!!
            expandedList[word.id] = newIsExpanded
            setExpandedStyle(newIsExpanded)
        }


        val translateAdapter = TranslateAdapter()
        translateList.adapter = translateAdapter
        translateAdapter.submitList(word.translates)

        binding.frame.setOnClickListener {
            onClickListener?.onRootClickListener(word.id)
        }

        setVolumeImage(playSound, word)

        playSound.setOnClickListener {
            if (!player.isPlaying && word.sound?.fileName != null) {
                playingList[word.id] = playingList[word.id]?.let { !it } ?: true
                playAudio(playSound, word)
            }
        }
    }

    private fun playAudio(playSound: ImageButton, word: WordRV) {
        audioManager.setStreamVolume(
            AudioManager.STREAM_MUSIC,
            audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
            0
        );
        setVolumeImage(playSound, word)

        player.apply {
            try {
                reset()
                setDataSource(getAudioPath(playSound.context, word.sound!!.fileName))
                prepare()
                start()
            } catch (e: IOException) {
                Timber.e("prepare() failed $e")
            }
        }
        player.setOnCompletionListener {
            playingList[word.id] = false
            setVolumeImage(playSound, word)
            audioManager.setStreamVolume(
                AudioManager.STREAM_MUSIC,
                oldVolume,
                0
            );
        }
    }

    private fun setVolumeImage(playSound: ImageButton, word: WordRV) {
        if (playingList[word.id] == true) {
            playSound.background =
                AppCompatResources.getDrawable(playSound.context, R.drawable.volume_up_active)
        } else {
            if (word.sound?.fileName != null) {
                playSound.background =
                    AppCompatResources.getDrawable(
                        playSound.context,
                        R.drawable.volume_up_available
                    )
            } else {
                playSound.background = null
            }
        }

    }
}