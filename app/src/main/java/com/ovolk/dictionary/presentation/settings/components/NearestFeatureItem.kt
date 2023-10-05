package com.ovolk.dictionary.presentation.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ovolk.dictionary.domain.model.nearest_feature.NearestFeature
import com.ovolk.dictionary.domain.model.nearest_feature.NearestFeatureStatus

@Composable
fun NearestFeatureItem(feature: NearestFeature) {
    val emoji = when (feature.status) {
        NearestFeatureStatus.PLANNED -> "(🤔 in plans)"
        NearestFeatureStatus.IN_PROGRESS -> "(👨‍💻 in progress)"
        NearestFeatureStatus.READY_TO_DELIVERY -> "(✅ ready to delivery)"
        NearestFeatureStatus.PAUSED -> "(⏸️ on paused)"
    }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = "- ${feature.value}", style = TextStyle(fontWeight = FontWeight.Bold))
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = emoji, style = TextStyle(fontSize = 13.sp))
    }
}

@Composable
@Preview(showBackground = true)
fun NearestFeatureItemPreview() {
    Column() {
        NearestFeatureItem(
            NearestFeature(
                value = "feature 1",
                status = NearestFeatureStatus.READY_TO_DELIVERY
            ),
        )
        NearestFeatureItem(
            NearestFeature(
                value = "feature 2",
                status = NearestFeatureStatus.IN_PROGRESS
            ),
        )
        NearestFeatureItem(
            NearestFeature(
                value = "feature 3",
                status = NearestFeatureStatus.PAUSED
            ),
        )
        NearestFeatureItem(
            NearestFeature(
                value = "feature 4",
                status = NearestFeatureStatus.PLANNED
            ),
        )
    }
}

