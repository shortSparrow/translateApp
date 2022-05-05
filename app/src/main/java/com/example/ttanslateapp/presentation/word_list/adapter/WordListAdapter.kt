package com.example.ttanslateapp.presentation.word_list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.ttanslateapp.databinding.WordRvItemBinding
import com.example.ttanslateapp.domain.model.WordRV
import com.example.ttanslateapp.presentation.modify_word.adapter.translate.TranslateAdapter
import timber.log.Timber
import kotlin.collections.set

class WordListAdapter : ListAdapter<WordRV, WordItemViewHolder>(WordListAdapterDiffCallback()) {
    var onClickListener: OnClickListener? = null
    private val isOpenedSet = mutableMapOf<Long, Boolean>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordItemViewHolder {
        return WordRvItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
            .run { WordItemViewHolder(this) }
    }

    override fun onBindViewHolder(holder: WordItemViewHolder, position: Int) {
        val word = getItem(position)
        if (!isOpenedSet.containsKey(word.id)) {
            isOpenedSet[word.id] = false
        }

        // FIXME: add show more button
        with(holder.binding) {
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


            playSound.setOnClickListener {
                Timber.d("playSoundClickListener")
            }
            root.setOnClickListener {
                with(Timber) { d("onRootClickListener") }
                onClickListener?.onRootClickListener(word.id)
            }
        }
    }

    interface OnClickListener {
        fun onRootClickListener(wordId: Long)
    }
}