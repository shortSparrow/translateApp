package com.ovolk.dictionary.presentation.core.snackbar

open class SnackBarType(
    val message: String,
    val action: (() -> Unit)? = null,
    val actionTitle: String? = null
)

class SnackBarError(message: String, action: (() -> Unit)? = null, actionTitle: String? = null) :
    SnackBarType(
        message = message,
        action = action,
        actionTitle = actionTitle,
    )

class SnackBarSuccess(message: String, action: (() -> Unit)? = null, actionTitle: String? = null) :
    SnackBarType(
        message = message,
        action = action,
        actionTitle = actionTitle,
    )

class SnackBarInfo(message: String, action: (() -> Unit)? = null, actionTitle: String? = null) :
    SnackBarType(
        message = message,
        action = action,
        actionTitle = actionTitle,
    )

class SnackBarAlert(message: String, action: (() -> Unit)? = null, actionTitle: String? = null) :
    SnackBarType(
        message = message,
        action = action,
        actionTitle = actionTitle,
    )
