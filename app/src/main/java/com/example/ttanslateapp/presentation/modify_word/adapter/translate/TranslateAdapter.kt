package com.example.ttanslateapp.presentation.modify_word.adapter.translate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.ttanslateapp.databinding.TranslateChipBinding
import com.example.ttanslateapp.domain.model.edit.TranslateWordItem


class TranslateAdapter :
    ListAdapter<TranslateWordItem, TranslateItemViewHolder>(TranslateAdapterDiffCallback()) {

    var onChipClickListener: OnChipClickListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TranslateItemViewHolder {
        val itemBinding =
            TranslateChipBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TranslateItemViewHolder(itemBinding)
    }

    override fun onBindViewHolder(
        holder: TranslateItemViewHolder,
        position: Int
    ) {
        val chip = getItem(position)
        with(holder.binding) {
            chipItem.text = chip.value

            root.setOnClickListener {
                onChipClickListener?.onChipClick(it, chip)
            }
        }
    }

    interface OnChipClickListener {
        fun onChipClick(it: View, translateWordItem: TranslateWordItem)
    }


}