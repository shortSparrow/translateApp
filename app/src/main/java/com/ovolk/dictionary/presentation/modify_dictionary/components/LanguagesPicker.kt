package com.ovolk.dictionary.presentation.modify_dictionary.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.ovolk.dictionary.domain.model.modify_word.ValidateResult
import com.ovolk.dictionary.domain.model.select_languages.LanguagesType

val selectLanguagePickerWidth = 300.dp

@Composable
fun LanguagesPicker(
    languageToName: String?,
    langToValidation: ValidateResult,
    languageFromName: String?,
    langFromValidation: ValidateResult,
    openLanguageBottomSheet: (langType: LanguagesType) -> Unit
) {
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
            PickLanguageButton(
                langName = languageFromName,
                validationError = langFromValidation,
                hintMessage = if (languageFromName == null) "select language" else null,
                onClick = { openLanguageBottomSheet(LanguagesType.LANG_FROM) }
            )

            Column {
                Divider(
                    Modifier
                        .padding(top = 17.dp)
                        .width(40.dp)
                        .height(2.dp)
                )
            }

            PickLanguageButton(
                langName = languageToName,
                validationError = langToValidation,
                hintMessage = if (languageToName == null) "select language" else null,
                onClick = { openLanguageBottomSheet(LanguagesType.LANG_TO) }
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun LanguagesPickerPreview() {
    LanguagesPicker(
        languageFromName = "EN",
        languageToName = "UK",
        langToValidation = ValidateResult(),
        langFromValidation = ValidateResult(),
        openLanguageBottomSheet = {}
    )
}

@Composable
@Preview(showBackground = true)
fun LanguagesPickerPreview2() {
    LanguagesPicker(
        languageFromName = "FR",
        languageToName = "EN",
        langFromValidation = ValidateResult(),
        langToValidation = ValidateResult(),
        openLanguageBottomSheet = {}
    )
}

@Composable
@Preview(showBackground = true)
fun LanguagesPickerPreview3() {
    LanguagesPicker(
        languageFromName = null,
        languageToName = "EN",
        langFromValidation = ValidateResult(successful = false, errorMessage = "this filed is required"),
        langToValidation = ValidateResult(),
        openLanguageBottomSheet = {}
    )
}