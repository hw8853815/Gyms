package com.han.Gyms

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.testTag
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.ktx.auth
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.text.input.TextFieldValue

@Composable
fun GymListScreen(
    onLogout: () -> Unit,
    onMapClick: (List<Gym>) -> Unit,
    gymList: List<Gym>,
    onGymListLoaded: (List<Gym>) -> Unit,
    onGymClick: (String) -> Unit
) {
    val db = Firebase.firestore
    var showDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

    // Load gyms from Firestore and pass back to parent
    DisposableEffect(Unit) {
        val listener = db.collection("Gyms")
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    Log.w("GymListScreen", "Listen failed.", error)
                    return@addSnapshotListener
                }
                if (snapshots != null) {
                    val gyms = snapshots?.documents?.mapNotNull { doc ->
                        val gym = doc.toObject(Gym::class.java)
                        gym?.copy(id = doc.id)
                    } ?: emptyList()
                    onGymListLoaded(gyms)
                }
            }

        onDispose {
            listener.remove()
        }
    }

    val filteredGyms = remember(searchQuery.text, gymList) {
        if (searchQuery.text.isBlank()) gymList
        else gymList.filter {
            it.name.contains(searchQuery.text, ignoreCase = true)
                    || it.address.contains(searchQuery.text, ignoreCase = true)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("page_gym_list")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { showDialog = true },
                modifier = Modifier.weight(1f).testTag("btn_post_gym")
            ) {
                Text("Post Gym")
            }

            Button(
                onClick = { onMapClick(gymList) },
                modifier = Modifier.weight(1f).testTag("btn_map_view")
            ) {
                Text("Map View")
            }

            Button(
                onClick = {
                    Firebase.auth.signOut()
                    onLogout()
                },
                modifier = Modifier.weight(1f).testTag("btn_logout"),
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
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredGyms) { gym ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onGymClick(gym.id) }
                        .testTag("gym_card_${gym.id}"),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = gym.name, style = MaterialTheme.typography.titleMedium)
                        Text(text = gym.address, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search by name or address") },
                modifier = Modifier.weight(1f).testTag("input_search"),
                singleLine = true,
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null)
                }
            )
            Button(
                onClick = { onMapClick(filteredGyms) },
                modifier = Modifier.padding(start = 8.dp).testTag("btn_show_on_map")
            ) {
                Text("Show On Map")
            }
        }
    }

    if (showDialog) {
        AddGymDialog(
            onDismiss = { showDialog = false },
            onSuccess = {
                showDialog = false
            },
            uid = Firebase.auth.currentUser?.uid ?: ""
        )
    }
}
