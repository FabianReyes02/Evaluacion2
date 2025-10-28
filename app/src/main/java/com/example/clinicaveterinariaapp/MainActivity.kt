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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import androidx.compose.ui.unit.dp

import com.example.clinicaveterinariaapp.ui.theme.ClinicaVeterinariaAppTheme
import com.example.clinicaveterinariaapp.ui.theme.AppBackground

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClinicaVeterinariaAppTheme {
                // Ajusta el valor de blurDp aquí (ej: 4.dp, 8.dp, 16.dp)
                AppBackground(blurDp = 8.dp) {
                    AppNavigation()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            val loginVm: VistaModeloInicioSesion = viewModel()
            LoginScreen(
                vm = loginVm,
                navController = navController
            )
        }
        composable("registro") {
            val userVm: VistaModeloUsuarios = viewModel()
            PantallaRegistro(navController = navController, vm = userVm)
        }
        composable("menu") {
            PantallaMenu(navController = navController)
        }
        composable("reservas") {
            val snackbarHostState = remember { SnackbarHostState() }
            val scope = rememberCoroutineScope()
            Scaffold(
                topBar = { TopAppBar(title = { Text("Reservas") }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver") } }) },
                snackbarHost = { SnackbarHost(snackbarHostState) } // SnackbarHost añadido
            ) { innerPadding ->
                val resVm: VistaModeloReserva = viewModel()
                PantallaReservas(
                    vm = resVm, 
                    innerPadding = innerPadding,
                    onShowSnackbar = { mensaje -> // Lógica para mostrar el snackbar
                        scope.launch {
                            snackbarHostState.showSnackbar(mensaje)
                        }
                    }
                )
            }
        }
        composable("usuarios") {
             val snackbarHostState = remember { SnackbarHostState() }
            Scaffold(
                topBar = { TopAppBar(title = { Text("Usuarios") }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver") } }) },
                 snackbarHost = { SnackbarHost(snackbarHostState) }
            ) { innerPadding ->
                val userVm: VistaModeloUsuarios = viewModel()
                // Aquí habría que adaptar PantallaUsuarios para recibir onShowSnackbar si se quiere unificar
                PantallaUsuarios(vm = userVm, innerPadding = innerPadding)
            }
        }
        composable("profesionales") {
             val snackbarHostState = remember { SnackbarHostState() }
            Scaffold(
                topBar = { TopAppBar(title = { Text("Profesionales") }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver") } }) },
                 snackbarHost = { SnackbarHost(snackbarHostState) }
            ) { innerPadding ->
                val profVm: VistaModeloProfesionales = viewModel()
                 // Aquí habría que adaptar PantallaProfesionales para recibir onShowSnackbar si se quiere unificar
                PantallaProfesionales(vm = profVm, innerPadding = innerPadding)
            }
        }
    }
}
