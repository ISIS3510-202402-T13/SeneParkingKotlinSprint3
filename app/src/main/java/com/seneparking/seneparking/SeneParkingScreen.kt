package com.seneparking.seneparking

import android.content.Intent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.navigation.compose.NavHost
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.seneparking.seneparking.ui.AuthenticationViewModel
import com.seneparking.seneparking.ui.LogInScreen
import com.seneparking.seneparking.ui.SignUpScreen
import com.seneparking.seneparking.ui.MapScreen
import dagger.hilt.android.HiltAndroidApp


/**
 * enum values that represent the screens in the app
 */
enum class SeneParkingScreen(@StringRes val title: Int) {
    Start(title = R.string.seneparking),
    SignUp(title = R.string.sign_up),
    Map(title = R.string.map)
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
            modifier = Modifier
                .fillMaxSize()  // Remove verticalScroll
        ) {
            composable(route = SeneParkingScreen.Start.name) {
                LogInScreen(
                    modifier = Modifier.padding(innerPadding),  // Apply padding here
                    onLoginButtonClicked = { context.startActivity(Intent(context, MapActivity::class.java)) },
                    onSignUpButtonClicked = { navController.navigate(SeneParkingScreen.SignUp.name) }
                )
            }
            composable(route = SeneParkingScreen.SignUp.name) {
                SignUpScreen(
                    modifier = Modifier.padding(innerPadding),  // Apply padding here
                    onSignUpButtonClicked = { navController.navigate(SeneParkingScreen.Start.name) }
                )
            }
            composable(route = SeneParkingScreen.Map.name) {
                MapScreen()  // No padding here to make the map fill the entire screen
            }
        }
    }
}


