package com.r42914lg.major

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kaspersky.components.composesupport.config.withComposeSupport
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.r42914lg.major.MainActivity.Companion.INTENT_PARAM_KEY
import io.github.kakaocup.compose.node.element.ComposeScreen.Companion.onComposeScreen
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith

fun <A : ComponentActivity> ActivityScenarioRule<A>.getActivity(): A {
    var activity: A? = null
    scenario.onActivity { activity = it }
    return activity!!
}

@RunWith(AndroidJUnit4::class)
class MainScreenDeeplinkTest : TestCase(
    kaspressoBuilder = Kaspresso.Builder.withComposeSupport()
) {
    @get:Rule
    val activityRule = ActivityScenarioRule<MainActivity>(
        Intent(
            ApplicationProvider.getApplicationContext(),
            MainActivity::class.java
        ).apply {
            putExtra(INTENT_PARAM_KEY, "some-value")
        }
    )

    @get:Rule
    val composeRule = AndroidComposeTestRule(activityRule) { it.getActivity() }

    @Test
    fun mainScreenShowTest() = run {
        step("check listing is showing") {
            flakySafely(5_000) {
                onComposeScreen<ListingScreen>(composeRule) {
                    root { assertIsDisplayed() }
                    copyToClipboardView { assertIsDisplayed() }
                }
            }
        }
    }
}