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

class UbahDataDiriTest {

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
    fun editCredential() {
        onView(withId(R.id.btn_menu_profile)).perform(click())
        onView(withId(R.id.btn_edit)).perform(click())


        onView(withId(R.id.et_name)).perform(click())
            .perform(replaceText("Nama Black Box"), closeSoftKeyboard());

        onView(withId(R.id.btn_save)).perform(click())
        onView(isRoot()).perform(waitFor(5000));
        onView(withText("Success")).check(matches(isDisplayed()));
    }


}
