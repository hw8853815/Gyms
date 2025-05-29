package com.han.Gyms

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.*
import org.junit.Rule
import org.junit.Test

class GymListScreenInstrumentedTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun openAndClosePostGymDialog() {
        composeTestRule.setContent {
            GymListScreen(
                onLogout = {},
                onMapClick = {},
                gymList = emptyList(),
                onGymListLoaded = {},
                onGymClick = {}
            )
        }

        composeTestRule.onNodeWithTag("btn_post_gym").performClick()
        composeTestRule.onNodeWithTag("btn_submit").assertExists()
    }

    @Test
    fun searchAndShowOnMap() {
        composeTestRule.setContent {
            GymListScreen(
                onLogout = {},
                onMapClick = {},
                gymList = listOf(
                    Gym(id = "1", name = "Planet Fitness", address = "Sydney", imageUrl = "", addedBy = "", location = com.google.firebase.firestore.GeoPoint(0.0, 0.0)),
                    Gym(id = "2", name = "Anytime", address = "Melbourne", imageUrl = "", addedBy = "", location = com.google.firebase.firestore.GeoPoint(0.0, 0.0))
                ),
                onGymListLoaded = {},
                onGymClick = {}
            )
        }

        composeTestRule.onNodeWithTag("input_search").performTextInput("Planet")
        composeTestRule.onNodeWithTag("btn_show_on_map").performClick()
    }
}
