package com.r42914lg.major

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kaspersky.components.composesupport.config.withComposeSupport
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import io.github.kakaocup.compose.node.element.ComposeScreen.Companion.onComposeScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainScreenTest : TestCase(
    kaspressoBuilder = Kaspresso.Builder.withComposeSupport()
) {
    @get:Rule
    val composeRuleDirect = createAndroidComposeRule<MainActivity>()

    @Test
    fun mainScreenShowTest() = run {
        step("check listing is showing") {
            flakySafely(5_000) {
                onComposeScreen<ListingScreen>(composeRuleDirect) {
                    root { assertIsDisplayed() }
                    copyToClipboardView { assertIsDisplayed() }
                }
            }
        }
    }
}