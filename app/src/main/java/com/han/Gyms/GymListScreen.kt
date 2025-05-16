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
fun GymListScreen(onLogout: () -> Unit = {}) {
    val db = Firebase.firestore
    var gymList by remember { mutableStateOf(listOf<Gym>()) }
    var showDialog by remember { mutableStateOf(false) }

    // Load gyms from Firestore
    DisposableEffect(Unit) {
        val listener = db.collection("Gyms")
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    Log.w("GymListScreen", "Listen failed.", error)
                    return@addSnapshotListener
                }
                if (snapshots != null) {
                    val gyms = snapshots.mapNotNull { it.toObject(Gym::class.java) }
                    gymList = gyms
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
        Button(
            onClick = {
                Firebase.auth.signOut()
                onLogout()
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Logout")
        }

        Button(onClick = { showDialog = true }) {
            Text("Add Gym")
        }

        Text(
            text = "Available Gyms",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
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
