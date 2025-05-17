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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GymsTheme {
                AppRoot()
            }
        }
    }
}

@Composable
fun AppRoot() {
    val navController: NavHostController = rememberNavController()
    val auth = Firebase.auth
    var isLoggedIn by remember { mutableStateOf(auth.currentUser != null) }
    var gymList by remember { mutableStateOf(listOf<Gym>()) }
    var gymsForMap by remember { mutableStateOf(listOf<Gym>()) }

    if (!isLoggedIn) {
        LoginScreen(onLoginSuccess = { isLoggedIn = true })
    } else {
        NavHost(navController = navController, startDestination = "gymList") {
            composable("gymList") {
                GymListScreen(
                    onLogout = {
                        Firebase.auth.signOut()
                        isLoggedIn = false
                    },
                    onMapClick = { gymsToShow ->
                        gymsForMap = gymsToShow
                        navController.navigate("map")
                    },
                    onGymClick = { gymId -> navController.navigate("gymDetail/$gymId") },
                    gymList = gymList,
                    onGymListLoaded = { gymList = it }
                )
            }
            composable("map") {
                GymMapScreen(
                    gymList = gymsForMap,
                    onBack = { navController.popBackStack() }
                )
            }
            composable("gymDetail/{gymId}") { backStackEntry ->
                val gymId = backStackEntry.arguments?.getString("gymId") ?: return@composable
                GymDetailScreen(
                    gymId = gymId,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}