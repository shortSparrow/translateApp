package com.ovolk.dictionary.presentation.exam.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import com.ovolk.dictionary.R
import com.ovolk.dictionary.databinding.ItemRvExamAdapterBinding
import com.ovolk.dictionary.domain.model.exam.ExamWord
import com.ovolk.dictionary.domain.model.exam.ExamWordStatus
import com.ovolk.dictionary.presentation.exam.ExamMode


class ExamAdapter :
    ListAdapter<ExamWord, ExamAdapterViewHolder>(ExamWordAdapterDiffCallback()) {
    var clickListener: OnItemClickListener? = null
    var handleLoadNewWords: HandleLoadNewWords? = null
    var mode: ExamMode = ExamMode.DAILY_MODE
    var totalCount: Int = 0 // all word list


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExamAdapterViewHolder {
        return ItemRvExamAdapterBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
            .run { ExamAdapterViewHolder(this) }
    }

    override fun onBindViewHolder(holder: ExamAdapterViewHolder, position: Int) {
        val item = getItem(position)

        handleLoadNewWords?.onLoadNewWords(position)

        with(holder.binding) {
            wordPosition.text = position.plus(1).toString()
            wordPosition.backgroundTintList = setBG(item.status, wordPosition.context)

            wordPositionDots.visibility = when(mode) {
                ExamMode.DAILY_MODE -> {
                    if (position == currentList.size - 1) View.GONE else View.VISIBLE
                }
                ExamMode.INFINITY_MODE -> {
                    // when scroll very fast in infinity scroll position == currentList.size - 1 could not have time to process,
                    // and .... disappeared between previous loading last item and first next. For avoid this we make if with total count
                    if (position == totalCount - 1) View.GONE else View.VISIBLE
                }
            }

            if (item.isActive) {
                wordPositionContainer.background = ContextCompat.getDrawable(
                    holder.binding.root.context,
                    R.drawable.prev_next_exam_button
                )
            } else {
                wordPositionContainer.background = null
            }

            holder.binding.root.setOnClickListener {
                clickListener?.onItemClick(root, position)
            }
        }
    }

    private fun setBG(status: ExamWordStatus, context: Context): ColorStateList {
        return when (status) {
            ExamWordStatus.SUCCESS -> {
                context.resources.getColorStateList(R.color.green)
            }
            ExamWordStatus.FAIL -> {
                context.resources.getColorStateList(R.color.red)
            }
            ExamWordStatus.IN_PROCESS -> {
                context.resources.getColorStateList(R.color.blue)
            }
            ExamWordStatus.UNPROCESSED -> {
                context.resources.getColorStateList(R.color.grey)
            }
        }
    }

    interface HandleLoadNewWords {
        fun onLoadNewWords(position: Int)
    }

    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }
}