package com

import android.preference.PreferenceManager
import androidx.test.InstrumentationRegistry
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.feylabs.halalkan.view.ContainerActivity
import com.feylabs.halalkan.R
import org.junit.*

class RegisterTest {


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
    fun registerWithAccount() {
        onView(withId(R.id.btn_menu_profile)).perform(click())

        onView(withId(R.id.btn_register)).perform(click())
        onView(withId(R.id.et_name)).perform(click()).perform(typeText("blackbox"));
        onView(withId(R.id.et_username)).perform(click()).perform(typeText("blackbox@gmail.com"));
        onView(withId(R.id.et_phone)).perform(click()).perform(typeText("6289668478593"));
        onView(withId(R.id.et_password)).perform(click()).perform(typeText("password"));

        onView(withId(R.id.btn_register)).perform(click())
    }





}
