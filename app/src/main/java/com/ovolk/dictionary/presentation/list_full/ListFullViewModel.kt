package com.ovolk.dictionary.presentation.list_full

import android.app.Application
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ovolk.dictionary.domain.model.modify_word.WordRV
import com.ovolk.dictionary.domain.use_case.lists.GetListsUseCase
import com.ovolk.dictionary.util.helpers.getAudioPath
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class ListsFullViewModel @Inject constructor(
    private val getListsUseCase: GetListsUseCase,
    private val application: Application,
) : ViewModel() {
    var state by mutableStateOf(ListFullState())
        private set

    private var searchJob: Job? = null
    private val player = MediaPlayer()
    var listener: Listener? = null

    private val audioManager = application.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private val oldVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

    private fun searchDebounced(searchText: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            getListsUseCase.searchWordListByListId(listId = state.listId, query = searchText)
                .collectLatest {
                    state = state.copy(
                        wordList = it,
                        loadingStatusWordList = LoadingState.SUCCESS,
                        noAnyWords = if (searchText.isEmpty()) it.isEmpty() else state.noAnyWords // on first load and any case when delete search value
                    )
                }
        }
    }


    private fun playAudio(
        word: WordRV,
        onCompletionListener: () -> Unit,
        onStartListener: () -> Unit
    ) {
        if (player.isPlaying) return
        onStartListener()

        audioManager.setStreamVolume(
            AudioManager.STREAM_MUSIC,
            audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
            0
        )

        player.apply {
            try {
                reset()
                setDataSource(getAudioPath(application, word.sound!!.fileName))
                prepare()
                start()
            } catch (e: IOException) {
                Timber.e("prepare() failed $e")
            }
        }
        player.setOnCompletionListener {
            onCompletionListener()
            audioManager.setStreamVolume(
                AudioManager.STREAM_MUSIC,
                oldVolume,
                0
            )
        }
    }

    fun onAction(action: ListFullAction) {
        when (action) {
            is ListFullAction.SearchWord -> {
                searchDebounced(action.query)
            }

            ListFullAction.TakeExam -> {
                listener?.navigateToExam(listId = state.listId, listName = state.listName)
            }

            is ListFullAction.PressOnWord -> {
                listener?.navigateToEditWord(
                    wordId = action.wordId
                )
            }

            ListFullAction.AddNewWord -> {
                listener?.navigateToAddWord(
                    listId = state.listId,
                    dictionaryId = state.dictionaryId
                )
            }

            is ListFullAction.InitialLoadData -> {
                state =
                    state.copy(
                        listId = action.listId,
                        loadingStatusWordList = LoadingState.PENDING,
                        dictionaryId = action.dictionaryId,
                    )
                viewModelScope.launch(Dispatchers.IO) {
                    val title = getListsUseCase.getListById(action.listId)?.title ?: ""
                    withContext(Dispatchers.Main) {
                        state = state.copy(
                            listName = title
                        )
                    }
                }
                searchDebounced("")
            }

            is ListFullAction.PlayAudio -> {
                playAudio(
                    action.word,
                    onCompletionListener = action.onCompletionListener,
                    onStartListener = action.onStartListener
                )
            }
        }
    }


    interface Listener {
        fun navigateToExam(listId: Long, listName: String)
        fun navigateToEditWord(wordId: Long)
        fun navigateToAddWord(listId: Long, dictionaryId: Long)
    }
}