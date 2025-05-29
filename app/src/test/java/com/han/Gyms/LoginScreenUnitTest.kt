package com.han.Gyms

import org.junit.Assert.*
import org.junit.Test

class LoginScreenUnitTest {

    @Test
    fun testRegisterInput() {
        assertTrue(isValidRegisterInput("User", "a@b.com", "pw"))
        assertFalse(isValidRegisterInput("", "a@b.com", "pw"))
        assertFalse(isValidRegisterInput("User", "", "pw"))
        assertFalse(isValidRegisterInput("User", "a@b.com", ""))
    }

    @Test
    fun testLoginInput() {
        assertTrue(isValidLoginInput("a@b.com", "pw"))
        assertFalse(isValidLoginInput("", "pw"))
        assertFalse(isValidLoginInput("a@b.com", ""))
    }
}
