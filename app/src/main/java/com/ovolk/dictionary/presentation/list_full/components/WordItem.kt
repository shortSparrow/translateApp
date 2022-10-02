package com.ovolk.dictionary.presentation.list_full.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.modify_word.WordAudio
import com.ovolk.dictionary.domain.model.modify_word.WordRV
import com.ovolk.dictionary.domain.model.modify_word.modify_word_chip.Translate
import com.ovolk.dictionary.presentation.core.compose.flow_row.FlowRow
import com.ovolk.dictionary.presentation.list_full.ListFullAction

@Composable
fun WordItem(
    word: WordRV,
    onAction: (ListFullAction) -> Unit
) {
    var isExpanded by rememberSaveable {
        mutableStateOf(false)
    }

    val isShowMoreExist by rememberSaveable {
        mutableStateOf(word.description.isNotEmpty())
    }

    var isAudioPlay by rememberSaveable {
        mutableStateOf(false)
    }

    val iconColor =
        if (isAudioPlay) colorResource(id = R.color.green) else colorResource(id = R.color.blue)
    val showMoreLess =
        if (isExpanded) stringResource(id = R.string.show_less) else stringResource(id = R.string.show_more)


    Column() {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .clickable { onAction(ListFullAction.PressOnWord(wordId = word.id)) },
            elevation = 3.dp,

            ) {
            Surface(modifier = Modifier.padding(8.dp)) {
                Column {
                    Row(horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = word.langFrom)

                        Row(
                            Modifier
                                .weight(1f)
                                .padding(
                                    top = dimensionResource(id = R.dimen.small_gutter),
                                    start = dimensionResource(id = R.dimen.gutter),
                                    end = dimensionResource(id = R.dimen.small_gutter)
                                )
                        ) {
                            Text(
                                text = word.value,
                                Modifier
                                    .weight(7f)
                            )
                            Text(
                                text = word.transcription,
                                Modifier
                                    .weight(3f)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .width(30.dp)
                                .height(40.dp)
                        ) {
                            if (word.sound != null) {
                                Icon(
                                    painter = painterResource(id = R.drawable.volume_up_available),
                                    contentDescription = stringResource(id = R.string.full_lists_cd_play_sound),
                                    tint = iconColor,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(top = dimensionResource(id = R.dimen.small_gutter))
                                        .clickable {
                                            onAction(
                                                ListFullAction.PlayAudio(
                                                    onStartListener = { isAudioPlay = true },
                                                    word = word,
                                                    onCompletionListener = { isAudioPlay = false })
                                            )
                                        }
                                )
                            }
                        }
                    }

                    Row(
                        Modifier
                            .padding(top = dimensionResource(id = R.dimen.large_gutter))
                    ) {
                        Text(
                            text = word.langTo,
                        )

                        FlowRow {
                            word.translates.forEach { translate ->
                                Surface(
                                    modifier = Modifier.padding(8.dp)
                                ) {
                                    TranslateChipItem(title = translate.value)
                                }
                            }
                        }
                    }


                    if (isExpanded && isShowMoreExist) {
                        Column(
                            Modifier
                                .padding(top = dimensionResource(id = R.dimen.small_gutter))
                        ) {
                            Text(text = stringResource(id = R.string.full_lists_cd_word_description))
                            Text(
                                text = word.description,
                                Modifier
                                    .padding(start = dimensionResource(id = R.dimen.small_gutter))
                            )
                        }

                    }

                    if (isShowMoreExist) {
                        Text(
                            text = showMoreLess,
                            Modifier
                                .align(Alignment.End)
                                .clickable { isExpanded = !isExpanded },
                            colorResource(id = R.color.purple_700)
                        )
                    }
                }

            }
        }
    }
}


fun getPreviewTranslates() = listOf(
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
        id = 1L,
        localId = 1L,
        createdAt = 13313131L,
        updatedAt = 13343131L,
        value = "Єдиноріг",
        isHidden = false
    ),
)

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_2)
@Composable
fun PreviewListFullScreen1() {

    WordItem(
        onAction = {},
        word = WordRV(
            id = 1L,
            langFrom = "EN",
            langTo = "UA",
            transcription = "Юнікорн",
            value = "Unicorn",
            sound = WordAudio(fileName = "PATH"),
            translates = getPreviewTranslates(),
            description = "This is description",
        )
    )
}

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_2)
@Composable
fun PreviewListFullScreen2() {
    WordItem(
        onAction = {},
        word = WordRV(
            id = 1L,
            langFrom = "EN",
            langTo = "UA",
            transcription = "Юнікорн",
            value = "Unicorn",
            sound = null,
            translates = getPreviewTranslates(),
            description = "",
        )
    )
}