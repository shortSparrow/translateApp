package com.ovolk.dictionary.presentation.exam.adapter

import androidx.recyclerview.widget.DiffUtil
import com.ovolk.dictionary.domain.model.exam.ExamWord

class ExamWordAdapterDiffCallback : DiffUtil.ItemCallback<ExamWord>() {
    override fun areItemsTheSame(oldItem: ExamWord, newItem: ExamWord): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ExamWord, newItem: ExamWord): Boolean {
        return oldItem == newItem
    }
}