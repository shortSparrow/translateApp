package com.ovolk.dictionary.domain.repositories

import com.ovolk.dictionary.domain.model.nearest_feature.NearestFeature


interface NearestFeatureRepository {
    suspend fun getNearestFeature(): List<NearestFeature>
}