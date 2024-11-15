package com.seneparking.seneparking

import android.os.Build
import android.util.LruCache
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.seneparking.seneparking.services.map.GetLocationUseCase
import com.seneparking.seneparking.storage.LocalStorageManagement
import com.seneparking.seneparking.ui.model.ParkingLot
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.S)
@HiltViewModel
class MapActivityVM @Inject constructor(
    private val getLocationUseCase: GetLocationUseCase,
    private val localStorageManagement: LocalStorageManagement // Inject local storage management
) : ViewModel() {

    private val _viewState: MutableStateFlow<ViewState> = MutableStateFlow(ViewState.Loading)
    val viewState: StateFlow<ViewState> = _viewState.asStateFlow()

    private val _favoriteParkingLot: MutableStateFlow<String?> = MutableStateFlow(null)
    val favoriteParkingLot: StateFlow<String?> = _favoriteParkingLot.asStateFlow()

    // LruCache for caching parking lot data
    private val parkingLotCache: LruCache<String, List<ParkingLot>> = LruCache(5)

    private val _parkingLots = MutableStateFlow<List<ParkingLot>>(emptyList())
    val parkingLots: StateFlow<List<ParkingLot>> = _parkingLots.asStateFlow()

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    init {
        loadFavoriteParkingLot() // Load favorite parking lot on initialization
        fetchParkingLots() // Fetch parking lots with caching
    }

    // Handle location permission events
    fun handle(event: PermissionEvent) {
        when (event) {
            PermissionEvent.Granted -> {
                viewModelScope.launch {
                    getLocationUseCase.invoke().collect {
                        _viewState.value = ViewState.Success(it)
                    }
                }
            }
            PermissionEvent.Revoked -> {
                _viewState.value = ViewState.RevokedPermissions
            }
        }
    }

    // Save favorite parking lot ID
    fun saveFavoriteParkingLot(parkingLotId: String) {
        viewModelScope.launch {
            localStorageManagement.saveFavoriteParkingLot(parkingLotId)
            _favoriteParkingLot.value = parkingLotId
        }
    }

    // Load favorite parking lot ID from local storage
    private fun loadFavoriteParkingLot() {
        viewModelScope.launch {
            val storedFavorite = localStorageManagement.getFavoriteParkingLot()
            _favoriteParkingLot.value = storedFavorite
        }
    }

    // Fetch parking lots with caching
    private fun fetchParkingLots() {
        viewModelScope.launch {
            // Check if data is in memory cache
            val cachedData = parkingLotCache.get("parkingLots")
            if (cachedData != null) {
                _parkingLots.value = cachedData // Load from memory cache
            } else {
                // Check if data is in persistent cache
                val storedData = localStorageManagement.getCachedParkingLots()
                if (storedData.isNotEmpty()) {
                    _parkingLots.value = storedData // Load from persistent cache
                    parkingLotCache.put("parkingLots", storedData) // Also cache in memory
                } else {
                    // !cache = fetch from Firebase and cache it
                    val fetchedData = getParkingLotsFromFirebase()
                    if (fetchedData.isNotEmpty()) {
                        parkingLotCache.put("parkingLots", fetchedData) // Cache in memory
                        localStorageManagement.saveParkingLots(fetchedData) // Save in persistent cache
                        _parkingLots.value = fetchedData
                    }
                }
            }
        }
    }

    // Function to get data from Firebase
    private suspend fun getParkingLotsFromFirebase(): List<ParkingLot> {
        return try {
            db.collection("parkingLots")
                .get()
                .await() // Await the result from Firebase
                .documents
                .mapNotNull { it.toObject(ParkingLot::class.java) } // Map documents to ParkingLot objects
        } catch (e: Exception) {
            emptyList() // Return an empty list in case of error
        }
    }

    // Save user credentials
    fun saveUserCredentials(username: String, token: String) {
        val credentials = "$username:$token"
        localStorageManagement.saveUserCredentials(credentials)
    }

    // Retrieve user credentials
    fun getUserCredentials(): String? {
        return localStorageManagement.getUserCredentials()
    }
}

sealed interface ViewState {
    object Loading : ViewState
    data class Success(val location: LatLng?) : ViewState
    object RevokedPermissions : ViewState
}

sealed interface PermissionEvent {
    object Granted : PermissionEvent
    object Revoked : PermissionEvent
}
