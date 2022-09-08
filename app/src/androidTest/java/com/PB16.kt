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

class PB16 {

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
    fun translateText() {
        onView(isRoot()).perform(waitFor(1000));
        onView(withId(R.id.menu_translate)).perform(click())
        onView(isRoot()).perform(waitFor(1000));
        onView(withId(R.id.et_translate_source)).perform(click()).perform(typeText("Selamat Pagi"));
        onView(isRoot()).perform(waitFor(11000));
        onView(withId(R.id.et_translate)).check(matches(withText("좋은 아침")));
    }

    @Test
    fun translateImage() {
        onView(withId(R.id.menu_translate)).perform(click())
        onView(withId(R.id.img_camera)).perform(click())
        //at delay to do manual testing translate
    }





}
