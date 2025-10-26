package com.example.clinicaveterinariaapp

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaRegistro(navController: NavController, vm: VistaModeloUsuarios) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Nueva Cuenta") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = vm.nombre,
                onValueChange = { vm.alCambiarNombre(it) },
                label = { Text("Nombre completo") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) }
            )
            if (vm.errorNombre != null) {
                Text(vm.errorNombre ?: "", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = vm.correo,
                onValueChange = { vm.alCambiarCorreo(it) },
                label = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = vm.errorCorreo != null,
                leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) }
            )
            if (vm.errorCorreo != null) {
                Text(vm.errorCorreo ?: "", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = vm.contrasena,
                onValueChange = { vm.alCambiarContrasena(it) },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = vm.errorContrasena != null,
                visualTransformation = PasswordVisualTransformation(),
                leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) }
            )
            if (vm.errorContrasena != null) {
                Text(vm.errorContrasena ?: "", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    vm.crearUsuario { exito, mensaje ->
                        scope.launch {
                            snackbarHostState.showSnackbar(mensaje ?: "")
                        }
                        if (exito) {
                            navController.popBackStack() // Volver al login
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !vm.estaCargando
            ) {
                if (vm.estaCargando) {
                    CircularProgressIndicator(modifier = Modifier.size(18.dp).padding(end = 8.dp))
                }
                Text("Registrarme")
            }
        }
    }
}
