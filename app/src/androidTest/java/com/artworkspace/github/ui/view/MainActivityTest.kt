package com.artworkspace.github.ui.view

import android.content.res.Resources
import android.view.KeyEvent
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.artworkspace.github.R
import com.artworkspace.github.adapter.ListUserAdapter
import com.artworkspace.github.utils.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {
    @Before
    fun setup() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.idlingResource)
        ActivityScenario.launch(MainActivity::class.java)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.idlingResource)
    }

    @Test
    fun testComponentShowCorrectly() {
        onView(withId(R.id.toolbar_home)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_result_count)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_users)).check(matches(isDisplayed()))
    }

    @Test
    fun testSelectFirstUser() {
        onView(withId(R.id.rv_users)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_users)).perform(
            actionOnItemAtPosition<ListUserAdapter.ListViewHolder>(
                0,
                click()
            )
        )

        onView(withId(R.id.user_detail_container)).check(matches(isDisplayed()))
        onView(withId(R.id.tabs)).check(matches(isDisplayed()))
        onView(withId(R.id.fab_favorite)).check(matches(isDisplayed()))
    }

    @Test
    fun testAddUserToFavorite() {
        onView(withId(R.id.rv_users)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_users)).perform(
            actionOnItemAtPosition<ListUserAdapter.ListViewHolder>(
                0,
                click()
            )
        )

        onView(withId(R.id.user_detail_container)).check(matches(isDisplayed()))
        onView(withId(R.id.tabs)).check(matches(isDisplayed()))
        onView(withId(R.id.fab_favorite)).check(matches(isDisplayed()))
        onView(withId(R.id.fab_favorite)).perform(click())
        pressBack()

        onView(withId(R.id.favorite)).perform(click())
        onView(withId(R.id.rv_favorite)).check(matches(isDisplayed()))
    }

    @Test
    fun testDeleteUserFromFavorite() {
        onView(withId(R.id.favorite)).check(matches(isDisplayed()))
        onView(withId(R.id.favorite)).perform(click())

        onView(withId(R.id.rv_favorite)).perform(
            actionOnItemAtPosition<ListUserAdapter.ListViewHolder>(0, click())
        )

        onView(withId(R.id.user_detail_container)).check(matches(isDisplayed()))
        onView(withId(R.id.tabs)).check(matches(isDisplayed()))
        onView(withId(R.id.fab_favorite)).check(matches(isDisplayed()))
        onView(withId(R.id.fab_favorite)).perform(click())
        pressBack()
    }

    @Test
    fun testSearchUserFound() {
        onView(withId(R.id.search)).check(matches(isDisplayed()))
        onView(withId(R.id.search)).perform(click())

        onView(
            withId(androidx.appcompat.R.id.search_src_text)
        ).perform(
            clearText(), typeText("fikriyusrihan")
        ).perform(pressKey(KeyEvent.KEYCODE_ENTER))

        onView(withId(R.id.rv_users)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_users)).perform(
            actionOnItemAtPosition<ListUserAdapter.ListViewHolder>(
                0,
                click()
            )
        )

        onView(withId(R.id.user_detail_container)).check(matches(isDisplayed()))
        onView(withId(R.id.tabs)).check(matches(isDisplayed()))
        onView(withId(R.id.fab_favorite)).check(matches(isDisplayed()))
    }
}