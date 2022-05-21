package com.example.ttanslateapp.presentation.word_list.adapter

import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.ttanslateapp.databinding.WordRvItemBinding
import com.example.ttanslateapp.domain.model.WordRV
import com.example.ttanslateapp.presentation.MainActivity
import kotlin.collections.set

class WordListAdapter : ListAdapter<WordRV, WordItemViewHolder>(WordListAdapterDiffCallback()) {
    var onClickListener: OnClickListener? = null
    private val player = MediaPlayer()
    private val playingList = mutableMapOf<Long, Boolean>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordItemViewHolder {
        return WordRvItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
            .run { WordItemViewHolder(this, player, playingList) }
    }

    override fun onBindViewHolder(holder: WordItemViewHolder, position: Int) {
        val word = getItem(position)
        holder.bind(word, onClickListener)
    }

    interface OnClickListener {
        fun onRootClickListener(wordId: Long)
    }
}