package com.seneparking.seneparking

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.seneparking.seneparking.ui.theme.SeneParkingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SeneParkingTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    LoginScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen() {

    val mobileNumber = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

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

        // Mobile Number or University ID Input
        OutlinedTextField(
            value = "",
            onValueChange = {mobileNumber.value = it},
            label = { Text(stringResource(id = R.string.mobile_number_sign_in_hint)) },
            modifier = Modifier
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )

        Spacer(modifier = Modifier.height(mediumSpacing))

        // Password Input
        OutlinedTextField(
            value = "",
            onValueChange = {password.value = it},
            label = { Text(stringResource(id = R.string.password)) },
            modifier = Modifier
                .fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Log In Button
        Button(
            onClick = { /* Handle login */ },
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
            onClick = { /* Handle account creation */ },
            modifier = Modifier
                .fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors()) {
                Text(stringResource(id = R.string.create_new_account))}

    }
}


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    SeneParkingTheme {
        LoginScreen()
    }
}
