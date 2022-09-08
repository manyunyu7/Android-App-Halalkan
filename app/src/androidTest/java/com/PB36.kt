package com

import android.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.InstrumentationRegistry
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.EspressoExtensions.Companion.waitFor
import com.feylabs.halalkan.view.ContainerActivity
import com.feylabs.halalkan.R
import org.hamcrest.Matchers.*
import org.junit.*

class PB36 {

    companion object {
        @BeforeClass
        fun clearDatabase() {
            InstrumentationRegistry.getInstrumentation().uiAutomation.executeShellCommand("pm clear PACKAGE_NAME")
                .close()
        }
    }

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
    fun deleteDriver() {
        onView(isRoot()).perform(waitFor(20000));
        onView(withId(R.id.rv_resto_nearby)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())
        )

        onView(withId(R.id.menu_driver)).perform(click())
        onView(isRoot()).perform(waitFor(20000));

        onView(withId(R.id.rv)).check(RecyclerViewItemCountAssertion(greaterThan(0)))
        onView(withId(R.id.rv)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())
        )

        onView(withId(R.id.btn_delete_driver)).perform(click())
        onView(isRoot()).perform(waitFor(15000));
    }


}
