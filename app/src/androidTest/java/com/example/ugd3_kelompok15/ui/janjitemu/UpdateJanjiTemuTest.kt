package com.example.ugd3_kelompok15.ui.janjitemu


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.RootMatchers.isPlatformPopup
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.ugd3_kelompok15.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.*
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class UpdateJanjiTemuTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(UpdateJanjiTemu::class.java)

    @Test
    fun updateJanjiTemuTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(500)

        val materialButton = onView(
            allOf(
                withId(R.id.btnSave), withText("Save Janji Temu"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    9
                ),
                isDisplayed()
            )
        )
        materialButton.perform(click())

        val materialAutoCompleteTextView = onView(
            allOf(
                withId(R.id.ed_rs),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.layout_rs),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        materialAutoCompleteTextView.perform(click())

        val materialTextView = onData(anything())
            .inRoot(isPlatformPopup())
            .atPosition(0)
        materialTextView.perform(click())

        val materialButton2 = onView(
            allOf(
                withId(R.id.btnSave), withText("Save Janji Temu"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    9
                ),
                isDisplayed()
            )
        )
        materialButton2.perform(click())

        val materialAutoCompleteTextView2 = onView(
            allOf(
                withId(R.id.ed_dr),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.layout_dr),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        materialAutoCompleteTextView2.perform(click())

        val materialTextView2 = onData(anything())
            .inRoot(isPlatformPopup())
            .atPosition(0)
        materialTextView2.perform(click())

        val materialButton3 = onView(
            allOf(
                withId(R.id.btnSave), withText("Save Janji Temu"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    9
                ),
                isDisplayed()
            )
        )
        materialButton3.perform(click())

        val materialButton4 = onView(
            allOf(
                withId(R.id.layoutdate), withText("Pilih Tanggal"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    6
                ),
                isDisplayed()
            )
        )
        materialButton4.perform(click())

        val materialButton5 = onView(
            allOf(
                withId(android.R.id.button1), withText("OK"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    3
                )
            )
        )
        materialButton5.perform(scrollTo(), click())

        val materialButton6 = onView(
            allOf(
                withId(R.id.btnSave), withText("Save Janji Temu"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    9
                ),
                isDisplayed()
            )
        )
        materialButton6.perform(click())

        val appCompatEditText = onView(
            allOf(
                withId(R.id.tietkeluhan),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    8
                ),
                isDisplayed()
            )
        )
        appCompatEditText.perform(replaceText("batuk"), closeSoftKeyboard())

        val materialButton7 = onView(
            allOf(
                withId(R.id.btnSave), withText("Save Janji Temu"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    9
                ),
                isDisplayed()
            )
        )
        materialButton7.perform(click())
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
