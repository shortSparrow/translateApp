package com.ovolk.dictionary.util

import android.content.res.Resources
import android.view.View


/**
 * Do exactly the same as a lazy, but without synchronization. Designed mostly
 * to use in activity, fragments, view models etc, where more then 1 thread doesn't
 * expected.
 */
fun <T> lazySimple(initializer: () -> T): Lazy<T> = lazy(LazyThreadSafetyMode.NONE, initializer)


fun View.t(resourceId: Int) = this.context.getString(resourceId)

val Int.dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()