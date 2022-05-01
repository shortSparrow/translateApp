package com.example.ttanslateapp.presentation.layout

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.withStyledAttributes
import com.example.ttanslateapp.R
import kotlin.properties.Delegates

// TODO Додати AttributeSet, щоб мати кілька стейтів
// Можна використуовувати, тыльки якщо вона  не змынюэться динамічно https://medium.com/android-news/demystifying-the-jvmoverloads-in-kotlin-10dd098e6f72
class TranslatedWord @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private var state by Delegates.notNull<Int>()

    init {
        LayoutInflater.from(context).inflate(R.layout.input_translated_word, this, true)
        context.withStyledAttributes(attrs, R.styleable.TranslatedWord, defStyleAttr, 0) {
            state = getInt(R.styleable.TranslatedWord_state, 0)
        }
    }

    fun test() {

    }
}


///**
// * Це без JvmOverloads, тому ми пишемо усі 4 контурктори
// * Якщо не передам жождних attr то буде викликано 1-ий констурктор, а в @JvmOverloads 4-ий але з дефолтними параметрами
// *
// * https://medium.com/@mmlodawski/https-medium-com-mmlodawski-do-not-always-trust-jvmoverloads-5251f1ad2cfe
// */
//class TranslatedWord: ConstraintLayout {
//    constructor(context: Context) : super(context) {
//        prepared(context)
//    }
//    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
//        prepared(context)
//    }
//    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
//        prepared(context)
//    }
//
//    private fun prepared(context: Context) {
//        LayoutInflater.from(context).inflate(R.layout.input_translated_word, this, true)
//    }
//}