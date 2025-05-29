package com.han.Gyms

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.window.Dialog
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import kotlinx.coroutines.launch
import android.location.Geocoder
import java.util.Locale

fun isValidGymInput(name: String, address: String, imageUrl: String): Boolean {
    return name.isNotBlank() && address.isNotBlank() && imageUrl.isNotBlank()
}

@Composable
fun AddGymDialog(onDismiss: () -> Unit, onSuccess: () -> Unit, uid: String) {
    val db = Firebase.firestore
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var name by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(shape = MaterialTheme.shapes.medium) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Add New Gym", style = MaterialTheme.typography.titleMedium)

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).testTag("input_name")
                )

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Address") },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).testTag("input_address")
                )

                OutlinedTextField(
                    value = imageUrl,
                    onValueChange = { imageUrl = it },
                    label = { Text("Image URL") },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).testTag("input_imageUrl")
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = { onDismiss() }) { Text("Cancel") }
                    Button(
                        onClick = {
                            if (!isValidGymInput(name, address, imageUrl)) {
                                Toast.makeText(context, "All fields required", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            coroutineScope.launch {
                                try {
                                    val geocoder = Geocoder(context, Locale.getDefault())
                                    val results = geocoder.getFromLocationName(address, 1)
                                    if (!results.isNullOrEmpty()) {
                                        val lat = results[0].latitude
                                        val lng = results[0].longitude
                                        val geoPoint = GeoPoint(lat, lng)

                                        val gym = Gym(
                                            name = name,
                                            address = address,
                                            imageUrl = imageUrl,
                                            addedBy = uid,
                                            location = geoPoint
                                        )
                                        db.collection("Gyms")
                                            .add(gym)
                                            .addOnSuccessListener { docRef ->
                                                docRef.update("id", docRef.id)
                                                Toast.makeText(context, "Gym added!", Toast.LENGTH_SHORT).show()
                                                onSuccess()
                                            }
                                            .addOnFailureListener {
                                                Toast.makeText(context, "Failed to add gym", Toast.LENGTH_SHORT).show()
                                            }
                                    } else {
                                        Toast.makeText(context, "Invalid address", Toast.LENGTH_SHORT).show()
                                    }
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Geocoding error: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                        modifier = Modifier.testTag("btn_submit")
                    ) {
                        Text("Submit")
                    }
                }
            }
        }
    }
}
