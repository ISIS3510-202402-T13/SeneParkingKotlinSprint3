package com.seneparking.seneparking.ui

import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seneparking.seneparking.R
import org.json.JSONObject
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SignUpScreen(
    onSignUpButtonClicked: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var mobileNumber by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }
    var uniandesCode by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Error states for validation messages
    var firstNameError by remember { mutableStateOf<String?>(null) }
    var lastNameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var mobileNumberError by remember { mutableStateOf<String?>(null) }
    var dateOfBirthError by remember { mutableStateOf<String?>(null) }
    var uniandesCodeError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    // Ensure network requests are allowed on the main thread
    val policy = ThreadPolicy.Builder().permitAll().build()
    StrictMode.setThreadPolicy(policy)

    // Validation functions
    val emailPattern = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    fun validateForm(): Boolean {
        var isValid = true

        // Validate First Name
        if (firstName.isBlank()) {
            firstNameError = "First name cannot be empty"
            isValid = false
        } else {
            firstNameError = null
        }

        // Validate Last Name
        if (lastName.isBlank()) {
            lastNameError = "Last name cannot be empty"
            isValid = false
        } else {
            lastNameError = null
        }

        // Validate Email
        if (email.isBlank() || !email.matches(emailPattern)) {
            emailError = "Invalid email format"
            isValid = false
        } else {
            emailError = null
        }

        // Validate Mobile Number
        if (mobileNumber.isBlank() || mobileNumber.length > 10 || !mobileNumber.all { it.isDigit() }) {
            mobileNumberError = "Mobile number must be numeric and not exceed 10 digits"
            isValid = false
        } else {
            mobileNumberError = null
        }

        // Validate Date of Birth (DD/MM/YYYY)
        try {
            dateFormat.isLenient = false
            dateFormat.parse(dateOfBirth)
            dateOfBirthError = null
        } catch (e: Exception) {
            dateOfBirthError = "Date of birth must be in format DD/MM/YYYY"
            isValid = false
        }

        // Validate Uniandes Code
        if (uniandesCode.isBlank() || !uniandesCode.all { it.isDigit() }) {
            uniandesCodeError = "Uniandes code must be numeric"
            isValid = false
        } else {
            uniandesCodeError = null
        }

        // Validate Password
        if (password.isBlank()) {
            passwordError = "Password cannot be empty"
            isValid = false
        } else {
            passwordError = null
        }

        return isValid
    }

    // Function to send the POST request to Firestore API
    fun createUser() {
        val url = URL("https://firestore.googleapis.com/v1/projects/seneparking-f457b/databases/(default)/documents/users")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Content-Type", "application/json; utf-8")
        connection.doOutput = true

        // Create JSON object with the user data
        val userData = JSONObject()
        val fields = JSONObject()

        fields.put("firstName", JSONObject().put("stringValue", firstName))
        fields.put("lastName", JSONObject().put("stringValue", lastName))
        fields.put("email", JSONObject().put("stringValue", email))
        fields.put("mobileNumber", JSONObject().put("stringValue", mobileNumber))
        fields.put("dateOfBirth", JSONObject().put("stringValue", dateOfBirth))
        fields.put("uniandesCode", JSONObject().put("stringValue", uniandesCode))
        fields.put("password", JSONObject().put("stringValue", password)) // Include password

        userData.put("fields", fields)

        try {
            val outputStream: OutputStream = connection.outputStream
            outputStream.write(userData.toString().toByteArray())
            outputStream.flush()

            val responseCode = connection.responseCode
            if (responseCode == 200 || responseCode == 201) {
                onSignUpButtonClicked() // Signup success, navigate to the next screen
            } else {
                // Handle errors, like displaying an error message
            }
        } catch (e: Exception) {
            e.printStackTrace() // Handle the exception
        } finally {
            connection.disconnect()
        }
    }

    // Enable scrolling for the whole form
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState) // Enable scrolling
            .background(Color(0xFFFF3D63))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        // App Logo
        Image(
            painter = painterResource(id = R.drawable.temp_logo),
            contentDescription = "App Logo",
            modifier = Modifier.size(80.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "SeneParking",
            style = MaterialTheme.typography.displayLarge,
            color = Color.White,
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Register",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
        )

        Spacer(modifier = Modifier.height(8.dp))

        // First Name Input
        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("First name") },
            modifier = Modifier.fillMaxWidth(),
            isError = firstNameError != null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.LightGray,
                cursorColor = Color.White,
                focusedLabelColor = Color.White
            )
        )
        if (firstNameError != null) {
            Text(text = firstNameError!!, color = Color.Red)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Last Name Input
        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Last name") },
            modifier = Modifier.fillMaxWidth(),
            isError = lastNameError != null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.LightGray,
                cursorColor = Color.White,
                focusedLabelColor = Color.White
            )
        )
        if (lastNameError != null) {
            Text(text = lastNameError!!, color = Color.Red)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Email Input
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = emailError != null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.LightGray,
                cursorColor = Color.White,
                focusedLabelColor = Color.White
            )
        )
        if (emailError != null) {
            Text(text = emailError!!, color = Color.Red)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Mobile Number Input
        OutlinedTextField(
            value = mobileNumber,
            onValueChange = { mobileNumber = it },
            label = { Text("Mobile number") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = mobileNumberError != null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.LightGray,
                cursorColor = Color.White,
                focusedLabelColor = Color.White
            )
        )
        if (mobileNumberError != null) {
            Text(text = mobileNumberError!!, color = Color.Red)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Date of Birth Input (DD/MM/YYYY format) - regular text keyboard
        OutlinedTextField(
            value = dateOfBirth,
            onValueChange = { dateOfBirth = it },
            label = { Text("Date of birth (DD/MM/YYYY)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text), // Use regular text keyboard
            isError = dateOfBirthError != null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.LightGray,
                cursorColor = Color.White,
                focusedLabelColor = Color.White
            )
        )
        if (dateOfBirthError != null) {
            Text(text = dateOfBirthError!!, color = Color.Red)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Uniandes Code Input
        OutlinedTextField(
            value = uniandesCode,
            onValueChange = { uniandesCode = it },
            label = { Text("Uniandes code") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = uniandesCodeError != null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.LightGray,
                cursorColor = Color.White,
                focusedLabelColor = Color.White
            )
        )
        if (uniandesCodeError != null) {
            Text(text = uniandesCodeError!!, color = Color.Red)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Password Input
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            isError = passwordError != null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.LightGray,
                cursorColor = Color.White,
                focusedLabelColor = Color.White
            )
        )
        if (passwordError != null) {
            Text(text = passwordError!!, color = Color.Red)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Register Button
        IconButton(
            onClick = {
                if (validateForm()) {
                    createUser() // Call the function to create the user
                }
            },
            modifier = Modifier
                .size(56.dp)
                .background(Color.White, shape = CircleShape)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Go forward",
                tint = Color.Red
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    SignUpScreen()
}
