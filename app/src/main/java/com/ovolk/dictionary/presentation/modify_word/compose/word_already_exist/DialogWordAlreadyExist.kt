package com.ovolk.dictionary.presentation.modify_word.compose.word_already_exist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ovolk.dictionary.R
import com.ovolk.dictionary.presentation.core.dialog.MyDialog
import com.ovolk.dictionary.presentation.modify_word.ModifyWordAction
import com.ovolk.dictionary.presentation.modify_word.WordAlreadyExistActions

@Composable
fun DialogWordAlreadyExist(onAction: (ModifyWordAction) -> Unit, wordValue: String) {

    fun onClose() {
        onAction(
            ModifyWordAction.HandleWordAlreadyExistModal(
                WordAlreadyExistActions.CLOSE
            )
        )
    }

    fun onReplace() {
        onAction(
            ModifyWordAction.HandleWordAlreadyExistModal(
                WordAlreadyExistActions.REPLACE
            )
        )
    }

    fun onGoToWord() {
        onAction(
            ModifyWordAction.HandleWordAlreadyExistModal(
                WordAlreadyExistActions.GO_TO_WORD
            )
        )
    }
    MyDialog(
        title = stringResource(
            id = R.string.modify_word_dialog_word_already_exist_title,
            wordValue
        ),
        onDismissRequest = ::onClose,
        content = {
            Text(text = stringResource(id = R.string.modify_word_dialog_word_already_exist_description))
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            ) {
                Box(Modifier.weight(1f)) {
                    OutlinedButton(onClick = ::onReplace) {
                        Text(
                            text = stringResource(id = R.string.modify_word_dialog_word_already_exist_replace),
                            color = colorResource(id = R.color.blue),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                Spacer(modifier = Modifier.width(15.dp))
                Box(Modifier.weight(1f)) {
                    OutlinedButton(onClick = ::onGoToWord, Modifier.align(Alignment.BottomEnd)) {
                        Text(
                            text = stringResource(id = R.string.modify_word_dialog_word_already_exist_go_to_word),
                            color = colorResource(id = R.color.blue),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun DialogWordAlreadyExistPreview() {
    DialogWordAlreadyExist(
        onAction = {},
        wordValue = "Horse"
    )
}