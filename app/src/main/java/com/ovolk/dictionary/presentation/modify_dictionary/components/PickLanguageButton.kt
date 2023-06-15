package com.ovolk.dictionary.presentation.modify_dictionary.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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

@Composable
fun PickLanguageButton(
    error: Boolean,
    hintMessage: String? = null,
    langName: String? = null,
    onClick: () -> Unit
) {

    val errorColor =
        colorResource(id = R.color.red)
    val borderColor = if (error) {
        errorColor
    } else {
        colorResource(id = R.color.blue)
    }


    val selectLanguagePickerWidth = 110.dp
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
                        contentDescription = stringResource(id = R.string.cd_lang_icon)
                    )
                    Text(
                        text = langName ?: "",
                        Modifier.padding(start = 10.dp),
                    )
                }
//                Icon(
//                    painter = painterResource(id = R.drawable.prev_arrow),
//                    contentDescription = stringResource(id = R.string.cd_arrow_lang),
//                    modifier = Modifier.rotate(rotateArrow)
//                )
            }
        }

        Text(
            text = hintMessage ?: "",
            color = colorResource(id = R.color.grey),
            fontSize = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Visible,
            softWrap = false,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
@Preview(showBackground = true)
fun PickLanguageButtonPreview() {
    PickLanguageButton(error = false, hintMessage = null, langName = "UK", onClick = {})
}

@Composable
@Preview(showBackground = true)
fun PickLanguageButtonPreview2() {
    PickLanguageButton(error = true, hintMessage = null, langName = "UK", onClick = {})
}

@Composable
@Preview(showBackground = true)
fun PickLanguageButtonPreview3() {
    PickLanguageButton(
        error = false,
        hintMessage = "select language",
        langName = "UK",
        onClick = {})
}