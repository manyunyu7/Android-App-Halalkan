package com

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.EspressoExtensions.Companion.waitFor
import com.feylabs.halalkan.view.ContainerActivity
import com.feylabs.halalkan.R
import org.hamcrest.Matchers.*
import org.junit.*

class PB17 {

    @Before
    fun setup() {
        ActivityScenario.launch(ContainerActivity::class.java)
        IdlingRegistry.getInstance().register(EspressoIdlingResource.getEspressoIdlingResource())
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.idlingResource)
    }

    @Test
    fun translateTextWithConvo() {
        onView(withId(R.id.menu_translate)).perform(click())
        onView(withId(R.id.btn_menu_convo)).perform(click())

        //doing manual test to input voice
        onView(isRoot()).perform(waitFor(15000))

        //check if text is translated and included to convo
        onView(withId(R.id.rv_convo)).check(RecyclerViewItemCountAssertion(greaterThan(0)))

        onView(withText("Selamat pagi")).check(matches(isDisplayed()));
        onView(withText("좋은 아침")).check(matches(isDisplayed()));
    }







}
