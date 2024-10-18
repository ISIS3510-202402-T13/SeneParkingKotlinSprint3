package com.seneparking.seneparking.ui

import android.Manifest
import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.google.maps.android.compose.*
import com.seneparking.seneparking.ui.theme.SeneParkingTheme
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.model.LatLng

@Composable
fun MapScreen() {
    val context = LocalContext.current
    var locationEnabled by remember { mutableStateOf(false) }

    // Request location permission
    val permissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    LaunchedEffect(key1 = permissionState.hasPermission) {
        if (permissionState.hasPermission) {
            locationEnabled = true
        } else {
            permissionState.launchPermissionRequest()
        }
    }

    if (locationEnabled) {
        MyLocationMap()
    } else {
        // You can show an alternative UI if permission is denied
    }
}

@Composable
fun MyLocationMap() {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var myLocation by remember { mutableStateOf<LatLng?>(null) }

    // Get the user's last known location
    LaunchedEffect(Unit) {
        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    myLocation = LatLng(location.latitude, location.longitude)
                }
            }
        }
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        properties = MapProperties(isMyLocationEnabled = true), // Enables blue dot for current location
        uiSettings = MapUiSettings(zoomControlsEnabled = true)
    ) {
        // Center map around user location if available
        myLocation?.let {
            CameraPositionState(position = CameraPosition(it, 15f, 0f, 0f))
            Marker(
                position = it,
                title = "Your Location",
            )
        }
    }
}