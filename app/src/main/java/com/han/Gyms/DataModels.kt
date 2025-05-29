package com.han.Gyms

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint

data class User(
    val name: String = "",
    val email: String = ""
)

data class Gym(
    val id: String = "",
    val name: String = "",
    val address: String = "",
    val imageUrl: String = "",
    val addedBy: String = "",
    val location: GeoPoint = GeoPoint(0.0, 0.0)
)

data class Comment(
    val gymId: String = "",
    val userId: String = "",
    val content: String = "",
    val userName: String = "",
    val timestamp: Timestamp = Timestamp.now()
)
