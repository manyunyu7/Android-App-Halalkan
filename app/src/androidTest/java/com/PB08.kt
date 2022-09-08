package com

import android.preference.PreferenceManager
import androidx.test.InstrumentationRegistry
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.EspressoExtensions.Companion.waitFor
import com.feylabs.halalkan.view.ContainerActivity
import com.feylabs.halalkan.R
import org.junit.*

class PB08 {

    companion object {
        @BeforeClass
        fun clearDatabase() {
            InstrumentationRegistry.getInstrumentation().uiAutomation.executeShellCommand("pm clear PACKAGE_NAME")
                .close()
        }
    }

    @get:Rule
    val scenarioRule = ActivityScenarioRule(ContainerActivity::class.java)

    @Before
    fun setup() {
        ActivityScenario.launch(ContainerActivity::class.java)
        IdlingRegistry.getInstance().register(EspressoIdlingResource.getEspressoIdlingResource())
    }

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = sharedPrefs.edit()
        editor.clear()
        editor.apply()
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.idlingResource)
    }

    @Test
    fun openPrayerTimeAndSeeTime() {
        onView(withId(R.id.menu_prayer)).perform(click())
        //doing scanning manually with code 12021842564
        onView(isRoot()).perform(waitFor(1000));
        onView(withId(R.id.menu_prayer_time)).perform(click())
        onView(isRoot()).perform(waitFor(1000));


        onView(withId(R.id.btn_before_date)).perform(click())
        onView(withId(R.id.btn_next_date)).perform(click())

        onView(withId(R.id.tv_fajr_time)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_zuhr_time)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_asr_time)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_maghrib_time)).check(matches(isDisplayed()))

    }


}
