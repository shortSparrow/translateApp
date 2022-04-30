package com.example.ttanslateapp.presentation.modify_word.adapter.hints

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.ttanslateapp.databinding.HintChipBinding
import com.example.ttanslateapp.databinding.TranslateChipBinding
import com.example.ttanslateapp.domain.model.edit.HintItem


class HintAdapter :
    ListAdapter<HintItem, HintItemViewHolder>(HintAdapterDiffCallback()) {

    var onChipClickListener: OnChipClickListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HintItemViewHolder {
        val itemBinding =
            HintChipBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HintItemViewHolder(itemBinding)
    }

    override fun onBindViewHolder(
        holder: HintItemViewHolder,
        position: Int
    ) {
        val hint = getItem(position)

        with(holder.binding) {
            hintItem.text = hint.value

            hintItem.setOnClickListener {
                onChipClickListener?.onChipClick(it, hint)
            }

            deleteHint.setOnClickListener {
                onChipClickListener?.onDeleteClick(hint)
            }
        }
    }

    interface OnChipClickListener {
        fun onChipClick(it: View, hintItem: HintItem)
        fun onDeleteClick(hintItem: HintItem)
    }


}