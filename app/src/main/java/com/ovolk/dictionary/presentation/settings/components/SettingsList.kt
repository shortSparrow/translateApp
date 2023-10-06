package com.ovolk.dictionary.presentation.settings.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomDrawer
import androidx.compose.material.BottomDrawerValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.nearest_feature.NearestFeature
import com.ovolk.dictionary.domain.model.settings.SettingsItem
import com.ovolk.dictionary.domain.model.settings.SettingsNavigation
import com.ovolk.dictionary.presentation.settings.SettingsAction
import com.ovolk.dictionary.util.helpers.get_preview_models.getPreviewNearestFeatureList
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SettingsList(
    settingsList: List<SettingsItem>,
    nearestFeatureList: List<NearestFeature>,
    onAction: (SettingsAction) -> Unit,
) {
    val drawerState = rememberBottomDrawerState(BottomDrawerValue.Closed)
    val scope = rememberCoroutineScope()

    fun closeModal() {
        scope.launch {
            drawerState.close()
        }
    }

    fun onComplaintsButtonCLick() {
        scope.launch { drawerState.expand() }
    }

    if (drawerState.isOpen) {
        BackHandler() {
            closeModal()
        }
    }

    // close bottomSheet on Open state (available only Closed and Expanded)
    LaunchedEffect(key1 = drawerState.isExpanded) {
        if(!drawerState.isExpanded && !drawerState.isClosed) {
            closeModal()
        }
    }

    BottomDrawer(
        drawerState = drawerState,
        drawerShape = RoundedCornerShape(
            bottomStart = 0.dp,
            bottomEnd = 0.dp,
            topStart = 20.dp,
            topEnd = 20.dp,
        ),
        gesturesEnabled = false,
        drawerContent = {
            ComplaintAndSuggestionDrawer(
                nearestFeatureList = nearestFeatureList,
                drawerState = drawerState
            )
        }
    ) {
        ComplaintAndSuggestionDrawerBody(
            settingsList = settingsList,
            onAction = onAction,
            onComplaintsButtonCLick = { onComplaintsButtonCLick() }
        )
    }

}

@Preview(showBackground = true)
@Composable
fun SettingsListPreview() {
    SettingsList(
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