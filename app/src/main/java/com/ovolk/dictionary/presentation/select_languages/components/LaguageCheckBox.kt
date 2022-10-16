package com.ovolk.dictionary.presentation.select_languages.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.select_languages.Language

@Composable
fun LanguageCheckBox(language: Language, onCheck: (language: Language) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheck(language) }) {
        Checkbox(
            checked = language.isChecked,
            onCheckedChange = { onCheck(language) },
            colors = CheckboxDefaults.colors(checkedColor = colorResource(id = R.color.blue))
        )
        Text(text = language.nativeName)
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LanguageCheckBoxPreview() {
    Column() {
        LanguageCheckBox(
            language = Language(langCode = "uk", name = "Ukrainian", nativeName = "українська"),
            onCheck = {}
        )
    }
}