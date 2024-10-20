package com.seneparking.seneparking.ui

import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seneparking.seneparking.R
import com.seneparking.seneparking.ui.theme.SeneParkingTheme
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogInScreen(
    onLoginButtonClicked: () -> Unit = {},
    onSignUpButtonClicked: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var mobileNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Validation errors
    var mobileNumberError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var loginError by remember { mutableStateOf<String?>(null) }

    // Ensure we can make network requests on the main thread
    val policy = ThreadPolicy.Builder().permitAll().build()
    StrictMode.setThreadPolicy(policy)

    // Validation function
    fun validateForm(): Boolean {
        var isValid = true

        // Validate that mobile number is numeric and not empty
        if (mobileNumber.isBlank() || !mobileNumber.all { it.isDigit() }) {
            mobileNumberError = "Mobil number must be numeric"
            isValid = false
        } else {
            mobileNumberError = null
        }

        // Validate that password is not empty
        if (password.isBlank()) {
            passwordError = "Password can't be empty"
            isValid = false
        } else {
            passwordError = null
        }

        return isValid
    }

    // Function to make the login HTTP request
    fun loginUser(mobileNumber: String, password: String): Boolean {
        val url = URL("https://firestore.googleapis.com/v1/projects/seneparking-f457b/databases/(default)/documents/users")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        return try {
            val responseCode = connection.responseCode

            if (responseCode == 200) {
                // Parse the response
                val responseStream = connection.inputStream.bufferedReader().use { it.readText() }
                val jsonResponse = JSONObject(responseStream)
                val documents = jsonResponse.getJSONArray("documents")

                // Iterate over the documents to find the correct user
                for (i in 0 until documents.length()) {
                    val doc = documents.getJSONObject(i)
                    val fields = doc.getJSONObject("fields")
                    val firestoreMobileNumber = fields.getJSONObject("mobileNumber").getString("stringValue")
                    val firestorePassword = fields.getJSONObject("password").getString("stringValue")

                    // Compare the mobile number and password
                    if (firestoreMobileNumber == mobileNumber && firestorePassword == password) {
                        return true // Successful login
                    }
                }
                false // User not found or incorrect password
            } else {
                false // Error in the HTTP request
            }
        } catch (e: Exception) {
            false // Any exception in making the request
        } finally {
            connection.disconnect()
        }
    }

    // Main content
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFF3D63))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(80.dp))

        // App Logo
        Image(
            painter = painterResource(id = R.drawable.temp_logo),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(120.dp)
                .align(Alignment.CenterHorizontally),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(16.dp))

        // App Name
        Text(
            text = stringResource(id = R.string.company_name),
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Mobile Number Input
        OutlinedTextField(
            value = mobileNumber,
            onValueChange = { mobileNumber = it },
            label = { Text("Número de móvil") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = mobileNumberError != null,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )
        if (mobileNumberError != null) {
            Text(text = mobileNumberError!!, color = Color.Red)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Password Input
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(id = R.string.password)) },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            isError = passwordError != null,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )
        if (passwordError != null) {
            Text(text = passwordError!!, color = Color.Red)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Log In Button
        Button(
            onClick = {
                if (validateForm()) {
                    if (loginUser(mobileNumber, password)) {
                        onLoginButtonClicked()
                    } else {
                        loginError = "User or password incorrect"
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors()
        ) {
            Text(stringResource(id = R.string.log_in))
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Display login error
        if (loginError != null) {
            Text(text = loginError!!, color = Color.Red)
        }

        // Forgotten Password Text
        Text(
            text = stringResource(id = R.string.forgotten_password),
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 14.sp,
            modifier = Modifier.clickable { /* Handle forgotten password */ }
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Create New Account Button
        Button(
            onClick = { onSignUpButtonClicked() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors()
        ) {
            Text(stringResource(id = R.string.create_new_account))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    SeneParkingTheme {
        LogInScreen(onLoginButtonClicked = {})
    }
}
