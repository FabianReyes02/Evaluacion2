package com.example.clinicaveterinariaapp.pantallas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.clinicaveterinariaapp.ui.theme.ClinicaVeterinariaAppTheme
import com.example.clinicaveterinariaapp.navegacion.AppNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClinicaVeterinariaAppTheme {
                AppNavigation()
            }
        }
    }
}
