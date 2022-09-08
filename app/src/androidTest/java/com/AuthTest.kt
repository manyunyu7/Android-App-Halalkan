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

class AuthTest {

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
    fun openLoginPage() {
        onView(withId(R.id.btn_menu_profile)).perform(click())
        onView(withId(R.id.et_username)).check(matches(isDisplayed()))
        onView(withId(R.id.et_password)).check(matches(isDisplayed()))
    }

    @Test
    fun inputWrongCredential() {
        onView(withId(R.id.btn_menu_profile)).perform(click())
        onView(withId(R.id.et_username)).check(matches(isDisplayed()))
        onView(withId(R.id.et_password)).check(matches(isDisplayed()))

        onView(withId(R.id.et_username)).perform(click()).perform(typeText("email@gmail.com"));
        onView(withId(R.id.et_password)).perform(click()).perform(typeText("password"));

        onView(withId(R.id.btn_login)).perform(click())

        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText("email or password incorrect")))
    }

    @Test
    fun inputRightCredential() {
        onView(withId(R.id.btn_menu_profile)).perform(click())
        onView(withId(R.id.et_username)).check(matches(isDisplayed()))
        onView(withId(R.id.et_password)).check(matches(isDisplayed()))


        onView(withId(R.id.et_username)).perform(ViewActions.replaceText("yossy@gmail.com"))
        onView(withId(R.id.et_password)).perform(ViewActions.replaceText("password.com"))

        onView(withId(R.id.btn_login)).perform(click())


    }





}
