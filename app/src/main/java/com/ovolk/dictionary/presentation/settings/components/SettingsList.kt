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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.ovolk.dictionary.BuildConfig
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.settings.SettingsItem
import com.ovolk.dictionary.presentation.settings.SettingsAction
import com.ovolk.dictionary.presentation.settings.SettingsState


@Composable
fun SettingsList(list: List<SettingsItem>, onAction: (SettingsAction) -> Unit) {

    Column {
        Column(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.medium_gutter))
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
                text = "version ${BuildConfig.VERSION_NAME}",
                color = colorResource(id = R.color.light_grey),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_2)
@Composable
fun SettingsListPreview() {
    SettingsList(list = SettingsState().settingsList, onAction = {})
}