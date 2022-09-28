package com.ovolk.dictionary.presentation.word_list.adapter

import androidx.recyclerview.widget.DiffUtil
import com.ovolk.dictionary.domain.model.modify_word.WordRV

class WordListAdapterDiffCallback : DiffUtil.ItemCallback<WordRV>() {
    override fun areItemsTheSame(oldItem: WordRV, newItem: WordRV): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: WordRV, newItem: WordRV): Boolean {
        return oldItem == newItem
    }
}