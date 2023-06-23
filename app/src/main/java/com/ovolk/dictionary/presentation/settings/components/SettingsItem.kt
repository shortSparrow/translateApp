package com.ovolk.dictionary.presentation.settings.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ovolk.dictionary.R
import com.ovolk.dictionary.domain.model.settings.SettingsItem
import com.ovolk.dictionary.domain.model.settings.SettingsNavigation

@Composable
fun SettingsItem(item: SettingsItem, onClick: (item: SettingsItem) -> Unit) {
    Surface(
        border = BorderStroke(1.dp, color = colorResource(id = R.color.light_grey)), // or maybe blue_2
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = dimensionResource(id = R.dimen.gutter))
            .clickable { onClick(item) }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(dimensionResource(id = R.dimen.gutter))
        ) {
            Icon(
                painter = painterResource(id = item.iconId),
                contentDescription = item.contentDescription
            )
            Text(
                text = item.title.uppercase(),
                modifier = Modifier.padding(start = dimensionResource(id = R.dimen.gutter)),
                fontWeight = FontWeight.Bold
            )
        }

    }
}

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_2)
@Composable
fun SettingsItemPreview() {
    Column {
        SettingsItem(
            item = SettingsItem(
                "language",
                "language settings",
                R.drawable.language,
                navigateTo = SettingsNavigation.DICTIONARY_LIST
            ),
            onClick = {}
        )
    }
}