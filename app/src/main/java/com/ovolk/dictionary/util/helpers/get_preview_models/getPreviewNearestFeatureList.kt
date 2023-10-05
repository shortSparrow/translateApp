package com.ovolk.dictionary.util.helpers.get_preview_models

import com.ovolk.dictionary.domain.model.nearest_feature.NearestFeature
import com.ovolk.dictionary.domain.model.nearest_feature.NearestFeatureStatus

fun getPreviewNearestFeatureList() = listOf(
    NearestFeature(
        value = "1",
        status = NearestFeatureStatus.READY_TO_DELIVERY
    ),
    NearestFeature(
        value = "2",
        status = NearestFeatureStatus.IN_PROGRESS
    ),
    NearestFeature(
        value = "3",
        status = NearestFeatureStatus.PAUSED
    ),
    NearestFeature(
        value = "4",
        status = NearestFeatureStatus.PLANNED
    ),
)