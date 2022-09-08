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

class PB19 {

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
    fun openRestoList_then_detail_and_photo() {
        onView(withId(R.id.menu_resto)).perform(click())
        onView(withId(R.id.label_nearest_restaurant)).perform(click())
        onView(isRoot()).perform(waitFor(10000))
        onView(withId(R.id.rv_list)).check(RecyclerViewItemCountAssertion(greaterThan(3)))

        onView(withId(R.id.rv_list)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())
        )

        onView(withId(R.id.label_name)).check(matches(withText("Bakmi GM")));
        onView(withId(R.id.btn_see_all_photos)).perform(click())
        onView(isRoot()).perform(waitFor(1000))
        onView(withId(R.id.rv)).check(RecyclerViewItemCountAssertion(greaterThan(0)))
    }


    @Test
    fun openRestoDirection() {
        onView(withId(R.id.menu_resto)).perform(click())
        onView(withId(R.id.label_nearest_restaurant)).perform(click())
        onView(isRoot()).perform(waitFor(100))

        onView(withId(R.id.rv_list)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())
        )
        onView(isRoot()).perform(closeSoftKeyboard())

        onView(withId(R.id.btn_maps)).perform(click())
    }

    @Test
    fun seeAndCreateRestoReviews() {
        onView(withId(R.id.menu_resto)).perform(click())
        onView(withId(R.id.label_nearest_restaurant)).perform(click())

        onView(isRoot()).perform(waitFor(5000))
        onView(withId(R.id.rv_list)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())
        )
        onView(isRoot()).perform(closeSoftKeyboard())

        onView(withId(R.id.btn_write_review)).perform(click())
        onView(withId(R.id.btn_write_review)).perform(click())

        //TIME TO INPUT RATING AND PICK PHOTO manually
        onView(isRoot()).perform(waitFor(20000))

        onView(
            allOf(
                isDescendantOfA(withId(R.id.et_deskripsi)),
                withClassName(endsWith("EditText"))
            )
        ).perform(
            typeText("Tes Review FE")
        )
        onView(isRoot()).perform(closeSoftKeyboard())
        onView(withId(R.id.btn_publish_review)).perform(click())
    }


}
