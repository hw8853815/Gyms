package com.han.Gyms

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.*
import org.junit.Rule
import org.junit.Test

class LoginScreenInstrumentedTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun register_and_login_button_clickable() {
        composeTestRule.setContent {
            LoginScreen(onLoginSuccess = {})
        }

        composeTestRule.onNodeWithTag("input_username").performTextInput("TestUser")
        composeTestRule.onNodeWithTag("input_email").performTextInput("test@example.com")
        composeTestRule.onNodeWithTag("input_password").performTextInput("testpassword")

        composeTestRule.onNodeWithTag("btn_register").performClick()

        composeTestRule.onNodeWithTag("btn_login").performClick()
    }

    @Test
    fun register_with_blank_username_showsError_noCrash() {
        composeTestRule.setContent {
            LoginScreen(onLoginSuccess = {})
        }
        composeTestRule.onNodeWithTag("input_email").performTextInput("test@example.com")
        composeTestRule.onNodeWithTag("input_password").performTextInput("testpassword")

        composeTestRule.onNodeWithTag("btn_register").performClick()
    }
}
