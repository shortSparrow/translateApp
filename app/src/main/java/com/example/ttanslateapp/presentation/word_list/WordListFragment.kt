package com.example.ttanslateapp.presentation.word_list

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.ttanslateapp.R
import com.example.ttanslateapp.databinding.FragmentWordListBinding
import com.example.ttanslateapp.presentation.core.BaseFragment
import com.example.ttanslateapp.presentation.core.BindingInflater
import com.example.ttanslateapp.presentation.exam.ExamKnowledgeWordsFragment
import com.example.ttanslateapp.presentation.modify_word.ModifyWordFragment
import com.example.ttanslateapp.presentation.modify_word.ModifyWordModes
import com.example.ttanslateapp.presentation.word_list.adapter.WordListAdapter
import com.example.ttanslateapp.util.getAppComponent
import timber.log.Timber

class WordListFragment : BaseFragment<FragmentWordListBinding>() {

    override val bindingInflater: BindingInflater<FragmentWordListBinding>
        get() = FragmentWordListBinding::inflate

    private val viewModel by viewModels {
        get(WordListViewModel::class.java)
    }

    private val wordListAdapter = WordListAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAppComponent().inject(this)

        viewModel.loadWordList()
        setAdapter()
        clickListeners()
        observeData()

        binding.searchWord.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Timber.d(query.toString())
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                Timber.d("SEARCH")
                viewModel.searchDebounced(query.toString())
                return true
            }
        })
    }

    private fun observeData() = with(binding) {
        viewModel.wordList.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                nothingFoundContainer.root.visibility = View.VISIBLE
                wordListRv.visibility = View.GONE
            } else {
                nothingFoundContainer.root.visibility = View.GONE
                wordListRv.visibility = View.VISIBLE
            }
            wordListAdapter.submitList(it)
        }

        viewModel.dictionaryIsEmpty.observe(viewLifecycleOwner) {
            if (it) {
                emptyListContainer.root.visibility = View.VISIBLE
                wordListContainer.visibility = View.GONE
            } else {
                emptyListContainer.root.visibility = View.GONE
                wordListContainer.visibility = View.VISIBLE
            }
        }

    }

    private fun clickListeners() = with(binding) {
        searchWord.setOnClickListener { searchWord.isIconified = false }
        addNewWord.setOnClickListener { launchAddWordScreen() }
        emptyListContainer.addFirstWord.setOnClickListener { launchAddWordScreen() }

        wordListAdapter.onClickListener = object : WordListAdapter.OnClickListener {
            override fun onRootClickListener(wordId: Long) {
                launchEditWordScreen(wordId)
            }
        }
    }

    private fun launchAddWordScreen() {
        findNavController().navigate(
            WordListFragmentDirections.actionWordListFragmentToModifyWordFragment(ModifyWordModes.MODE_ADD)
        )
    }

    private fun launchEditWordScreen(wordId: Long) {
        Timber.d("wordId ${wordId}")

        findNavController().navigate(
//            WordListFragmentDirections.actionWordListFragmentToModifyWordFragment(
//                mode = ModifyWordModes.MODE_EDIT,
//                wordId = wordId
//            )
            WordListFragmentDirections.actionWordListFragmentToModifyWordFragment(
                mode = ModifyWordModes.MODE_EDIT,
                wordId = wordId
            )
        )
    }

    private fun setAdapter() {
        wordListAdapter
            .apply { binding.wordListRv.adapter = this }

        val callback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = wordListAdapter.currentList[viewHolder.adapterPosition]
                viewModel.deleteWordById(item.id)
            }
        }

        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(binding.wordListRv)
    }

}