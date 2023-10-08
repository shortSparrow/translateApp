package com.ovolk.dictionary.presentation.settings.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.BottomDrawerValue
import androidx.compose.material.DrawerDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberSwipeableState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.nearest_feature.NearestFeature
import com.ovolk.dictionary.domain.model.settings.SettingsItem
import com.ovolk.dictionary.domain.model.settings.SettingsNavigation
import com.ovolk.dictionary.presentation.settings.SettingsAction
import com.ovolk.dictionary.util.helpers.get_preview_models.getPreviewNearestFeatureList
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SettingsListPresenter(
    settingsList: List<SettingsItem>,
    nearestFeatureList: List<NearestFeature>,
    onAction: (SettingsAction) -> Unit,
) {
    val swipeableState = rememberSwipeableState(BottomDrawerValue.Closed)
    val scope = rememberCoroutineScope()

    fun closeModal() {
        scope.launch {
            swipeableState.animateTo(BottomDrawerValue.Closed)
        }
    }

    fun onComplaintsButtonCLick() {
        scope.launch {
            swipeableState.animateTo(BottomDrawerValue.Open)

        }
    }

    if (swipeableState.currentValue == BottomDrawerValue.Open) {
        BackHandler() {
            closeModal()
        }
    }

    Box {
        SettingsList(
            settingsList = settingsList,
            onAction = onAction,
            onComplaintsButtonCLick = { onComplaintsButtonCLick() }
        )
        if (swipeableState.targetValue == BottomDrawerValue.Open || swipeableState.targetValue == BottomDrawerValue.Expanded) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(DrawerDefaults.scrimColor)
            )
        }
        ComplaintAndSuggestionDrawer(
            swipeableState = swipeableState,
            nearestFeatureList = nearestFeatureList
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsListPreview() {
    SettingsListPresenter(
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
        nearestFeatureList = getPreviewNearestFeatureList(),
        onAction = {},
    )
}