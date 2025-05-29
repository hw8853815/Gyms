package com.han.Gyms

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.testTag
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore

fun isValidRegisterInput(userName: String, email: String, password: String): Boolean {
    return userName.isNotBlank() && email.isNotBlank() && password.isNotBlank()
}

fun isValidLoginInput(email: String, password: String): Boolean {
    return email.isNotBlank() && password.isNotBlank()
}

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit = {},
    auth: FirebaseAuth = Firebase.auth
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("page_login"),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = userName,
            onValueChange = { userName = it },
            label = { Text("Username") },
            modifier = Modifier.padding(bottom = 8.dp).testTag("input_username")
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.testTag("input_email")
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.testTag("input_password")
        )

        Button(
            onClick = {
                if (!isValidRegisterInput(userName, email, password)) {
                    Toast.makeText(context, "All fields are required for registration", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val uid = auth.currentUser?.uid
                            val db = Firebase.firestore
                            val user = hashMapOf(
                                "name" to userName,
                                "email" to email
                            )
                            if (uid != null) {
                                db.collection("Users").document(uid).set(user)
                            }
                            Toast.makeText(context, "Registration successful!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            },
            modifier = Modifier.padding(top = 8.dp).testTag("btn_register")
        ) {
            Text("Register")
        }

        Button(
            onClick = {
                if (!isValidLoginInput(email, password)) {
                    Toast.makeText(context, "Email and password cannot be empty", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show()
                            onLoginSuccess()
                        } else {
                            Toast.makeText(context, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            },
            modifier = Modifier.padding(top = 8.dp).testTag("btn_login")
        ) {
            Text("Login")
        }
    }
}
