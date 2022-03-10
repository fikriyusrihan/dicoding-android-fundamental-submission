package com.artworkspace.github.ui.view

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.artworkspace.github.R
import com.artworkspace.github.adapter.ListUserAdapter
import com.artworkspace.github.utils.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Test

class FavoriteActivityTest {

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.idlingResource)
        ActivityScenario.launch(FavoriteActivity::class.java)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.idlingResource)
    }

    @Test
    fun testDeleteAllFavoriteUser() {
        onView(withId(R.id.rv_favorite)).check(matches(isDisplayed()))

        while (true) {
            try {
                onView(withId(R.id.rv_favorite)).perform(
                    actionOnItemAtPosition<ListUserAdapter.ListViewHolder>(0, click())
                )

                onView(withId(R.id.user_detail_container)).check(matches(isDisplayed()))
                onView(withId(R.id.tabs)).check(matches(isDisplayed()))
                onView(withId(R.id.fab_favorite)).check(matches(isDisplayed()))

                onView(withId(R.id.fab_favorite)).perform(click())
                pressBack()
            } catch (e: Exception) {
                break
            }
        }

        onView(withId(R.id.tv_message)).check(matches(isDisplayed()))
    }
}