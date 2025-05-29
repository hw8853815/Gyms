package com.han.Gyms

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.*
import org.junit.Rule
import org.junit.Test

class GymDetailScreenInstrumentedTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun sendComment_withInput_doesNotCrash() {
        composeTestRule.setContent {
            GymDetailScreen(gymId = "fake_id", onBack = {})
        }

        composeTestRule.onNodeWithTag("input_comment").performTextInput("This is a test comment.")

        composeTestRule.onNodeWithTag("btn_send").performClick()
    }

    @Test
    fun sendComment_withBlankInput_doesNotCrash() {
        composeTestRule.setContent {
            GymDetailScreen(gymId = "fake_id", onBack = {})
        }

        composeTestRule.onNodeWithTag("btn_send").performClick()
    }
}
