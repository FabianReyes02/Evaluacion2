package com.example.clinicaveterinariaapp.navegacion

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.clinicaveterinariaapp.pantallas.PantallaInicioSesion
import com.example.clinicaveterinariaapp.pantallas.PantallaMenu
import com.example.clinicaveterinariaapp.pantallas.PantallaRegistro
import com.example.clinicaveterinariaapp.pantallas.PantallaReservas
import com.example.clinicaveterinariaapp.vista_modelo.VistaModeloInicioSesion
import com.example.clinicaveterinariaapp.vista_modelo.VistaModeloReserva
import com.example.clinicaveterinariaapp.vista_modelo.VistaModeloUsuarios
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "inicio_sesion") {
        composable("inicio_sesion") {
            val loginVm: VistaModeloInicioSesion = viewModel()
            PantallaInicioSesion(
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
                topBar = {
                    TopAppBar(
                        title = { Text("Reservas") },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            titleContentColor = MaterialTheme.colorScheme.primary,
                        )
                    )
                },
                snackbarHost = { SnackbarHost(snackbarHostState) }
            ) { innerPadding ->
                val resVm: VistaModeloReserva = viewModel()
                PantallaReservas(
                    vm = resVm,
                    innerPadding = innerPadding,
                    onShowSnackbar = { mensaje ->
                        scope.launch {
                            snackbarHostState.showSnackbar(mensaje)
                        }
                    }
                )
            }
        }
    }
}