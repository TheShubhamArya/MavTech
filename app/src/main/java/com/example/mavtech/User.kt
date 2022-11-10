package com.example.mavtech

import com.google.firebase.database.DataSnapshot
import java.io.Serializable

data class User(
    var name: String? = null,
    var email: String? = null,
    var currentRentals: String? = null
) : Serializable

data class CurrentlyRented (
    var id: String? = null,
    var dateFrom: String? = null,
    var dateTo: String? = null,
    var device : RentalItem? = null
) : Serializable

