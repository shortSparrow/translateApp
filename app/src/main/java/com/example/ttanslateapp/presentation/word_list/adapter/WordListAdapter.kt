package com.example.ttanslateapp.presentation.word_list.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.ListAdapter
import com.example.ttanslateapp.R
import com.example.ttanslateapp.databinding.WordRvItemBinding
import com.example.ttanslateapp.domain.model.WordRV
import com.example.ttanslateapp.presentation.modify_word.adapter.translate.TranslateAdapter
import kotlin.collections.set

class WordListAdapter : ListAdapter<WordRV, WordItemViewHolder>(WordListAdapterDiffCallback()) {

    var onClickListener: OnClickListener? = null
    private val isOpenedSet = mutableMapOf<Long, Boolean>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordItemViewHolder {
        return WordRvItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
            .run { WordItemViewHolder(this) }
    }

    private fun open(binding: WordRvItemBinding) {
        with(binding) {
            root.updateLayoutParams {
                height = ViewGroup.LayoutParams.WRAP_CONTENT
            }
            root.findViewById<TextView>(R.id.show_more).text = "Приховати"
            binding.showMoreContent.setPadding(0, 0, 0, 50)
        }
    }

    private fun close(binding: WordRvItemBinding) {
        with(binding) {
            val params: ViewGroup.LayoutParams = root.layoutParams
            params.height = 400
            root.layoutParams = params
            root.findViewById<TextView>(R.id.show_more).text = "Розкрити"
            binding.showMoreContent.setPadding(0, 0, 0, 0)
        }
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
            if (word.description.isNotEmpty()) {
                descriptionValue.text = word.description
                showMoreContent.visibility = View.VISIBLE
            } else {
                showMoreContent.visibility = View.GONE
            }

            showMore.setOnClickListener {
                isOpenedSet[word.id] = !isOpenedSet[word.id]!!

                if (isOpenedSet[word.id] == true) {
                    open(holder.binding)
                } else {
                    close(holder.binding)
                }
            }

            val translateAdapter = TranslateAdapter()
            translateList.adapter = translateAdapter
            translateAdapter.submitList(word.translates)


            playSound.setOnClickListener {
                Log.d("DDD", "D")
            }
            root.setOnClickListener {
                Log.d("DDD_2", "D")
                onClickListener?.onRootClickListener(word.id)
            }
        }
    }

    interface OnClickListener {
        fun onRootClickListener(wordId: Long)
    }
}