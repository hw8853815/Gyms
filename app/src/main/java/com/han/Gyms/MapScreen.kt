package com.han.Gyms

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.maps.android.compose.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack

@Composable
fun GymMapScreen(gymList: List<Gym>, onBack: () -> Unit = {}) {
    val defaultLocation = LatLng(-27.4705, 153.0260)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, 12f)
    }

    Box(modifier = Modifier.fillMaxSize()) {

        GoogleMap(
            modifier = Modifier.fillMaxSize().padding(WindowInsets.statusBars.asPaddingValues()),
            cameraPositionState = cameraPositionState
        ) {
            for (gym in gymList) {
                Marker(
                    state = MarkerState(
                        position = LatLng(gym.location.latitude, gym.location.longitude)
                    ),
                    title = gym.name,
                    snippet = gym.address
                )
            }
        }

        IconButton(
            onClick = { onBack() },
            modifier = Modifier
                .padding(top = 32.dp, start = 16.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.Black.copy(alpha = 0.7f),
                modifier = Modifier.size(36.dp)
            )
        }
    }
}