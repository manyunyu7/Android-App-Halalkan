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

class PB28 {

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
    fun addNewResto() {
        onView(isRoot()).perform(waitFor(11000));
        onView(withId(R.id.btn_add)).perform(click())
        onView(withId(R.id.et_name)).perform(click())
            .perform(replaceText("Nama Black Box"), closeSoftKeyboard());

        onView(withId(R.id.et_desc)).perform(click())
            .perform(replaceText("Deskripsi Restoran Blackbox"), closeSoftKeyboard());

        onView(withId(R.id.et_address)).perform(click())
            .perform(replaceText("Alamat Restoran Blackbox"), closeSoftKeyboard());

        onView(withId(R.id.et_phone)).perform(click())
            .perform(replaceText("089665774543"), closeSoftKeyboard());

        //pick photo
        onView(isRoot()).perform(waitFor(15000));


        onView(withId(R.id.btn_register)).perform(click())
    }

    @Test
    fun cancelOrder() {

    }


}
