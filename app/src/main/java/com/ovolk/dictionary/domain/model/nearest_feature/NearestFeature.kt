package com.ovolk.dictionary.domain.model.nearest_feature

enum class NearestFeatureStatus(val value: Int) {  READY_TO_DELIVERY(0), IN_PROGRESS(1), PLANNED(2), PAUSED(3) }


data class NearestFeature(
    val value: String,
    val status: NearestFeatureStatus
)
