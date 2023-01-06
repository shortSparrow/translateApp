package com.ovolk.dictionary.presentation.core.word_item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.modify_word.WordAudio
import com.ovolk.dictionary.domain.model.modify_word.WordRV
import com.ovolk.dictionary.presentation.core.chip.TranslateChipItem
import com.ovolk.dictionary.presentation.core.flow_row.FlowRow
import com.ovolk.dictionary.util.compose.click_effects.opacityClick
import com.ovolk.dictionary.util.helpers.get_preview_models.getPreviewTranslates

@Composable
fun WordItem(
    word: WordRV,
    onWordClick: (wordId: Long) -> Unit,
    onPlayAudioClick: (
        onStartListener: () -> Unit,
        word: WordRV,
        onCompletionListener: () -> Unit,
    ) -> Unit
) {
    var isExpanded by rememberSaveable {
        mutableStateOf(false)
    }

    val isShowMoreExist = word.description.isNotEmpty()

    var isAudioPlay by rememberSaveable {
        mutableStateOf(false)
    }

    val iconColor =
        if (isAudioPlay) colorResource(id = R.color.green) else colorResource(id = R.color.blue)
    val showMoreLess =
        if (isExpanded) stringResource(id = R.string.show_less) else stringResource(id = R.string.show_more)

    Column {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .clickable { onWordClick(word.id) },
            elevation = 3.dp,

            ) {
            Surface(modifier = Modifier.padding(8.dp)) {
                Column {
                    Row(horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = word.langFrom.uppercase())

                        Row(
                            Modifier
                                .weight(1f)
                                .padding(
                                    start = dimensionResource(id = R.dimen.gutter),
                                    end = if (word.sound != null) 10.dp else 0.dp
                                )
                        ) {
                            Text(
                                text = word.value,
                                Modifier
                                    .weight(7f)
                                    .padding(end = 10.dp),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = word.transcription,
                                modifier = Modifier
                                    .weight(3f)
                                    .padding(start = 10.dp),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }


                        if (word.sound != null) {
                            Surface(
                                modifier = Modifier.size(35.dp),
                                shape = CircleShape
                            ) {
                                Box(
                                    Modifier.clickable {
                                        onPlayAudioClick(
                                            { isAudioPlay = true },
                                            word,
                                            { isAudioPlay = false }
                                        )
                                    },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.volume_up_available),
                                        contentDescription = stringResource(id = R.string.full_lists_cd_play_sound),
                                        tint = iconColor,
                                        modifier = Modifier
                                            .size(30.dp)
                                    )
                                }
                            }
                        }
                    }

                    Row(Modifier.padding(top = dimensionResource(id = R.dimen.large_gutter))) {
                        Text(text = word.langTo.uppercase())

                        FlowRow {
                            word.translates.forEach { translate ->
                                Surface(
                                    modifier = Modifier.padding(
                                        start = dimensionResource(id = R.dimen.gutter),
                                        end = 8.dp,
                                        bottom = 8.dp
                                    )
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
                                .opacityClick { isExpanded = !isExpanded },
                            colorResource(id = R.color.blue)
                        )
                    }
                }

            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewListFullScreen1() {
    WordItem(
        onWordClick = {},
        onPlayAudioClick = { start, word, end -> {} },
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

@Preview(showBackground = true)
@Composable
fun PreviewListFullScreen2() {
    WordItem(
        onWordClick = {},
        onPlayAudioClick = { start, word, end -> {} },
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