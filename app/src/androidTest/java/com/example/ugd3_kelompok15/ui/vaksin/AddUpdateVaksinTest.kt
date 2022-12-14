package com.example.ugd3_kelompok15.ui.vaksin


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
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
class AddUpdateVaksinTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(AddUpdateVaksin::class.java)

    @Test
    fun addUpdateVaksinTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(500)

        val materialButton = onView(
            allOf(
                withId(R.id.btnSaveVaksin), withText("Save Pendaftaran Vaksin"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    11
                ),
                isDisplayed()
            )
        )
        materialButton.perform(click())

        val appCompatEditText = onView(
            allOf(
                withId(R.id.tietNamaPendaftar),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatEditText.perform(click())

        val appCompatEditText2 = onView(
            allOf(
                withId(R.id.tietNamaPendaftar),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatEditText2.perform(replaceText("Hakimi"), closeSoftKeyboard())

        val materialButton2 = onView(
            allOf(
                withId(R.id.btnSaveVaksin), withText("Save Pendaftaran Vaksin"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    11
                ),
                isDisplayed()
            )
        )
        materialButton2.perform(click())

        val appCompatEditText3 = onView(
            allOf(
                withId(R.id.tietUmur),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        appCompatEditText3.perform(replaceText("22"), closeSoftKeyboard())

        val materialButton3 = onView(
            allOf(
                withId(R.id.btnSaveVaksin), withText("Save Pendaftaran Vaksin"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    11
                ),
                isDisplayed()
            )
        )
        materialButton3.perform(click())

        val materialAutoCompleteTextView = onView(
            allOf(
                withId(R.id.ed_rsVaksin),
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
            .atPosition(4)
        materialTextView.perform(click())

        val materialButton4 = onView(
            allOf(
                withId(R.id.btnSaveVaksin), withText("Save Pendaftaran Vaksin"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    11
                ),
                isDisplayed()
            )
        )
        materialButton4.perform(click())

        val materialAutoCompleteTextView2 = onView(
            allOf(
                withId(R.id.ed_jenisVaksin),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.layout_jenisVaksin),
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
            .atPosition(1)
        materialTextView2.perform(click())

        val materialButton5 = onView(
            allOf(
                withId(R.id.btnSaveVaksin), withText("Save Pendaftaran Vaksin"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    11
                ),
                isDisplayed()
            )
        )
        materialButton5.perform(click())

        val materialButton6 = onView(
            allOf(
                withId(R.id.layoutJadwalVaksin), withText("Pilih Tanggal"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    10
                ),
                isDisplayed()
            )
        )
        materialButton6.perform(click())

        val materialButton7 = onView(
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
        materialButton7.perform(scrollTo(), click())

        val materialButton8 = onView(
            allOf(
                withId(R.id.btnSaveVaksin), withText("Save Pendaftaran Vaksin"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    11
                ),
                isDisplayed()
            )
        )
        materialButton8.perform(click())
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
