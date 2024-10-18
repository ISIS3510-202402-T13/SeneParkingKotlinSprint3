package com.seneparking.seneparking

import androidx.annotation.StringRes
import androidx.navigation.compose.NavHost
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHost
import androidx.navigation.NavHostController


/**
 * enum values that represent the screens in the app
 */
enum class SeneParkingScreen(@StringRes val title: Int) {
    Start(title = 0),
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
        title = { /*TODO*/ },
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
fun SeneParkingApp( navController: NavHostController = rememberNavController()) {

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
        val uiState by viewModel.uiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = SeneParkingScreen.Start.name,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {
            composable(route = SeneParkingScreen.Start.name) {
                SignInScreen(
                    onLoginButtonClicked = { }
                )
            }
        }


    }
}

