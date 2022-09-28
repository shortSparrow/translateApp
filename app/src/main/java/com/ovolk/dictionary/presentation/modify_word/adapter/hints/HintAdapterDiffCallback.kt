package com.ovolk.dictionary.presentation.modify_word.adapter.hints

import androidx.recyclerview.widget.DiffUtil
import com.ovolk.dictionary.domain.model.modify_word.modify_word_chip.HintItem

@Deprecated("Probably redundant after Differ was added")
class HintAdapterDiffCallback : DiffUtil.ItemCallback<HintItem>() {
    override fun areItemsTheSame(oldItem: HintItem, newItem: HintItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: HintItem, newItem: HintItem): Boolean {
        return oldItem == newItem
    }
}