package com.han.Gyms

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import com.han.Gyms.ui.theme.GymsTheme
import androidx.compose.runtime.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GymsTheme {
                Firebase.auth.signOut()
                AppRoot()
            }
        }
    }
}

@Composable
fun AppRoot() {
    val auth = Firebase.auth
    var isLoggedIn by remember { mutableStateOf(auth.currentUser != null) }

    if (isLoggedIn) {
        GymListScreen(onLogout = {
            isLoggedIn = false
        })
    } else {
        LoginScreen(onLoginSuccess = {
            isLoggedIn = true
        })
    }
}