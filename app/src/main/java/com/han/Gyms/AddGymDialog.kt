package com.han.Gyms

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.window.Dialog
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.han.Gyms.Gym
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

@Composable
fun AddGymDialog(onDismiss: () -> Unit, onSuccess: () -> Unit) {
    val db = Firebase.firestore
    val context = LocalContext.current
    val uid = Firebase.auth.currentUser?.uid ?: return

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
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                )

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Address") },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                )

                OutlinedTextField(
                    value = imageUrl,
                    onValueChange = { imageUrl = it },
                    label = { Text("Image URL") },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = { onDismiss() }) { Text("Cancel") }
                    Button(onClick = {
                        val gym = Gym(name, address, imageUrl, uid)
                        db.collection("Gyms")
                            .add(gym)
                            .addOnSuccessListener {
                                Toast.makeText(context, "Gym added!", Toast.LENGTH_SHORT).show()
                                onSuccess()
                            }
                            .addOnFailureListener {
                                Toast.makeText(context, "Failed to add gym", Toast.LENGTH_SHORT).show()
                            }
                    }) {
                        Text("Submit")
                    }
                }
            }
        }
    }
}
