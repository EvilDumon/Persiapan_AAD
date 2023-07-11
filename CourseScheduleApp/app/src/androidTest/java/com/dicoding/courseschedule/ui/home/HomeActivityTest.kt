package com.dicoding.courseschedule.ui.home

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import org.junit.Assert.*
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.ui.add.AddCourseActivity
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
@RunWith(AndroidJUnit4::class)
class TaskActivityTest {
    @get:Rule
    var mActivityRule: ActivityTestRule<HomeActivity> = ActivityTestRule(HomeActivity::class.java)

    companion object {
        @BeforeClass
        @JvmStatic
        fun setup(){
            ActivityScenario.launch(HomeActivity::class.java)
        }
    }

    @Test
    fun clickPlusWillDisplayAddCourseActivity(){
        Intents.init()
        onView(withId(R.id.action_add)).perform(click())
        Intents.intended(hasComponent(AddCourseActivity::class.java.name))
        onView(withId(R.id.ed_course_name)).check(matches(isDisplayed()))
    }
}