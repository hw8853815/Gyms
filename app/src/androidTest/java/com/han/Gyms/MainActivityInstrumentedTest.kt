package com.han.Gyms

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.ktx.auth
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainActivityInstrumentedTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        Firebase.auth.signOut()
    }

    @Test
    fun launchApp_showsLoginOrGymList() {
        composeTestRule.setContent {
            AppRoot()
        }

        val loginExists = composeTestRule.onAllNodesWithTag("page_login").fetchSemanticsNodes().isNotEmpty()
        val gymListExists = composeTestRule.onAllNodesWithTag("page_gym_list").fetchSemanticsNodes().isNotEmpty()
        assert(loginExists || gymListExists)
    }

    @Test
    fun showsLogin_whenNotLoggedIn() {
        composeTestRule.setContent {
            AppRoot()
        }
        composeTestRule.onNodeWithTag("page_login").assertExists()
    }
}
