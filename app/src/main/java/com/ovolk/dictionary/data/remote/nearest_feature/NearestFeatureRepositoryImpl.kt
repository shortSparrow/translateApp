package com.ovolk.dictionary.data.remote.nearest_feature

import com.ovolk.dictionary.BuildConfig
import com.ovolk.dictionary.data.model.getFeatureStatus
import com.ovolk.dictionary.data.remote.retrofit.firebaseRetrofitService
import com.ovolk.dictionary.domain.model.nearest_feature.NearestFeature
import com.ovolk.dictionary.domain.repositories.AppSettingsRepository
import com.ovolk.dictionary.domain.repositories.NearestFeatureRepository
import javax.inject.Inject


class NearestFeatureRepositoryImpl @Inject constructor(
    val appSettingsRepository: AppSettingsRepository
) : NearestFeatureRepository {

    override suspend fun getNearestFeature(): List<NearestFeature> {
        try {
            val response =
                firebaseRetrofitService.getNearestFeature(BuildConfig.FIRESTORE_WEB_KEY).execute()
            if (!response.isSuccessful) return emptyList()
            val values =
                response
                    .body()
                    ?.fields
                    ?.get(appSettingsRepository.getAppSettings().appLanguageCode)
                    ?.arrayValue
                    ?.values
                    ?.map {
                        NearestFeature(
                            value = it.mapValue.fields.value.stringValue,
                            status = getFeatureStatus(it.mapValue.fields.status.stringValue)
                        )
                    }
                    ?.sortedBy { it.status.value }

            return values ?: emptyList()
        } catch (e: Exception) {
            return emptyList()
        }
    }
}