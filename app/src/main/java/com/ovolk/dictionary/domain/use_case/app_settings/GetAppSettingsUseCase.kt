package com.ovolk.dictionary.domain.use_case.app_settings

import android.app.Application
import javax.inject.Inject

class GetAppSettingsUseCase @Inject constructor(
    private val application: Application,
)