package com.seneparking.seneparking.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seneparking.seneparking.R
import com.seneparking.seneparking.ui.theme.SeneParkingTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    onBackButtonClicked: () -> Unit = {},
    onResetPasswordClicked: () -> Unit = {}
) {
    var emailOrMobile: String by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFF3D63)) // Fondo rojo
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Back Button
        Text(
            text = "Back",
            color = Color.Blue,
            modifier = Modifier
                .align(Alignment.Start)
                .clickable { onBackButtonClicked() }
                .padding(8.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Key Icon
        Image(
            painter = painterResource(id = R.drawable.key_icon), // Asegúrate de tener el icono de la llave en los recursos
            contentDescription = "Key Icon",
            modifier = Modifier.size(100.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Title
        Text(
            text = "Forgot Password?",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Subtitle
        Text(
            text = "Enter your email or mobile number to reset your password",
            fontSize = 16.sp,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 16.dp),
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Input Field
        OutlinedTextField(
            value = emailOrMobile,
            onValueChange = { emailOrMobile = it },
            label = { Text("Email or Mobile Number") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                cursorColor = MaterialTheme.colorScheme.primary,
                containerColor = Color.White,  // Fondo blanco para el campo de texto
                errorContainerColor = Color.White // Fondo blanco en estado de error
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Reset Password Button
        Button(
            onClick = { onResetPasswordClicked() },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White // Fondo blanco
            )
        ) {
            Text(
                text = "Reset Password",
                color = Color(0xFFFF3D63), // Texto del mismo color que el fondo de pantalla
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ForgotPasswordScreenPreview() {
    SeneParkingTheme {
        ForgotPasswordScreen()
    }
}
