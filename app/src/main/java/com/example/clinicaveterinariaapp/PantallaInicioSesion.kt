package com.example.clinicaveterinariaapp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import kotlinx.coroutines.launch

@Composable
fun PantallaInicioSesion(vm: VistaModeloInicioSesion, onInicioExitoso: (Boolean) -> Unit = {}) {
    val snackbarHostState = androidx.compose.runtime.remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Usuario por defecto (para pruebas)
    val usuarioPorDefecto = RepositorioUsuarios.obtenerUsuarioPorDefecto()

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Bienvenido a VetApp 🐾", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = vm.correo,
                onValueChange = { vm.alCambiarCorreo(it) },
                label = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = vm.errorCorreo != null,
                leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) }
            )
            if (vm.errorCorreo != null) Text(text = vm.errorCorreo ?: "", color = MaterialTheme.colorScheme.error)

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
            if (vm.errorContrasena != null) Text(text = vm.errorContrasena ?: "", color = MaterialTheme.colorScheme.error)

            Spacer(modifier = Modifier.height(8.dp))

            // Mostrar credenciales por defecto y botón para autocompletar
            Text(text = "Usuario por defecto: ${usuarioPorDefecto.correo} / ${usuarioPorDefecto.contrasena}", style = MaterialTheme.typography.bodySmall)
            TextButton(onClick = {
                vm.alCambiarCorreo(usuarioPorDefecto.correo)
                vm.alCambiarContrasena(usuarioPorDefecto.contrasena)
            }) {
                Text("Autocompletar usuario de prueba")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                vm.iniciarSesion { exito, mensaje ->
                    scope.launch { snackbarHostState.showSnackbar(mensaje ?: if (exito) "Inicio correcto" else "Error") }
                    if (exito) onInicioExitoso(true)
                }
            }, modifier = Modifier.fillMaxWidth(), enabled = !vm.estaCargando) {
                if (vm.estaCargando) CircularProgressIndicator(modifier = Modifier.size(18.dp))
                Text("Iniciar sesión")
            }

            Spacer(modifier = Modifier.height(12.dp))
            TextButton(onClick = { /* lógica registro */ }) { Text("¿No tienes cuenta? Regístrate") }
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun VistaPrevioInicioSesion() {
    PantallaInicioSesion(vm = VistaModeloInicioSesion())
}
