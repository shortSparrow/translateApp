package com.example.ttanslateapp.presentation.list_full

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.media.AudioManager
import android.media.MediaPlayer
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.ttanslateapp.domain.model.modify_word.WordRV
import com.example.ttanslateapp.domain.use_case.lists.GetListsUseCase
import com.example.ttanslateapp.presentation.MainActivity
import com.example.ttanslateapp.presentation.modify_word.ModifyWordModes
import com.example.ttanslateapp.util.getAudioPath
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject


inline fun <reified Activity : ComponentActivity> Context.getActivity(): Activity? {
    return when (this) {
        is Activity -> this
        else -> {
            var context = this
            while (context is ContextWrapper) {
                context = context.baseContext
                if (context is Activity) return context
            }
            null
        }
    }
}

@HiltViewModel
class ListsFullViewModel @Inject constructor(
    private val getListsUseCase: GetListsUseCase,
    private val application: Application,
) : ViewModel() {
    var state by mutableStateOf(ListFullState())
        private set

    private var searchJob: Job? = null
    private val player = MediaPlayer()
    private var navController: NavController? = null

    var initialMount = false

    private val audioManager = application.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private val oldVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

    fun setNavController(navController: NavController) {
        this.navController = navController
    }


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
        );

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
            );
        }
    }

    fun onAction(action: ListFullAction) {
        when (action) {
            ListFullAction.GoBack -> {
                navController?.popBackStack()
            }
            is ListFullAction.SearchWord -> {
                searchDebounced(action.query)
            }
            ListFullAction.TakeExam -> {
                navController?.navigate(
                    ListFullFragmentDirections.actionListFullFragmentToExamKnowledgeWordsFragment(
                        listId = state.listId
                    )
                )
            }
            is ListFullAction.PressOnWord -> {
                navController?.navigate(
                    ListFullFragmentDirections.actionListFullFragmentToModifyWordFragment(
                        mode = ModifyWordModes.MODE_EDIT,
                        wordId = action.wordId
                    )
                )
            }
            ListFullAction.AddNewWord -> {
                navController?.navigate(
                    ListFullFragmentDirections.actionListFullFragmentToModifyWordFragment(
                        mode = ModifyWordModes.MODE_ADD,
                        listId = state.listId
                    )
                )
            }
            is ListFullAction.InitialLoadData -> {
                state =
                    state.copy(listId = action.listId, loadingStatusWordList = LoadingState.PENDING)
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
}