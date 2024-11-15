package com.seneparking.seneparking.data

data class ParkingLot(
    val name: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val totalSpots: Int? = null,
    val availableSpots: Int? = null,
    val availableEvSpots: Int? = null,
    val address: String? = null,
    val city: String? = null,
    val district: String? = null,
    val farePerDay: Int? = null,
    val openTime: String? = null,
    val closeTime: String? = null
)
