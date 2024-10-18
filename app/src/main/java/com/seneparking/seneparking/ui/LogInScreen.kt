package com.seneparking.seneparking.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seneparking.seneparking.R
import com.seneparking.seneparking.ui.theme.SeneParkingTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogInScreen(
    onLoginButtonClicked: () -> Unit = {},
    onSignUpButtonClicked: () -> Unit = {},
    modifier: Modifier = Modifier
) {

    var uniandesCode by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Validation errors
    var uniandesCodeError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    // Validation function
    fun validateForm(): Boolean {
        var isValid = true

        // Validate that uniandesCode is numeric
        if (uniandesCode.isBlank() || !uniandesCode.all { it.isDigit() }) {
            uniandesCodeError = "Uniandes code must be numeric"
            isValid = false
        } else {
            uniandesCodeError = null
        }

        // Validate that password is not empty
        if (password.isBlank()) {
            passwordError = "Password cannot be empty"
            isValid = false
        } else {
            passwordError = null
        }

        return isValid
    }

    // Main background color
    val backgroundColor = Color(0xFFFF3D63)

    // Set padding and spacing
    val padding = 16.dp
    val logoSize = 120.dp
    val largeSpacing = 40.dp
    val mediumSpacing = 20.dp
    val smallSpacing = 8.dp

    // Main content
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(padding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(80.dp))

        // App Logo
        Image(
            painter = painterResource(id = R.drawable.temp_logo),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(logoSize)
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

        Spacer(modifier = Modifier.height(largeSpacing))

        // Uniandes Code Input
        OutlinedTextField(
            value = uniandesCode,
            onValueChange = { uniandesCode = it },
            label = { Text(stringResource(id = R.string.mobile_number_sign_in_hint)) },
            modifier = Modifier
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = uniandesCodeError != null,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )
        if (uniandesCodeError != null) {
            Text(text = uniandesCodeError!!, color = Color.Red)
        }

        Spacer(modifier = Modifier.height(mediumSpacing))

        // Password Input
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(id = R.string.password)) },
            modifier = Modifier
                .fillMaxWidth(),
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
                    onLoginButtonClicked()
                }
            },
            modifier = Modifier
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors()
        ) {
            Text(stringResource(id = R.string.log_in))
        }

        Spacer(modifier = Modifier.height(smallSpacing))

        // Forgotten Password Text
        Text(
            text = stringResource(id = R.string.forgotten_password),
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 14.sp,
            modifier = Modifier.clickable { /* Handle forgotten password */ }
        )

        Spacer(modifier = Modifier.height(largeSpacing))

        // Create New Account Button
        Button(
            onClick = { onSignUpButtonClicked() },
            modifier = Modifier
                .fillMaxWidth(),
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
