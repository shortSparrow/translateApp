package com.ovolk.dictionary.presentation.modify_word.adapter.hints

import android.view.LayoutInflater
import android.view.ViewGroup
import com.ovolk.dictionary.databinding.ItemHintChipBinding
import com.ovolk.dictionary.domain.model.modify_word.modify_word_chip.HintItem
import com.ovolk.dictionary.presentation.modify_word.adapter.ModifyWordAdapter

private typealias ClickListener = ModifyWordAdapter.OnItemMultiClickListener<HintItem>

class HintAdapter : ModifyWordAdapter<HintItem, HintItemViewHolder, ClickListener>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HintItemViewHolder {
        return ItemHintChipBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
            .run { HintItemViewHolder(this) }
    }

    override fun onBindViewHolder(
        holder: HintItemViewHolder,
        position: Int
    ) {
        val hint = getItem(position)

        with(holder.binding) {
            hintItem.text = hint.value
            hintItem.setOnClickListener { clickListener?.onItemClick(it, hint) }
            deleteHint.setOnClickListener { clickListener?.onItemDeleteClick(hint) }
        }
    }
}