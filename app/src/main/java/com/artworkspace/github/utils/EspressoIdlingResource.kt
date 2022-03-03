package com.artworkspace.github.utils

import androidx.test.espresso.IdlingResource
import androidx.test.espresso.idling.CountingIdlingResource


object EspressoIdlingResource {
    private val countingIdlingResource = CountingIdlingResource("GLOBAL")

    val idlingResource: IdlingResource
        get() = countingIdlingResource

    fun increment() = countingIdlingResource.increment()

    fun decrement() = countingIdlingResource.decrement()
}