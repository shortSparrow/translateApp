package com.ovolk.dictionary.presentation.modify_dictionary.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.modify_word.ValidateResult

@Composable
fun PickLanguageButton(
    validationError: ValidateResult,
    hintMessage: String? = null,
    langName: String? = null,
    onClick: () -> Unit
) {
    val selectLanguagePickerWidth = 110.dp
    val iconSize = 24

    val errorColor =
        colorResource(id = R.color.red)
    val borderColor = if (validationError.successful) {
        colorResource(id = R.color.blue)
    } else {
        errorColor
    }

    Column(
        modifier = Modifier
            .width(selectLanguagePickerWidth),

        ) {
        Surface(
            shape = RoundedCornerShape(50),
            border = BorderStroke(width = 1.dp, color = borderColor),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable { onClick() }
                    .padding(6.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.language),
                        contentDescription = stringResource(id = R.string.cd_lang_icon),
                        modifier = Modifier.size(iconSize.dp)
                    )
                    if (langName == null && hintMessage != null) {
                        Text(
                            text = hintMessage,
                            Modifier.padding(start = 10.dp),
                            color = colorResource(id = R.color.grey),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            softWrap = false,
                            fontSize = 12.sp
                        )
                    }
                    Text(
                        text = langName ?: "",
                        Modifier
                            .padding(start = 5.dp, end = (5 + iconSize).dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        if (!validationError.successful && validationError.errorMessage != null) {
            Text(
                text = validationError.errorMessage as String,
                maxLines = 2,
                fontSize = 12.sp,
                color = colorResource(id = R.color.red),
                overflow = TextOverflow.Ellipsis
            )
        }


    }
}

@Composable
@Preview(showBackground = true)
fun PickLanguageButtonPreview() {
    PickLanguageButton(
        validationError = ValidateResult(),
        hintMessage = null,
        langName = "UK",
        onClick = {})
}

@Composable
@Preview(showBackground = true)
fun PickLanguageButtonPreview2() {
    PickLanguageButton(
        validationError = ValidateResult(
            successful = false,
            errorMessage = "this field is required"
        ), hintMessage = null, langName = "UK", onClick = {})
}

@Composable
@Preview(showBackground = true)
fun PickLanguageButtonPreview3() {
    PickLanguageButton(
        validationError = ValidateResult(),
        hintMessage = "select language",
        langName = null,
        onClick = {})
}