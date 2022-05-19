package com.example.ttanslateapp.presentation.word_list.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.ttanslateapp.databinding.WordRvItemBinding
import com.example.ttanslateapp.domain.model.WordRV
import com.example.ttanslateapp.presentation.modify_word.adapter.translate.TranslateAdapter
import timber.log.Timber

class WordItemViewHolder(val binding: WordRvItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(word: WordRV, onClickListener: WordListAdapter.OnClickListener?) = with(binding) {
        langFrom.text = word.langFrom
        englishWord.text = word.value
        langTo.text = word.langTo
        transcription.text = word.transcription
        if (word.description.isNotEmpty()) {
            descriptionValue.text = word.description
        }

        val translateAdapter = TranslateAdapter()
        translateList.adapter = translateAdapter
        translateAdapter.submitList(word.translates)

        // FIXME click not work in chip
        // make translateAdapter clickable false, because it interrupt parent click, and I can't go to modify screen
        translateList.suppressLayout(true)

        playSound.setOnClickListener {
            // TODO: ADD PLAY SOUND
            Timber.d("playSoundClickListener")
        }
        root.setOnClickListener {
            with(Timber) { d("onRootClickListener") }
            onClickListener?.onRootClickListener(word.id)
        }
    }
}