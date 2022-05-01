package com.example.ttanslateapp.util

import androidx.fragment.app.Fragment
import com.example.ttanslateapp.TranslateApp
import com.example.ttanslateapp.di.ApplicationComponent

/**
 * Do exactly the same as a lazy, but without synchronization. Designed mostly
 * to use in activity, fragments, view models etc, where more then 1 thread doesn't
 * expected.
 */
fun <T> lazySimple(initializer: () -> T): Lazy<T> = lazy(LazyThreadSafetyMode.NONE, initializer)

fun Fragment.getAppComponent(): ApplicationComponent =
    (requireActivity().application as TranslateApp).component