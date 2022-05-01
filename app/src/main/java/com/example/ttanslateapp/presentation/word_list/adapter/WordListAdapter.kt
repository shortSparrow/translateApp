package com.example.ttanslateapp.presentation.word_list.adapter

import android.content.Context
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.doOnLayout
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ttanslateapp.R
import com.example.ttanslateapp.databinding.WordRvItemBinding
import com.example.ttanslateapp.domain.model.WordRV
import com.example.ttanslateapp.presentation.modify_word.adapter.translate.TranslateAdapter


class WordListAdapter(private val context: Context) :
    ListAdapter<WordRV, WordItemViewHolder>(WordListAdapterDiffCallback()) {

//    TODO make openedSet for showMore state
    var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordItemViewHolder {
        val itemBinding =
            WordRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WordItemViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: WordItemViewHolder, position: Int) {
        val word = getItem(position)

        with(holder.binding) {
            langFrom.text = word.langFrom
            englishWord.text = word.value
            langTo.text = word.langTo
            showMore.setOnClickListener {
                // зробити реалізацю тут
                onClickListener?.onClickShowMore(holder.binding.root)
            }


            // TODO використовуємо TranslateAdapter, звернути увагу, якщо його буде змінено
            val translateAdapter = TranslateAdapter()
            translateList.adapter = translateAdapter
            translateAdapter.submitList(word.translations)

            // layout точно поразувався
            holder.binding.root.doOnLayout {
                if (translateList.measuredHeight > 150) {
                    showMore.visibility = View.VISIBLE
                } else {
                    showMore.visibility = View.GONE
                }
            }



//            for (translate in word.translations) {
//                val view = LayoutInflater.from(context).inflate(R.layout.translate_chip,  null, false)
//                val text: TextView = view.findViewById(R.id.chip_item)
//                text.text = translate.value
//                holder.binding.translateList.addView(view)
//            }
        }
    }


    interface OnClickListener {
        fun onClickShowMore(view: CardView)
    }


}