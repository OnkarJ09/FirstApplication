package com.onkar.androidtest

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented UI test for the Android environment verification screen.
 *
 * It launches the real [MainActivity] and exercises the title, the counter,
 * the test button and the status text.
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun appLaunches_andShowsTitle() {
        composeTestRule.onNodeWithText(APP_TITLE).assertIsDisplayed()
    }

    @Test
    fun environmentChecks_areVisible() {
        composeTestRule.onNodeWithText("\u2713 Android application started").assertIsDisplayed()
        composeTestRule.onNodeWithText("\u2713 Kotlin compiled successfully").assertIsDisplayed()
        composeTestRule.onNodeWithText("\u2713 Jetpack Compose loaded").assertIsDisplayed()
    }

    @Test
    fun counter_startsAtZero_andStatusIsReady() {
        composeTestRule.onNodeWithTag(TestTags.COUNTER)
            .assertIsDisplayed()
            .assertTextEquals("Counter: 0")
        composeTestRule.onNodeWithTag(TestTags.STATUS)
            .assertTextEquals("Status: $STATUS_READY")
        composeTestRule.onNodeWithText(TEST_BUTTON_LABEL).assertIsDisplayed()
    }

    @Test
    fun testButton_incrementsCounter_andUpdatesStatus() {
        composeTestRule.onNodeWithTag(TestTags.TEST_BUTTON).performClick()

        composeTestRule.onNodeWithTag(TestTags.COUNTER).assertTextEquals("Counter: 1")
        composeTestRule.onNodeWithTag(TestTags.STATUS)
            .assertTextEquals("Status: $STATUS_BUTTON_WORKED")

        // A second click keeps the counter incrementing (immediate recomposition).
        composeTestRule.onNodeWithTag(TestTags.TEST_BUTTON).performClick()
        composeTestRule.onNodeWithTag(TestTags.COUNTER).assertTextEquals("Counter: 2")
    }
}
