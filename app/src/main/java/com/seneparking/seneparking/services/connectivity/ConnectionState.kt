package com.seneparking.seneparking.services.connectivity

sealed class ConnectionState {
    object Available : ConnectionState()
    object Unavailable : ConnectionState()
}