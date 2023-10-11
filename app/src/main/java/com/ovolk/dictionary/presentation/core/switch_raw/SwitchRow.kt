package com.ovolk.dictionary.presentation.core.switch_raw

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ovolk.dictionary.R


@Composable
fun SwitchRow(
    title: String,
    description: String,
    isEnable: Boolean,
    onCheckedChange: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 20.dp, bottom = 20.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title)
            Text(
                text = description, fontSize = 10.sp,
                color = colorResource(id = R.color.light_grey),
            )
        }

        Switch(
            checked = isEnable,
            onCheckedChange = { onCheckedChange() },
            modifier = Modifier.padding(start = 5.dp),
            colors = SwitchDefaults.colors(
                checkedThumbColor = colorResource(id = R.color.blue),
                checkedTrackColor = colorResource(id = R.color.blue),
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SwitchRowPreview() {
    SwitchRow(
        title = "title",
        description = "Some text witch explains why this switch need",
        isEnable = true,
        onCheckedChange = {},
    )
}

@Preview(showBackground = true)
@Composable
fun SwitchRowPreview2() {
    SwitchRow(
        title = "title",
        description = "Some text witch explains why this switch need. Some text witch explains why this switch need. Some text witch explains why this switch need. Some text witch explains why this switch need",
        isEnable = true,
        onCheckedChange = {},
    )
}