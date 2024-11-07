package com.seneparking.seneparking

import android.content.Intent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.seneparking.seneparking.ui.AuthenticationViewModel
import com.seneparking.seneparking.ui.ForgotPasswordScreen
import com.seneparking.seneparking.ui.LogInScreen
import com.seneparking.seneparking.ui.MapScreen
import com.seneparking.seneparking.ui.ParkingLotOwnerSignUpScreen // Import the screen
import com.seneparking.seneparking.ui.SignUpScreen

/**
 * enum values that represent the screens in the app
 */
enum class SeneParkingScreen(@StringRes val title: Int) {
    Start(title = R.string.seneparking),
    SignUp(title = R.string.sign_up),
    Map(title = R.string.map),
    ForgotPassword(title = R.string.forgot_password),
    ParkingLotOwnerSignUp(title = R.string.parking_lot_owner_sign_up) // Add this screen to the enum
}

/**
 * Composable that displays the topBar and displays back button if back navigation is possible.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeneParkingAppBar(
    currentScreen: SeneParkingScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

@Composable
fun SeneParkingApp(
    viewModel: AuthenticationViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current
    // Get current backstack entry
    val backStackEntry by navController.currentBackStackEntryAsState()

    // Get the name of the current screen
    val currentScreen = SeneParkingScreen.valueOf(
        backStackEntry?.destination?.route ?: SeneParkingScreen.Start.name
    )

    Scaffold(
        topBar = {
            SeneParkingAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = SeneParkingScreen.Start.name,
            modifier = Modifier.fillMaxSize()
        ) {
            composable(route = SeneParkingScreen.Start.name) {
                LogInScreen(
                    modifier = Modifier.padding(innerPadding),
                    onLoginButtonClicked = { context.startActivity(Intent(context, MapActivity::class.java)) },
                    onSignUpButtonClicked = { navController.navigate(SeneParkingScreen.SignUp.name) },
                    onForgotPasswordClicked = { navController.navigate(SeneParkingScreen.ForgotPassword.name) },
                    onParkingLotOwnerButtonClicked = { navController.navigate(SeneParkingScreen.ParkingLotOwnerSignUp.name) } // Add navigation for parking lot owner
                )
            }
            composable(route = SeneParkingScreen.SignUp.name) {
                SignUpScreen(
                    modifier = Modifier.padding(innerPadding),
                    onSignUpButtonClicked = { navController.navigate(SeneParkingScreen.Start.name) }
                )
            }
            composable(route = SeneParkingScreen.Map.name) {
                MapScreen()
            }
            composable(route = SeneParkingScreen.ForgotPassword.name) {
                ForgotPasswordScreen(onBackButtonClicked = { navController.popBackStack() })
            }
            composable(route = SeneParkingScreen.ParkingLotOwnerSignUp.name) { // Define the route for ParkingLotOwnerSignUpScreen
                ParkingLotOwnerSignUpScreen(
                    modifier = Modifier.padding(innerPadding),
                    onRegisterButtonClicked = { navController.popBackStack() } // Return to previous screen on registration
                )
            }
        }
    }
}
