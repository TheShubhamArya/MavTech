package com.example.mavtech

import java.io.Serializable

data class RentalItem (
    var name: String? = null,
    var type: String? = null,
    var description: String? = null,
    var rentedFromDate: String? = null,
    var rentedToDate: String? = null,
    var id: String? = null,
    var rentedByUserID : String? = null
) : Serializable
