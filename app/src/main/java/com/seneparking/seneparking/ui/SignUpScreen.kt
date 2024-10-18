package com.seneparking.seneparking.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seneparking.seneparking.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    onSignUpButtonClicked: () -> Unit = {}, // Define what happens when the button is clicked
    modifier: Modifier = Modifier
) {

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var mobileNumber by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }
    var uniandesCode by remember { mutableStateOf("") }

    // Main background color
    val backgroundColor = Color(0xFFFF3D63) // Match the background color of the screen in the image

    // Padding and spacing
    val padding = 16.dp
    val largeSpacing = 20.dp
    val smallSpacing = 8.dp

    // Main content
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(padding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        // App Logo
        Image(
            painter = painterResource(id = R.drawable.temp_logo), // Use your logo here
            contentDescription = "App Logo",
            modifier = Modifier.size(80.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(smallSpacing))

        // App Name
        Text(
            text = "SeneParking", // Hardcoded app name, or use a string resource
            style = MaterialTheme.typography.displayLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(largeSpacing))

        // Register Title
        Text(
            text = "Register",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        // Instructions
        Text(
            text = "Please enter your information to create an account",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White,
            modifier = Modifier.padding(vertical = smallSpacing)
        )

        Spacer(modifier = Modifier.height(smallSpacing))

        // First Name Input
        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("First name") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.LightGray,
                cursorColor = Color.White,
                focusedLabelColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(smallSpacing))

        // Last Name Input
        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Last name") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.LightGray,
                cursorColor = Color.White,
                focusedLabelColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(smallSpacing))

        // Email Input
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.LightGray,
                cursorColor = Color.White,
                focusedLabelColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(smallSpacing))

        // Mobile Number Input
        OutlinedTextField(
            value = mobileNumber,
            onValueChange = { mobileNumber = it },
            label = { Text("Mobile number") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.LightGray,
                cursorColor = Color.White,
                focusedLabelColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(smallSpacing))

        // Date of Birth Input
        OutlinedTextField(
            value = dateOfBirth,
            onValueChange = { dateOfBirth = it },
            label = { Text("Date of birth") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), // You can enhance it to a date picker if needed
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.LightGray,
                cursorColor = Color.White,
                focusedLabelColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(smallSpacing))

        // Uniandes Code Input
        OutlinedTextField(
            value = uniandesCode,
            onValueChange = { uniandesCode = it },
            label = { Text("Uniandes code") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.LightGray,
                cursorColor = Color.White,
                focusedLabelColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(largeSpacing))

        // "By continuing" disclaimer
        Text(
            text = "By continuing, you agree to our\nTerms of Service and Privacy Policy.",
            style = MaterialTheme.typography.bodySmall,
            color = Color.White,
            modifier = Modifier.padding(vertical = smallSpacing),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(largeSpacing))

        // Register Button with Icon
        IconButton(
            onClick = { onSignUpButtonClicked() },
            modifier = Modifier
                .size(56.dp)
                .background(Color.White, shape = CircleShape) // Optional for round button
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Go forward",
                tint = Color.Red // Set the icon color to red, you can change this
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    SignUpScreen()
}
