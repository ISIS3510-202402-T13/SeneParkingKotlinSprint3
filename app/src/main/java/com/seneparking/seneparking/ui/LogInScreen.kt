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

private const val MOBILE_NUMBER_MAX_LENGTH = 10
private const val PASSWORD_MAX_LENGTH = 20
private const val MOBILE_NUMBER_EXACT_LENGTH = 10

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogInScreen(
    onLoginButtonClicked: () -> Unit = {},
    onSignUpButtonClicked: () -> Unit = {},
    onForgotPasswordClicked: () -> Unit = {},
    onParkingLotOwnerButtonClicked: () -> Unit = {}, // New parameter for navigation
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

        if (mobileNumber.isBlank()) {
            mobileNumberError = "Mobile number cannot be empty"
            isValid = false
        } else if (!mobileNumber.all { it.isDigit() }) {
            mobileNumberError = "Mobile number must be numeric"
            isValid = false
        } else if (mobileNumber.length != MOBILE_NUMBER_EXACT_LENGTH) {
            mobileNumberError = "Mobile number must be exactly 10 digits"
            isValid = false
        } else {
            mobileNumberError = null
        }

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
                val responseStream = connection.inputStream.bufferedReader().use { it.readText() }
                val jsonResponse = JSONObject(responseStream)
                val documents = jsonResponse.getJSONArray("documents")

                for (i in 0 until documents.length()) {
                    val doc = documents.getJSONObject(i)
                    val fields = doc.getJSONObject("fields")
                    val firestoreMobileNumber = fields.getJSONObject("mobileNumber").getString("stringValue")
                    val firestorePassword = fields.getJSONObject("password").getString("stringValue")

                    if (firestoreMobileNumber == mobileNumber && firestorePassword == password) {
                        return true // Successful login
                    }
                }
                false
            } else {
                false
            }
        } catch (e: Exception) {
            false
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
            onValueChange = { if (it.length <= MOBILE_NUMBER_MAX_LENGTH) mobileNumber = it },
            label = { Text("Mobile number") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = mobileNumberError != null,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                cursorColor = MaterialTheme.colorScheme.primary,
                containerColor = Color.White,
                errorContainerColor = Color.White
            )
        )
        if (mobileNumberError != null) {
            Text(text = mobileNumberError!!, color = Color.White)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Password Input
        OutlinedTextField(
            value = password,
            onValueChange = { if (it.length <= PASSWORD_MAX_LENGTH) password = it },
            label = { Text(stringResource(id = R.string.password)) },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            isError = passwordError != null,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                cursorColor = MaterialTheme.colorScheme.primary,
                containerColor = Color.White,
                errorContainerColor = Color.White
            )
        )
        if (passwordError != null) {
            Text(text = passwordError!!, color = Color.White)
        }

        // Display login error
        if (loginError != null) {
            Text(text = loginError!!, color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

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
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White
            )
        ) {
            Text(text = "Log in", color = Color.Red)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Forgotten Password Text
        Text(
            text = "Forgot password?",
            color = Color.White,
            fontSize = 14.sp,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .clickable { onForgotPasswordClicked() }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Create New Account Button
        OutlinedButton(
            onClick = { onSignUpButtonClicked() },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color.White
            )
        ) {
            Text("Create new account")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // I'm a Parking Lot Owner Button
        Button(
            onClick = { onParkingLotOwnerButtonClicked() }, // Trigger navigation
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Blue,
                contentColor = Color.White
            )
        ) {
            Text("I'm a Parking Lot Owner")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LogInScreenPreview() {
    SeneParkingTheme {
        LogInScreen(onLoginButtonClicked = {})
    }
}
