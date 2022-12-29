package com.ovolk.dictionary.presentation.modify_word.compose.text_fields

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ovolk.dictionary.R
import com.ovolk.dictionary.presentation.modify_word.ModifyWordAction

@Composable
fun TextFieldDescription(descriptionWord: String, onAction: (ModifyWordAction) -> Unit) {

    OutlinedTextField(
        value = descriptionWord,
        onValueChange = { value -> onAction(ModifyWordAction.OnChangeDescription(value)) },
        label = { Text(text = stringResource(id = R.string.modify_word_description)) },
        modifier = Modifier
            .height(100.dp)
            .fillMaxWidth(1f),
    )
}

@Composable
@Preview(showBackground = true)
fun TextFieldDescriptionPreview() {
    TextFieldDescription(descriptionWord = "", onAction = {})
}