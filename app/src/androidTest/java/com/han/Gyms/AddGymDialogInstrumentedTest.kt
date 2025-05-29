package com.han.Gyms

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.*
import org.junit.Rule
import org.junit.Test

class AddGymDialogInstrumentedTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun submit_withValidInput_doesNotCrash() {
        composeTestRule.setContent {
            AddGymDialog(onDismiss = {}, onSuccess = {}, uid = "test_uid")
        }

        composeTestRule.onNodeWithTag("input_name").performTextInput("Test Gym")
        composeTestRule.onNodeWithTag("input_address").performTextInput("Sydney, Australia")
        composeTestRule.onNodeWithTag("input_imageUrl").performTextInput("https://test.com/img.jpg")

        composeTestRule.onNodeWithTag("btn_submit").performClick()
    }

    @Test
    fun submit_withBlankFields_doesNotCrash() {
        composeTestRule.setContent {
            AddGymDialog(onDismiss = {}, onSuccess = {}, uid = "test_uid")
        }
        composeTestRule.onNodeWithTag("btn_submit").performClick()
    }
}
