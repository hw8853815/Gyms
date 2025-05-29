package com.han.Gyms

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.*
import org.junit.Rule
import org.junit.Test

class MapScreenInstrumentedTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun mapScreen_shows_and_backButtonClickable() {
        composeTestRule.setContent {
            MapScreen(
                gymList = listOf(
                    Gym(
                        id = "1",
                        name = "Planet Fitness",
                        address = "Sydney",
                        imageUrl = "",
                        addedBy = "",
                        location = com.google.firebase.firestore.GeoPoint(-33.8688, 151.2093)
                    ),
                    Gym(
                        id = "2",
                        name = "Anytime Fitness",
                        address = "Melbourne",
                        imageUrl = "",
                        addedBy = "",
                        location = com.google.firebase.firestore.GeoPoint(-37.8136, 144.9631)
                    )
                ),
                onBack = {}
            )
        }
        composeTestRule.onNodeWithTag("btn_back").assertExists().performClick()
    }

    @Test
    fun mapScreen_emptyGymList_doesNotCrash() {
        composeTestRule.setContent {
            MapScreen(gymList = emptyList(), onBack = {})
        }
        composeTestRule.onNodeWithTag("btn_back").assertExists().performClick()
    }
}
