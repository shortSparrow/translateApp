package com.ovolk.dictionary.data.model

import com.ovolk.dictionary.domain.model.nearest_feature.NearestFeatureStatus

fun getFeatureStatus(value: String): NearestFeatureStatus {
    if (value == "progress") return NearestFeatureStatus.IN_PROGRESS
    if (value == "paused") return NearestFeatureStatus.PAUSED
    if (value == "ready") return NearestFeatureStatus.READY_TO_DELIVERY
    if (value == "planned") return NearestFeatureStatus.PLANNED
    return NearestFeatureStatus.PLANNED
}


data class NearestFeatureFirestore(
    val name: String,
    val fields: Map<String, FirestoreArrayValue>,
    val createTime: String,
    val updateTime: String
) {
    data class FirestoreArrayValue(
        val arrayValue: FirestoreArrayValues
    )

    data class FirestoreArrayValues(
        val values: List<FirestoreMapValue>
    )

    data class FirestoreMapValue(
        val mapValue: FirestoreInnerFields
    )

    data class FirestoreInnerFields(
        val fields: FirestoreValues
    )

    data class FirestoreValues(
        val value: FirestoreStringValue,
        val status: FirestoreStringValue
    )

    data class FirestoreStringValue(
        val stringValue: String
    )
}
