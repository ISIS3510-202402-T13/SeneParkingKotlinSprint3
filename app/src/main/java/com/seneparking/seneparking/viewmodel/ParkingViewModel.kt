package com.seneparking.seneparking.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.seneparking.seneparking.data.ParkingLot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext



class ParkingViewModel : ViewModel() {
    private val database = Firebase.database
    private var db: FirebaseFirestore = Firebase.firestore

    private val _parkingLot = MutableStateFlow<List<ParkingLot>>(emptyList())
    val parkingLot: StateFlow<List<ParkingLot>> = _parkingLot

    init {
        getParkingLots()
    }

    private fun getParkingLots() {
        viewModelScope.launch {
            val result: List<ParkingLot> = withContext(Dispatchers.IO) {
                getAllParkingLots()
            }
            _parkingLot.value = result
        }
    }

    private suspend fun getAllParkingLots(): List<ParkingLot> {
        return try {
            db.collection("parkingLots")
                .get()
                .await()
                .documents.mapNotNull { snapshot -> snapshot.toObject(ParkingLot::class.java) }
        } catch (e: Exception) {
            Log.e("ParkingViewModel", "Error getting parking lots", e)
            emptyList()
        }
    }


}
