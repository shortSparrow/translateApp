package com.example.ttanslateapp.presentation.modify_word.adapter.translate

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.ttanslateapp.databinding.TranslateChipBinding
import com.example.ttanslateapp.domain.model.edit.TranslateWordItem
import com.example.ttanslateapp.presentation.modify_word.adapter.ModifyWordAdapter

private typealias ClickListener = ModifyWordAdapter.OnItemClickListener<TranslateWordItem>

class TranslateAdapter : ModifyWordAdapter<TranslateWordItem, TranslateItemViewHolder, ClickListener>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TranslateItemViewHolder {
        return TranslateChipBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
            .run { TranslateItemViewHolder(this) }
    }

    override fun onBindViewHolder(
        holder: TranslateItemViewHolder,
        position: Int
    ) {
        val chip: TranslateWordItem = getItem(position)
        with(holder.binding) {
            chipItem.text = chip.value
            root.setOnClickListener {
                clickListener?.onItemClick(it, chip)
            }
        }
    }
}