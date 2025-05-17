package com.han.Gyms

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.maps.android.compose.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import java.text.SimpleDateFormat
import java.util.Locale
import com.google.firebase.analytics.ktx.analytics
import androidx.core.os.bundleOf


@Composable
fun GymDetailScreen(gymId: String, onBack: () -> Unit = {}) {
    val db = Firebase.firestore
    val context = LocalContext.current
    var gym by remember { mutableStateOf<Gym?>(null) }
    var comments by remember { mutableStateOf(listOf<Comment>()) }
    var newComment by remember { mutableStateOf("") }

    LaunchedEffect(gymId) {
        val analytics = Firebase.analytics
        analytics.logEvent(
            "gym_detail_view",
            bundleOf("gym_id" to gymId)
        )
    }

    DisposableEffect(gymId) {
        val listener = db.collection("Gyms").document(gymId)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null && snapshot.exists()) {
                    gym = snapshot.toObject(Gym::class.java)
                }
            }
        onDispose { listener.remove() }
    }

    DisposableEffect(gymId) {
        val listener = db.collection("Comments")
            .whereEqualTo("gymId", gymId)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    val list = snapshot.documents.mapNotNull { it.toObject(Comment::class.java) }
                    comments = list
                }
            }
        onDispose { listener.remove() }
    }

    Box(modifier = Modifier
        .fillMaxSize()) {

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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, top = 80.dp, bottom = 0.dp)
        ) {
            gym?.let { g ->
                Spacer(modifier = Modifier.height(16.dp))
                if (g.imageUrl.isNotBlank()) {
                    Image(
                        painter = rememberAsyncImagePainter(g.imageUrl),
                        contentDescription = g.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = g.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = g.address,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (g.location.latitude != 0.0 || g.location.longitude != 0.0) {
                    val gymLatLng = LatLng(g.location.latitude, g.location.longitude)
                    val cameraPositionState = rememberCameraPositionState {
                        position = CameraPosition.fromLatLngZoom(gymLatLng, 15f)
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    ) {
                        GoogleMap(
                            modifier = Modifier.matchParentSize(),
                            cameraPositionState = cameraPositionState
                        ) {
                            Marker(
                                state = MarkerState(position = gymLatLng),
                                title = g.name,
                                snippet = g.address
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            } ?: run {
                Text("Loading gym info...", modifier = Modifier.padding(16.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Comments", style = MaterialTheme.typography.titleMedium)
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(comments) { comment ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(8.dp)) {

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = comment.userName ?: "Anonymous",
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.weight(1f)
                                )

                                val formattedTime = comment.timestamp?.let {
                                    try {
                                        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                                        sdf.format(it.toDate())
                                    } catch (e: Exception) {
                                        ""
                                    }
                                } ?: ""
                                Text(
                                    text = formattedTime,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Text(
                                text = comment.content,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = newComment,
                    onValueChange = { newComment = it },
                    label = { Text("Add a comment") },
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = {
                        val uid = Firebase.auth.currentUser?.uid
                        if (newComment.isNotBlank() && uid != null) {
                            db.collection("Users").document(uid).get()
                                .addOnSuccessListener { doc ->
                                    val user = doc.toObject(User::class.java)
                                    val userName = user?.name ?: "Anonymous"

                                    val commentData = hashMapOf(
                                        "gymId" to gymId,
                                        "userId" to uid,
                                        "content" to newComment,
                                        "userName" to userName,
                                        "timestamp" to FieldValue.serverTimestamp()
                                    )
                                    db.collection("Comments").add(commentData)
                                    newComment = ""
                                }
                        } else {
                            Toast.makeText(context, "Please enter a comment", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("Send")
                }
            }
        }
    }
}

