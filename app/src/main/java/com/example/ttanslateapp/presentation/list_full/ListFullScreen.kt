package com.example.ttanslateapp.presentation.list_full

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.example.ttanslateapp.domain.model.modify_word.WordAudio
import com.example.ttanslateapp.domain.model.modify_word.WordRV
import com.example.ttanslateapp.domain.model.modify_word.modify_word_chip.Translate
import com.example.ttanslateapp.presentation.list_full.components.Header
import com.example.ttanslateapp.presentation.list_full.components.WordItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListFullScreen(
    state: ListFullState,
    onAction: (ListFullAction) -> Unit
) {
    Column() {
        Header(
            isVisibleRemoveFromList = true,
            onRemoveFromListPress = {}
        )

        CompositionLocalProvider(
            LocalOverscrollConfiguration provides null
        ) {
            LazyColumn() {
                items(state.wordList) { word ->
                    WordItem(word = word, onAction = onAction)
                }
            }
        }
        Text(text = "FULL LIST")
    }
}

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_2)
@Composable
fun PreviewListFullScreen() {
    ListFullScreen(
        onAction={},
        state = ListFullState(
            wordList = listOf(
                WordRV(
                    id = 1L,
                    langFrom = "EN",
                    langTo = "UA",
                    transcription = "Юнікорн",
                    value = "Unicorn",
                    sound = WordAudio(fileName = "PATH"),

                    translates = listOf(
                        Translate(
                            id = 1L,
                            localId = 1L,
                            createdAt = 13313131L,
                            updatedAt = 13343131L,
                            value = "Єдиноріг",
                            isHidden = false
                        ),
                        Translate(
                            id = 2L,
                            localId = 2L,
                            createdAt = 23313131L,
                            updatedAt = 23343131L,
                            value = "Коняка",
                            isHidden = true
                        ),

                        Translate(
                            id = 2L,
                            localId = 2L,
                            createdAt = 23313131L,
                            updatedAt = 23343131L,
                            value = "Коняка",
                            isHidden = true
                        ),
                        Translate(
                            id = 2L,
                            localId = 2L,
                            createdAt = 23313131L,
                            updatedAt = 23343131L,
                            value = "Коняка",
                            isHidden = true
                        ),
                        Translate(
                            id = 2L,
                            localId = 2L,
                            createdAt = 23313131L,
                            updatedAt = 23343131L,
                            value = "Коняка",
                            isHidden = true
                        ),
                        Translate(
                            id = 2L,
                            localId = 2L,
                            createdAt = 23313131L,
                            updatedAt = 23343131L,
                            value = "Коняка",
                            isHidden = true
                        ),
                        Translate(
                            id = 2L,
                            localId = 2L,
                            createdAt = 23313131L,
                            updatedAt = 23343131L,
                            value = "Коняка",
                            isHidden = true
                        ),
                        Translate(
                            id = 1L,
                            localId = 1L,
                            createdAt = 13313131L,
                            updatedAt = 13343131L,
                            value = "Єдиноріг",
                            isHidden = false
                        ),
                    ),
                    description = "This is description",

                    )
            )
        ),
    )
}