package com.example.clinicaveterinariaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigation()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            val loginVm = VistaModeloInicioSesion()
            LoginScreen(
                vm = loginVm,
                navController = navController
            )
        }
        composable("registro") { // Nueva ruta
            val userVm = VistaModeloUsuarios()
            PantallaRegistro(navController = navController, vm = userVm)
        }
        composable("menu") {
            PantallaMenu(navController = navController)
        }
        composable("reservas") {
            Scaffold(
                topBar = { TopAppBar(title = { Text("Reservas") }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver") } }) }
            ) { innerPadding ->
                val resVm = VistaModeloReserva()
                PantallaReservas(vm = resVm, innerPadding = innerPadding)
            }
        }
        composable("usuarios") {
            Scaffold(
                topBar = { TopAppBar(title = { Text("Usuarios") }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver") } }) }
            ) { innerPadding ->
                val userVm = VistaModeloUsuarios()
                PantallaUsuarios(vm = userVm, innerPadding = innerPadding)
            }
        }
        composable("profesionales") {
            Scaffold(
                topBar = { TopAppBar(title = { Text("Profesionales") }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver") } }) }
            ) { innerPadding ->
                val profVm = VistaModeloProfesionales()
                PantallaProfesionales(vm = profVm, innerPadding = innerPadding)
            }
        }
    }
}
