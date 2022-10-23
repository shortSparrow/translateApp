package com.ovolk.dictionary.presentation.modify_word.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ovolk.dictionary.domain.model.modify_word.ModifyWordListItem
import com.ovolk.dictionary.domain.model.modify_word.SelectLanguage
import com.ovolk.dictionary.domain.model.select_languages.LanguagesType
import com.ovolk.dictionary.presentation.modify_word.ComposeState
import com.ovolk.dictionary.presentation.modify_word.ModifyWordAction

@Composable
fun LanguagesPicker(
    state: ComposeState,
    onAction: (ModifyWordAction) -> Unit,
) {

    if (state.addNewLangModal.isOpen) {
        AddNewLangBottomSheet(state = state.addNewLangModal, onAction = onAction)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .width(selectLanguagePickerWidth * 2 + (40 + 20).dp)
                .padding(bottom = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {

            Text(
                text = "(language from)",
                fontSize = 12.sp,
                modifier = Modifier.width(110.dp),
                textAlign = TextAlign.Center
            )

            Text(
                text = "(language to)",
                fontSize = 12.sp,
                modifier = Modifier.width(110.dp),
                textAlign = TextAlign.Center
            )
        }

        Row(
            modifier = Modifier
                .width(selectLanguagePickerWidth * 2 + (40 + 20).dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {


            SelectLanguagePicker(
                state.languageFromList,
                onSelect = { lang ->
                    onAction(
                        ModifyWordAction.OnSelectLanguage(
                            LanguagesType.LANG_FROM,
                            lang
                        )
                    )
                },
                error = state.selectLanguageFromError,
                onAddNewLangPress = { onAction(ModifyWordAction.PressAddNewLanguage(LanguagesType.LANG_FROM)) }
            )


            Column {
                Divider(
                    Modifier
                        .padding(top = 17.dp)
                        .width(40.dp)
                        .height(2.dp)
                )
            }

            SelectLanguagePicker(
                state.languageToList,
                onSelect = { lang ->
                    onAction(
                        ModifyWordAction.OnSelectLanguage(
                            LanguagesType.LANG_TO,
                            lang
                        )
                    )
                },
                error = state.selectLanguageToError,
                onAddNewLangPress = { onAction(ModifyWordAction.PressAddNewLanguage(LanguagesType.LANG_TO)) }
            )

        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun LanguagesPickerPreview() {
    LanguagesPicker(
        state = ComposeState(
            wordListInfo = ModifyWordListItem(
                title = "My List",
                count = 10,
                id = 1L
            ),
            wordLists = emptyList(),
            isOpenAddNewListModal = true,
            isOpenSelectModal = false,
            languageFromList = listOf(
                SelectLanguage(langCode = "EN", name = "English", nativeName = "English")
            ),
            languageToList = listOf(
                SelectLanguage(langCode = "UA", name = "ukrainian", nativeName = "Українська")
            )
        ),
        onAction = {}
    )
}