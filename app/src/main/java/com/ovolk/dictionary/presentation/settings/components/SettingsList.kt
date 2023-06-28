package com.ovolk.dictionary.presentation.settings.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.ovolk.dictionary.BuildConfig
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.settings.SettingsItem
import com.ovolk.dictionary.domain.model.settings.SettingsNavigation
import com.ovolk.dictionary.presentation.settings.SettingsAction


@Composable
fun SettingsList(list: List<SettingsItem>, onAction: (SettingsAction) -> Unit) {

    Column {
        Column(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.gutter))
                .weight(1f)
        ) {
            list.forEach {
                SettingsItem(
                    item = it,
                    onClick = { item -> onAction(SettingsAction.OnPressSettings(item)) }
                )
            }
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = dimensionResource(id = R.dimen.small_gutter))
        ) {
            Text(
                text = stringResource(id = R.string.app_version, BuildConfig.VERSION_NAME),
                color = colorResource(id = R.color.light_grey),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsListPreview() {
    SettingsList(
        list = listOf(
            SettingsItem(
                title = stringResource(id = R.string.settings_language_item_title),
                contentDescription = stringResource(id = R.string.settings_language_cd_item_title),
                iconId = R.drawable.language,
                navigateTo = SettingsNavigation.DICTIONARY_LIST
            ),
            SettingsItem(
                title = stringResource(id = R.string.settings_exam_reminder_item_title),
                contentDescription = stringResource(id = R.string.settings_exam_reminder_cd_item_title),
                iconId = R.drawable.exam_reminder,
                navigateTo = SettingsNavigation.EXAM_REMINDER_SETTINGS
            ),
        ),
        onAction = {}
    )
}