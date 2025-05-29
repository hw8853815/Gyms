package com.han.Gyms

import org.junit.Assert.*
import org.junit.Test

class AddGymDialogUnitTest {

    @Test
    fun validInput_returnsTrue() {
        assertTrue(isValidGymInput("Gym1", "Address1", "https://img.com"))
    }

    @Test
    fun blankName_returnsFalse() {
        assertFalse(isValidGymInput("", "Address1", "https://img.com"))
    }

    @Test
    fun blankAddress_returnsFalse() {
        assertFalse(isValidGymInput("Gym1", "", "https://img.com"))
    }

    @Test
    fun blankImageUrl_returnsFalse() {
        assertFalse(isValidGymInput("Gym1", "Address1", ""))
    }
}
