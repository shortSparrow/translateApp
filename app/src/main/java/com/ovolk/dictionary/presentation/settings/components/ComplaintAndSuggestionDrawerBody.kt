package com.ovolk.dictionary.presentation.settings.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.ovolk.dictionary.BuildConfig
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.settings.SettingsItem
import com.ovolk.dictionary.domain.model.settings.SettingsNavigation
import com.ovolk.dictionary.presentation.settings.SettingsAction

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ComplaintAndSuggestionDrawerBody(
    settingsList: List<SettingsItem>,
    onAction: (SettingsAction) -> Unit,
    onComplaintsButtonCLick: () -> Unit,
) {

    CompositionLocalProvider(
        LocalOverscrollConfiguration provides null
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.padding(dimensionResource(id = R.dimen.gutter))
            ) {
                settingsList.forEach {
                    SettingsItem(item = it,
                        onClick = { item -> onAction(SettingsAction.OnPressSettings(item)) })
                }

                TextButton(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = { onComplaintsButtonCLick() },
                ) {
                    Text(text = stringResource(id = R.string.settings_daily_exam_complaints_and_suggestions), textAlign = TextAlign.Center)
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
}


@Composable
@Preview(showBackground = true)
fun ComplaintAndSuggestionDrawerBodyPreview() {
    ComplaintAndSuggestionDrawerBody(
        settingsList = listOf(
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
            SettingsItem(
                title = stringResource(id = R.string.settings_daily_exam_item_title),
                contentDescription = stringResource(id = R.string.settings_daily_exam_cd_item_title),
                iconId = R.drawable.exam,
                navigateTo = SettingsNavigation.EXAM_REMINDER_SETTINGS
            ),
            SettingsItem(
                title = stringResource(R.string.settings_languages_title),
                contentDescription = stringResource(R.string.settings_languages_title_cd_item_title),
                iconId = R.drawable.localization,
                navigateTo = SettingsNavigation.LOCALIZATION
            ),
        ),
        onAction = {},
        onComplaintsButtonCLick = { }
    )
}