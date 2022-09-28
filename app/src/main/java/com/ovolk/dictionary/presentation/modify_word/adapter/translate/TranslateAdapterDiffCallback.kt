package com.ovolk.dictionary.presentation.modify_word.adapter.translate

import androidx.recyclerview.widget.DiffUtil
import com.ovolk.dictionary.domain.model.modify_word.modify_word_chip.Translate

@Deprecated("Probably redundant after Differ was added")
class TranslateAdapterDiffCallback : DiffUtil.ItemCallback<Translate>() {
    override fun areItemsTheSame(oldItem: Translate, newItem: Translate): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Translate,
        newItem: Translate
    ): Boolean {
        return oldItem == newItem
    }
}