package com.seneparking.seneparking.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

// Data classes for parsing the Firestore response
data class ParkingLotsResponse(
    val documents: List<ParkingLotDocument>
)

data class ParkingLotDocument(
    val name: String,
    val fields: ParkingLotFields
)

data class ParkingLotData(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val totalSpots: Int,
    val availableSpots: Int,
    val availableEvSpots: Int,
    val address: String?,
    val city: String?,
    val district: String?,
    val farePerDay: Int?,
    val openTime: String?,
    val closeTime: String?
)


data class ParkingLotFields(
    val totalSpots: IntegerField?,
    val close_time: TimestampField?,
    val latitude: DoubleField,
    val created_at: TimestampField?,
    val city: StringField?,
    val availableSpots: IntegerField?,
    val address: StringField?,
    val available_ev_spots: IntegerField?,
    val district: StringField?,
    val open_time: TimestampField?,
    val longitude: DoubleField,
    val name: StringField,
    val farePerDay: IntegerField? = null
)

data class IntegerField(
    val integerValue: String
)

data class DoubleField(
    val doubleValue: Double
)

data class StringField(
    val stringValue: String
)

data class TimestampField(
    val timestampValue: String
)

// Retrofit interface to fetch parking lots
interface ParkingApiService {
    @GET("projects/seneparking-f457b/databases/(default)/documents/parkingLots")
    suspend fun getParkingLots(): ParkingLotsResponse
}

object RetrofitInstance {
    val api: ParkingApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://firestore.googleapis.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ParkingApiService::class.java)
    }
}
