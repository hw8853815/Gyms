package com.han.Gyms

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.ktx.auth
import android.util.Log

@Composable
fun GymListScreen(
    onLogout: () -> Unit,
    onMapClick: () -> Unit,
    gymList: List<Gym>,
    onGymListLoaded: (List<Gym>) -> Unit
) {
    val db = Firebase.firestore
    var showDialog by remember { mutableStateOf(false) }

    // Load gyms from Firestore and pass back to parent
    DisposableEffect(Unit) {
        val listener = db.collection("Gyms")
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    Log.w("GymListScreen", "Listen failed.", error)
                    return@addSnapshotListener
                }
                if (snapshots != null) {
                    val gyms = snapshots.mapNotNull { it.toObject(Gym::class.java) }
                    onGymListLoaded(gyms)
                }
            }

        onDispose {
            listener.remove()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { showDialog = true },
                modifier = Modifier.weight(1f)
            ) {
                Text("Add Gym")
            }

            Button(
                onClick = { onMapClick() },
                modifier = Modifier.weight(1f)
            ) {
                Text("Map View")
            }

            Button(
                onClick = {
                    Firebase.auth.signOut()
                    onLogout()
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Logout")
            }
        }

        Text(
            text = "Available Gyms",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(gymList) { gym ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = gym.name, style = MaterialTheme.typography.titleMedium)
                        Text(text = gym.address, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }

    if (showDialog) {
        AddGymDialog(
            onDismiss = { showDialog = false },
            onSuccess = {
                showDialog = false
            }
        )
    }
}
