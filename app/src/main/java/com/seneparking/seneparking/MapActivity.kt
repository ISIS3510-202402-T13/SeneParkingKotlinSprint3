package com.seneparking.seneparking

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.seneparking.seneparking.ui.model.ParkingLot
import com.seneparking.seneparking.ui.theme.SeneParkingTheme
import com.seneparking.seneparking.storage.LocalStorageManagement
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("MissingPermission")
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState()

            // Initialize ViewModel
            val locationViewModel: MapActivityVM by viewModels()

        // Inject LocalStorageManagement (use Hilt to provide an instance)
        val localStorageManagement: LocalStorageManagement = // Obtain injected instance (e.g., using Hilt)

            setContent {

                // Permissions state
                val permissionState = rememberMultiplePermissionsState(
                    permissions = listOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )

                // Collect view state and favorite parking lot
                val viewState by locationViewModel.viewState.collectAsStateWithLifecycle()
                val favoriteParkingLotId by locationViewModel.favoriteParkingLot.collectAsStateWithLifecycle()

                SeneParkingTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        LaunchedEffect(!hasLocationPermission()) {
                            permissionState.launchMultiplePermissionRequest()
                        }

                        when {
                            permissionState.allPermissionsGranted -> {
                                LaunchedEffect(Unit) {
                                    locationViewModel.handle(PermissionEvent.Granted)
                                }
                            }

                            permissionState.shouldShowRationale -> {
                                RationaleAlert(onDismiss = { }) {
                                    permissionState.launchMultiplePermissionRequest()
                                }
                            }

                            !permissionState.allPermissionsGranted && !permissionState.shouldShowRationale -> {
                                LaunchedEffect(Unit) {
                                    locationViewModel.handle(PermissionEvent.Revoked)
                                }
                            }
                        }

                        with(viewState) {
                            when (this) {
                                ViewState.Loading -> {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator()
                                    }
                                }

                                ViewState.RevokedPermissions -> {
                                    PermissionsRationale()
                                }

                                is ViewState.Success -> {
                                    val currentLoc = LatLng(
                                        this.location?.latitude ?: 0.0,
                                        this.location?.longitude ?: 0.0
                                    )
                                    val cameraState = rememberCameraPositionState()

                                    LaunchedEffect(key1 = currentLoc) {
                                        cameraState.centerOnLocation(currentLoc)
                                    }

                                    MainScreen(
                                        currentPosition = currentLoc,
                                        cameraState = cameraState,
                                        favoriteParkingLotId = favoriteParkingLotId
                                    )
                                }
                            }
                        }
                    }
                }
            }
    }
}

@Composable
fun MainScreen(
    currentPosition: LatLng,
    cameraState: CameraPositionState,
    parkingLotViewModel: ParkingLotViewModel = ParkingLotViewModel(),
    favoriteParkingLotId: String?
) {
    val marker = LatLng(currentPosition.latitude, currentPosition.longitude)
    val parkingLots by parkingLotViewModel.parkingLot.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraState,
            properties = MapProperties(
                isMyLocationEnabled = true,
                mapType = MapType.NORMAL,
                isTrafficEnabled = true
            )
        ) {
            // Show current location marker
            Marker(
                state = MarkerState(position = marker),
                title = "MyPosition",
                snippet = "This is a description of this Marker",
                draggable = true
            )

            // Show parking lot markers
            parkingLots.forEach { parkingLot ->
                val latitude = parkingLot.latitude ?: 0.0
                val longitude = parkingLot.longitude ?: 0.0
                val name = parkingLot.name ?: "Unknown"

                Marker(
                    state = MarkerState(position = LatLng(latitude, longitude)),
                    title = name,
                    icon = BitmapDescriptorFactory.defaultMarker(
                        if (parkingLot.id == favoriteParkingLotId) BitmapDescriptorFactory.HUE_GREEN
                        else BitmapDescriptorFactory.HUE_BLUE
                    )
                )
            }
        }
    }
}

@Composable
fun PermissionsRationale() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("We need permissions to use this app")
        Button(
            onClick = {
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            },
            enabled = !hasLocationPermission()
        ) {
            Text("Settings")
        }
    }
}

@Composable
fun RationaleAlert(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties()
    ) {
        Surface(
            modifier = Modifier.wrapContentSize(),
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("We need location permissions to use this app")
                Spacer(modifier = Modifier.height(24.dp))
                TextButton(
                    onClick = {
                        onConfirm()
                        onDismiss()
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("OK")
                }
            }
        }
    }
}

private suspend fun CameraPositionState.centerOnLocation(location: LatLng) = animate(
    update = CameraUpdateFactory.newLatLngZoom(location, 15f),
    durationMs = 1500
)
