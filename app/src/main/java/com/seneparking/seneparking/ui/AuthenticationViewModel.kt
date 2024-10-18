package com.seneparking.seneparking.ui

import androidx.lifecycle.ViewModel
import com.seneparking.seneparking.ui.data.AuthenticationUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthenticationViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AuthenticationUiState(userId = ""))
    val uiState: StateFlow<AuthenticationUiState> = _uiState.asStateFlow()

}