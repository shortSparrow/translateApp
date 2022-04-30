package com.example.ttanslateapp.presentation.modify_word.adapter.hints

import androidx.recyclerview.widget.DiffUtil
import com.example.ttanslateapp.domain.model.Chip
import com.example.ttanslateapp.domain.model.edit.HintItem
import com.example.ttanslateapp.domain.model.edit.TranslateWordItem

class HintAdapterDiffCallback : DiffUtil.ItemCallback<HintItem>() {
    override fun areItemsTheSame(oldItem: HintItem, newItem: HintItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: HintItem, newItem: HintItem): Boolean {
        return oldItem == newItem
    }
}