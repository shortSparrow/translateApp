package com.ovolk.dictionary.presentation.settings_dictionaries.components

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.dictionary.Dictionary
import com.ovolk.dictionary.presentation.settings_dictionaries.SettingsDictionariesAction

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DictionaryItem(
    dictionary: Dictionary,
    isSomeDictionarySelected: Boolean = false,
    onAction: (SettingsDictionariesAction) -> Unit
) {
    val context = LocalContext.current

    fun selectDictionary() {
        onAction(SettingsDictionariesAction.OnSelectDictionary(dictionary.id))
    }

    fun setDictionaryActive() {
        onAction(SettingsDictionariesAction.OnPressDictionary(dictionary.id))
    }

    fun onStartClick() {
        Toast.makeText(context, "This dictionary active", Toast.LENGTH_SHORT).show()
    }


    val borderColor =
        if (dictionary.isSelected) colorResource(id = R.color.green) else colorResource(id = R.color.blue_2)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(horizontal = 0.dp)
            .padding(bottom = 10.dp),
    ) {
        Box(modifier = Modifier.size(30.dp)) {
            if (dictionary.isActive) {
                Surface(shape = CircleShape) {
                    Icon(
                        painter = painterResource(R.drawable.star),
                        contentDescription = "active",
                        modifier = Modifier.clickable { onStartClick() },
                        tint = colorResource(id = R.color.blue)
                    )
                }
            }
        }
        Surface(
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(
                width = 2.dp, color = borderColor,
            ),
            modifier = Modifier
                .padding(start = 12.dp)
                .weight(1f)
        ) {

            Box(
                modifier = Modifier
                    .combinedClickable(
                        onLongClick = { selectDictionary() },
                        onClick = { if (isSomeDictionarySelected) selectDictionary() else setDictionaryActive() },
                    )
                    .padding(vertical = 16.dp, horizontal = 25.dp)
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = dictionary.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DictionaryItemPreviewActive() {
    DictionaryItem(
        dictionary = Dictionary(
            id = 0L,
            title = "EN-UA",
            langFromCode = "EN",
            langToCode = "UA",
            isActive = true,
            isSelected = false,
        ),
        onAction = {}
    )
}

@Preview(showBackground = true)
@Composable
fun DictionaryItemPreviewNotActive() {
    DictionaryItem(
        dictionary = Dictionary(
            id = 0L,
            title = "EN-FR",
            langFromCode = "EN",
            langToCode = "FR",
            isActive = false,
            isSelected = false,
        ),
        onAction = {}
    )
}

@Preview(showBackground = true)
@Composable
fun DictionaryItemPreviewSelected() {
    DictionaryItem(
        dictionary = Dictionary(
            id = 0L,
            title = "EN-FR",
            langFromCode = "EN",
            langToCode = "FR",
            isActive = false,
            isSelected = true,
        ),
        onAction = {}
    )
}