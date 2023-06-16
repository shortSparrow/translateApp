package com.ovolk.dictionary.presentation.core.select_language

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.select_languages.Language

@Composable
fun LanguageListItem(language: Language, onCheck: (language: Language) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheck(language) }) {
        RadioButton(
            selected = language.isChecked,
            onClick = { onCheck(language) },
            colors = RadioButtonDefaults.colors(selectedColor = colorResource(id = R.color.blue))
        )
        Text(text = language.nativeName.replaceFirstChar { it.uppercase() })
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LanguageListItemPreview() {
    Column {
        LanguageListItem(
            language = Language(langCode = "uk", name = "Ukrainian", nativeName = "українська"),
            onCheck = {}
        )
    }
}