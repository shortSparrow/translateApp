package com.ovolk.dictionary.presentation.modify_word.compose.languages_picker

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.modify_word.SelectLanguage
import com.ovolk.dictionary.domain.model.select_languages.LanguagesType
import com.ovolk.dictionary.presentation.modify_word.Languages
import com.ovolk.dictionary.presentation.modify_word.ModifyWordAction
import com.ovolk.dictionary.presentation.modify_word.compose.alerts.AddNewLangBottomSheet

@Composable
fun LanguagesPicker(
    state: Languages,
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
                text = stringResource(id = R.string.modify_word_language_picker_from),
                fontSize = 12.sp,
                modifier = Modifier.width(110.dp),
                textAlign = TextAlign.Center
            )

            Text(
                text = stringResource(id = R.string.modify_word_language_picker_to),
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
@Preview(showBackground = true)
fun LanguagesPickerPreview() {
    LanguagesPicker(
        state = Languages(
            languageFromList = listOf(
                SelectLanguage(langCode = "EN", name = "English", nativeName = "English", isChecked = true)
            ),
            languageToList = listOf(
                SelectLanguage(langCode = "UA", name = "ukrainian", nativeName = "Українська", isChecked = true)
            )
        ),
        onAction = {}
    )
}

@Composable
@Preview(showBackground = true)
fun LanguagesPickerPreview2() {
    LanguagesPicker(
        state = Languages(
            languageFromList = listOf(
                SelectLanguage(langCode = "EN", name = "English", nativeName = "English", isChecked = false)
            ),
            languageToList = listOf(
                SelectLanguage(langCode = "UA", name = "ukrainian", nativeName = "Українська", isChecked = false)
            )
        ),
        onAction = {}
    )
}