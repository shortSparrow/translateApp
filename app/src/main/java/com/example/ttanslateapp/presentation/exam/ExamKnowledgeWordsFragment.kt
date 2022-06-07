package com.example.ttanslateapp.presentation.exam

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.allViews
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import com.example.ttanslateapp.R
import com.example.ttanslateapp.databinding.FragmentExamKnowledgeWordsBinding
import com.example.ttanslateapp.domain.model.exam.ExamWord
import com.example.ttanslateapp.presentation.core.BaseFragment
import com.example.ttanslateapp.presentation.core.BindingInflater
import com.example.ttanslateapp.presentation.exam.adapter.ExamAdapter
import com.example.ttanslateapp.presentation.modify_word.ModifyWordModes
import com.example.ttanslateapp.presentation.word_list.WordListFragmentDirections
import com.example.ttanslateapp.util.getAppComponent
import com.example.ttanslateapp.util.setOnTextChange
import timber.log.Timber


class ExamKnowledgeWordsFragment : BaseFragment<FragmentExamKnowledgeWordsBinding>() {
    private var selectableHint: TextView? = null

    override val bindingInflater: BindingInflater<FragmentExamKnowledgeWordsBinding>
        get() = FragmentExamKnowledgeWordsBinding::inflate

    private val viewModel by viewModels {
        get(ExamKnowledgeWordsViewModel::class.java)
    }
    private val examAdapter = ExamAdapter()

    override fun onPrepareOptionsMenu(menu: Menu){
        super.onPrepareOptionsMenu(menu)
        val item = menu.findItem(R.id.exam)
        item.isVisible = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAppComponent().inject(this)

        setupAdapter()
        observeLiveDate()
        clickListeners()
        viewModel.generateWordsList()

        binding.examWordInput.setOnTextChange {
            binding.examWordContainer.error = null
            // clear variant backgroundTintList on user change input manually
            if (it.toString() != selectableHint?.text.toString()) {
                selectableHint?.backgroundTintList = null
            }
        }
    }

    private fun clickListeners() = with(binding) {
        showVariantsLabel.setOnClickListener {
            viewModel.toggleVisibleVariants()
        }
        showHintsLabel.setOnClickListener {
            viewModel.toggleVisibleHint()
        }
        showNextHintButton.setOnClickListener {
            viewModel.showNextHint()
        }
        emptyListContainer.addFirstWord.setOnClickListener {
            val action =
                ExamKnowledgeWordsFragmentDirections.actionExamKnowledgeWordsFragmentToModifyWordFragment(
                    mode = ModifyWordModes.MODE_ADD
                )
            findNavController().navigate(action)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun observeLiveDate() = with(binding) {

        viewModel.examWordListEmpty.observe(viewLifecycleOwner) {
            if (it) {
                examContainer.visibility = View.GONE
                emptyListContainer.root.visibility = View.VISIBLE
            } else {
                examContainer.visibility = View.VISIBLE
                emptyListContainer.root.visibility = View.GONE
            }
            progressBar.visibility = View.GONE
        }

        viewModel.examWordList.observe(viewLifecycleOwner) {
            examAdapter.submitList(it)
        }

        viewModel.isInoutWordInvalid.observe(viewLifecycleOwner) {
            if (it) {
                examWordContainer.error = getString(R.string.modify_word_invalid_word_value)
            }
        }

        viewModel.isShownVariants.observe(viewLifecycleOwner) {
            if (it) {
                showVariantsContainer.visibility = View.VISIBLE
            } else {
                showVariantsContainer.visibility = View.GONE
            }
        }

        viewModel.isShownHintsVisible.observe(viewLifecycleOwner) {
            if (it) {
                showHintsContainer.visibility = View.VISIBLE
                showNextHintButton.visibility =
                    if (viewModel.allHintsShown.value == true || viewModel.currentWord.value?.hints?.isEmpty() == true) View.GONE else View.VISIBLE
            } else {
                showHintsContainer.visibility = View.GONE
                showNextHintButton.visibility = View.GONE
            }
        }

//        viewModel.isExamEnd.observe(viewLifecycleOwner) {
//            if (it) {
//                Toast.makeText(requireContext(), "The end", Toast.LENGTH_SHORT).show()
//            }
//        }

        viewModel.answerResult.observe(viewLifecycleOwner) {
            if (it == AnswerResult.SUCCESS) {
                selectableHint?.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.green)
            }

            if (it == AnswerResult.FAILED) {
                selectableHint?.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.red)
            }

            showVariantsContainer.allViews.forEach {
                it.isClickable = false
            }
        }

        viewModel.currentWord.observe(viewLifecycleOwner) {
            examWordName.text = it.value
            examWordInput.text = null
            renderShowVariants(it)

            if (it.hints.isEmpty()) {
                showHintsLabel.visibility = View.GONE
            }
        }

        viewModel.countShownHints.observe(viewLifecycleOwner) { count ->
            if (viewModel.currentWord.value?.hints?.isNotEmpty() == true && count > 0) {
                generateHints(viewModel.currentWord.value!!, count)
            }
        }

        viewModel.allHintsShown.observe(viewLifecycleOwner) {
            if (it) {
                showNextHintButton.visibility = View.GONE
            }
        }
    }


    private fun generateHints(examWord: ExamWord, count: Int) = with(binding) {
        showHintsContainer.removeAllViews()
        for (i in (0 until count)) {
            val view = LayoutInflater.from(showHintsContainer.context)
                .inflate(R.layout.item_exam_word_hint, showHintsContainer, false)
            view.findViewById<TextView>(R.id.hint_value).text = examWord.hints[i].value
            showHintsContainer.addView(view)
        }
    }

    private fun renderShowVariants(examWord: ExamWord) = with(binding) {
        for (i in (0 until examWord.answerVariants.size)) {
            val view = LayoutInflater.from(showVariantsContainer.context)
                .inflate(R.layout.translate_chip, showVariantsContainer, false)
            val textItem = view.findViewById<TextView>(R.id.chip_item)
            textItem.text = examWord.answerVariants[i].value

            textItem.setOnClickListener {
                showVariantsContainer.allViews.forEach {
                    it.backgroundTintList = null
                }

                it.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.blue)

                selectableHint = textItem
                examWordInput.setText(textItem.text)
            }
            showVariantsContainer.addView(view)
        }
    }

    private fun setupAdapter() = with(binding) {
        wordPositionRv.adapter = examAdapter
        examCheckAnswer.setOnClickListener {
            if (viewModel.answerResult.value == AnswerResult.EMPTY) {
                viewModel.checkAnswer(examWordInput.text.toString()) {
                    val centerOfScreen: Int = wordPositionRv.width / 2 - 100
                    (wordPositionRv.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
                        viewModel.activeWordPosition,
                        centerOfScreen
                    )

                    if (viewModel.isExamEnd.value == false) {
                        examCheckAnswer.text = getString(R.string.next)
                    } else {
                        examCheckAnswer.text = getString(R.string.finish)
                        examCheckAnswer.setOnClickListener {
                            findNavController().popBackStack()
                        }
                    }

                }

            } else {
                showVariantsContainer.removeAllViews()
                viewModel.goToNextQuestion()
                examCheckAnswer.text = getString(R.string.check_answer)
            }
        }

        // disable scroll with touch
        val disabler: OnItemTouchListener = RecyclerViewDisabler()
        wordPositionRv.addOnItemTouchListener(disabler)
    }

}

class RecyclerViewDisabler : OnItemTouchListener {
    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        return true
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
}