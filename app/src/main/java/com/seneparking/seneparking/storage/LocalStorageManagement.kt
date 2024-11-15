package com.seneparking.seneparking.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seneparking.seneparking.ui.model.ParkingLot
import javax.inject.Inject

class LocalStorageManagement @Inject constructor(context: Context) {

    companion object {
        private const val PREFS_NAME = "sene_parking_prefs"
        private const val KEY_USER_CREDENTIALS = "user_credentials"
        private const val KEY_FAVORITE_PARKING_LOT = "favorite_parking_lot"
        private const val KEY_CACHED_PARKING_LOTS = "cached_parking_lots"
    }

    private val sharedPreferences: SharedPreferences
    private val gson = Gson()

    init {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        sharedPreferences = EncryptedSharedPreferences.create(
            context,
            PREFS_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    // Save user credentials
    fun saveUserCredentials(credentials: String) {
        sharedPreferences.edit().putString(KEY_USER_CREDENTIALS, credentials).apply()
    }

    // Retrieve user credentials
    fun getUserCredentials(): String? {
        return sharedPreferences.getString(KEY_USER_CREDENTIALS, null)
    }

    // Save favorite parking lot
    fun saveFavoriteParkingLot(parkingLotId: String) {
        sharedPreferences.edit().putString(KEY_FAVORITE_PARKING_LOT, parkingLotId).apply()
    }

    // Retrieve favorite parking lot
    fun getFavoriteParkingLot(): String? {
        return sharedPreferences.getString(KEY_FAVORITE_PARKING_LOT, null)
    }

    // Save parking lots to cache
    fun saveParkingLots(parkingLots: List<ParkingLot>) {
        val json = gson.toJson(parkingLots)
        sharedPreferences.edit().putString(KEY_CACHED_PARKING_LOTS, json).apply()
    }

    // Retrieve cached parking lots
    fun getCachedParkingLots(): List<ParkingLot> {
        val json = sharedPreferences.getString(KEY_CACHED_PARKING_LOTS, null) ?: return emptyList()
        val type = object : TypeToken<List<ParkingLot>>() {}.type
        return gson.fromJson(json, type)
    }

    // Clear all data
    fun clearAll() {
        sharedPreferences.edit().clear().apply()
    }
}
