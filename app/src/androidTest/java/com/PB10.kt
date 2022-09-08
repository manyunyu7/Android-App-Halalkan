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
import org.hamcrest.Matchers.greaterThan
import org.junit.*

class PB10 {

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
    fun openMosqueList_then_detail_and_photo() {
        onView(withId(R.id.menu_prayer)).perform(click())
        onView(withId(R.id.btn_see_all_masjid)).perform(click())
        onView(isRoot()).perform(waitFor(10000))
        onView(withId(R.id.rv_list)).check(RecyclerViewItemCountAssertion(greaterThan(3)))

        onView(withId(R.id.rv_list)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())
        )

        onView(withId(R.id.label_name)).check(matches(withText("Masjid Jami Al-Ukhuwah")));
        onView(withId(R.id.et_category_top)).check(matches(withText("University")));
        onView(withId(R.id.btn_see_all_photos)).perform(click())

        onView(withId(R.id.rv)).check(RecyclerViewItemCountAssertion(greaterThan(3)))
    }


    @Test
    fun openMosqueDirection() {
        onView(withId(R.id.menu_prayer)).perform(click())
        onView(withId(R.id.btn_see_all_masjid)).perform(click())
        onView(isRoot()).perform(waitFor(100))

        onView(withId(R.id.rv_list)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())
        )

        onView(withId(R.id.btn_maps)).perform(click())
    }


}
