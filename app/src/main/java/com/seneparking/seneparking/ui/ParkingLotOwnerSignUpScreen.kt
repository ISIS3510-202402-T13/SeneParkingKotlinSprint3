package com.seneparking.seneparking.ui

import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seneparking.seneparking.ui.theme.SeneParkingTheme
import org.json.JSONObject
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParkingLotOwnerSignUpScreen(
    onRegisterButtonClicked: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var parkingLotName by remember { mutableStateOf("") }
    var farePerDay by remember { mutableStateOf("") }
    var openTime by remember { mutableStateOf("") }
    var closeTime by remember { mutableStateOf("") }
    var availableSpots by remember { mutableStateOf("") }
    var longitude by remember { mutableStateOf("") }
    var latitude by remember { mutableStateOf("") }
    var availableEVSpots by remember { mutableStateOf("") }

    var message by remember { mutableStateOf<String?>(null) }
    var showAlert by remember { mutableStateOf(false) }

    // Setting network policy to allow network requests on the main thread
    val policy = ThreadPolicy.Builder().permitAll().build()
    StrictMode.setThreadPolicy(policy)

    // Function to validate the form inputs
    fun validateForm(): Boolean {
        message = when {
            parkingLotName.isBlank() -> "Parking Lot Name is required."
            farePerDay.isBlank() || farePerDay.toIntOrNull() == null || farePerDay.toInt() <= 0 -> "Fare Per Day must be a positive number."
            openTime.isBlank() -> "Open Time is required."
            closeTime.isBlank() -> "Close Time is required."
            availableSpots.isBlank() || availableSpots.toIntOrNull() == null || availableSpots.toInt() < 0 -> "Available Spots must be a non-negative number."
            longitude.isBlank() || longitude.toDoubleOrNull() == null || longitude.toDouble() !in -180.0..180.0 -> "Longitude must be between -180 and 180."
            latitude.isBlank() || latitude.toDoubleOrNull() == null || latitude.toDouble() !in -90.0..90.0 -> "Latitude must be between -90 and 90."
            availableEVSpots.isBlank() || availableEVSpots.toIntOrNull() == null || availableEVSpots.toInt() < 0 -> "Available EV Spots must be a non-negative number."
            else -> null
        }
        return message == null
    }

    // Function to create the parking lot in Firestore
    fun registerParkingLot() {
        val url = URL("https://firestore.googleapis.com/v1/projects/seneparking-f457b/databases/(default)/documents/parkingLots")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Content-Type", "application/json; utf-8")
        connection.doOutput = true

        // JSON object with the parking lot data
        val parkingData = JSONObject().apply {
            put("fields", JSONObject().apply {
                put("name", JSONObject().put("stringValue", parkingLotName))
                put("farePerDay", JSONObject().put("integerValue", farePerDay.toInt()))
                put("open_time", JSONObject().put("stringValue", openTime))
                put("close_time", JSONObject().put("stringValue", closeTime))
                put("availableSpots", JSONObject().put("integerValue", availableSpots.toInt()))
                put("longitude", JSONObject().put("doubleValue", longitude.toDouble()))
                put("latitude", JSONObject().put("doubleValue", latitude.toDouble()))
                put("available_ev_spots", JSONObject().put("integerValue", availableEVSpots.toInt()))
            })
        }

        try {
            val outputStream: OutputStream = connection.outputStream
            outputStream.write(parkingData.toString().toByteArray())
            outputStream.flush()

            if (connection.responseCode == 200 || connection.responseCode == 201) {
                message = "Parking lot successfully registered!"
                onRegisterButtonClicked()
            } else {
                message = "Failed to register parking lot"
            }
        } catch (e: Exception) {
            message = "Error: ${e.localizedMessage}"
        } finally {
            connection.disconnect()
            showAlert = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF64A55))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // Title
        Text(
            text = "Register Your Parking Lot",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Fields for the parking lot registration form
        OutlinedTextField(
            value = parkingLotName,
            onValueChange = { parkingLotName = it },
            label = { Text("Parking Lot Name") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(containerColor = Color.White)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = farePerDay,
            onValueChange = { farePerDay = it },
            label = { Text("Fare Per Day") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = TextFieldDefaults.outlinedTextFieldColors(containerColor = Color.White)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = openTime,
            onValueChange = { openTime = it },
            label = { Text("Open Time (e.g., 6:00am)") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(containerColor = Color.White)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = closeTime,
            onValueChange = { closeTime = it },
            label = { Text("Close Time (e.g., 10:00pm)") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(containerColor = Color.White)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = availableSpots,
            onValueChange = { availableSpots = it },
            label = { Text("Available Spots") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = TextFieldDefaults.outlinedTextFieldColors(containerColor = Color.White)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = longitude,
            onValueChange = { longitude = it },
            label = { Text("Longitude") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            colors = TextFieldDefaults.outlinedTextFieldColors(containerColor = Color.White)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = latitude,
            onValueChange = { latitude = it },
            label = { Text("Latitude") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            colors = TextFieldDefaults.outlinedTextFieldColors(containerColor = Color.White)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = availableEVSpots,
            onValueChange = { availableEVSpots = it },
            label = { Text("Available EV Spots") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = TextFieldDefaults.outlinedTextFieldColors(containerColor = Color.White)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Register button with validation and submission
        Button(
            onClick = {
                if (validateForm()) {
                    registerParkingLot()
                } else {
                    showAlert = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
        ) {
            Text("Register Parking Lot", color = Color.White)
        }

        // Alert Dialog to show validation or registration message
        if (showAlert) {
            AlertDialog(
                onDismissRequest = { showAlert = false },
                confirmButton = {
                    TextButton(onClick = { showAlert = false }) {
                        Text("OK")
                    }
                },
                title = { Text("Registration Status") },
                text = { Text(message ?: "Please review the form and correct any errors.") }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ParkingLotOwnerSignUpScreenPreview() {
    SeneParkingTheme {
        ParkingLotOwnerSignUpScreen()
    }
}
