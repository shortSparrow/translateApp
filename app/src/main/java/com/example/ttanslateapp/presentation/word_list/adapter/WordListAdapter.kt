package com.example.ttanslateapp.presentation.word_list.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.doOnPreDraw
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.ListAdapter
import com.example.ttanslateapp.R
import com.example.ttanslateapp.databinding.WordRvItemBinding
import com.example.ttanslateapp.domain.model.WordRV
import com.example.ttanslateapp.presentation.modify_word.adapter.translate.TranslateAdapter
import kotlin.collections.mutableMapOf
import kotlin.collections.set


class WordListAdapter(private val context: Context) :
    ListAdapter<WordRV, WordItemViewHolder>(WordListAdapterDiffCallback()) {

    private val isOpenedSet = mutableMapOf<Long, Boolean>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordItemViewHolder {
        val itemBinding =
            WordRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WordItemViewHolder(itemBinding)
    }

    private fun open(binding: WordRvItemBinding) {
        with(binding) {
            Log.d("XXX_open", root.measuredHeight.toString())
            root.updateLayoutParams {
                height = ViewGroup.LayoutParams.WRAP_CONTENT
            }
            root.findViewById<TextView>(R.id.show_more).text = "Приховати"
            binding.showMoreContent.setPadding(0, 0, 0, 50)
        }
    }

    private fun close(binding: WordRvItemBinding) {
        with(binding) {
            Log.d("XXX_close", root.measuredHeight.toString())
            val params: ViewGroup.LayoutParams = root.layoutParams
            params.height = 400
            root.layoutParams = params
            root.findViewById<TextView>(R.id.show_more).text = "Розкрити"
            binding.showMoreContent.setPadding(0, 0, 0, 0)
        }
    }

    override fun onBindViewHolder(holder: WordItemViewHolder, position: Int) {
        val word = getItem(position)
        if (!isOpenedSet.containsKey(word.id)) {
            isOpenedSet[word.id] = false
        }


        with(holder.binding) {
            langFrom.text = word.langFrom
            englishWord.text = word.value
            langTo.text = word.langTo
            if (word.description.isNotEmpty()) {
                descriptionValue.text = word.description
                showMoreContent.visibility = View.VISIBLE
            } else {
                showMoreContent.visibility = View.GONE
            }

            showMore.setOnClickListener {
                isOpenedSet[word.id] = !isOpenedSet[word.id]!!

                if (isOpenedSet[word.id] == true) {
                    open(holder.binding)
                } else {
                    close(holder.binding)
                }
                Log.d("Click", isOpenedSet[word.id].toString())
                Log.d("Click_id", word.id.toString())
                Log.d("Click_map", isOpenedSet.toString())

            }

            // TODO використовуємо TranslateAdapter, звернути увагу, якщо його буде змінено
            val translateAdapter = TranslateAdapter()
            translateList.adapter = translateAdapter
            translateAdapter.submitList(word.translations)


            root.doOnPreDraw {
                Log.d("FFF", root.measuredHeight.toString())
                if (root.measuredHeight > 400) {
                    holder.binding.showMore.visibility = View.VISIBLE

                    if (isOpenedSet[word.id] == true) {
                        open(holder.binding)
                    } else {
                        close(holder.binding)
                    }

                }
            }
        }
    }
}