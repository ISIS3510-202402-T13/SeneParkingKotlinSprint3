package com.seneparking.seneparking

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.google.android.gms.location.FusedLocationProviderClient
import com.seneparking.seneparking.ui.theme.SeneParkingTheme
import androidx.core.content.ContextCompat


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            SeneParkingTheme {
                SeneParkingApp()
            }
        }
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)
    }
}


