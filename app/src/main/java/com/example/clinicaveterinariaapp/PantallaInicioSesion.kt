package com.example.clinicaveterinariaapp

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
<<<<<<< HEAD
import androidx.compose.ui.graphics.Color
=======
>>>>>>> 503dbab95bbce6299bf372f0c3784ba91e0abad6
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

<<<<<<< HEAD
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.clinicaveterinariaapp.ui.theme.AppBackground
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextFieldDefaults

=======
>>>>>>> 503dbab95bbce6299bf372f0c3784ba91e0abad6
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(vm: VistaModeloInicioSesion, navController: NavController) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

<<<<<<< HEAD
    // Hacemos el fondo totalmente visible y menos difuminado
    AppBackground(alpha = 1f, blurDp = 20.dp) {
        Scaffold(
            containerColor = Color.Transparent,
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { padding ->
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
                    leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) },
                )
                if (vm.errorCorreo != null) {
                    Text(
                        text = vm.errorCorreo ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.align(Alignment.Start).padding(top = 4.dp)
                    )
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
                    leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                )
                if (vm.errorContrasena != null) {
                    Text(
                        text = vm.errorContrasena ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.align(Alignment.Start).padding(top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        vm.iniciarSesion { success, message ->
                            scope.launch {
                                snackbarHostState.showSnackbar(message ?: if (success) "Inicio de sesión correcto" else "Error")
                            }
                            if (success) {
                                navController.navigate("menu") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !vm.estaCargando,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF117373), // semitransparente para ver fondo
                        contentColor = Color.White
                    )
                ) {
                    if (vm.estaCargando) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp).padding(end = 8.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    Text("Iniciar sesión")
                }

                Spacer(modifier = Modifier.height(12.dp))
                TextButton(
                    onClick = { navController.navigate("registro") },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(text = "¿No tienes cuenta? Regístrate", color = MaterialTheme.colorScheme.primary)
                }
=======
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
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
            if (vm.errorCorreo != null) {
                Text(
                    text = vm.errorCorreo ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.align(Alignment.Start).padding(top = 4.dp)
                )
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
                Text(
                    text = vm.errorContrasena ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.align(Alignment.Start).padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    vm.iniciarSesion { success, message ->
                        scope.launch {
                            snackbarHostState.showSnackbar(message ?: if (success) "Inicio de sesión correcto" else "Error")
                        }
                        if (success) {
                            navController.navigate("menu") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !vm.estaCargando
            ) {
                if (vm.estaCargando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp).padding(end = 8.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Text("Iniciar sesión")
            }

            Spacer(modifier = Modifier.height(12.dp))
            TextButton(
                onClick = { navController.navigate("registro") },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "¿No tienes cuenta? Regístrate", color = MaterialTheme.colorScheme.primary)
>>>>>>> 503dbab95bbce6299bf372f0c3784ba91e0abad6
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
<<<<<<< HEAD
@Preview(showBackground = false)
@Composable
fun PreviewLoginScreen() {
    val navController = rememberNavController()
    val vm: VistaModeloInicioSesion = viewModel()
    LoginScreen(vm = vm, navController = navController)
=======
@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    val navController = rememberNavController()
    LoginScreen(vm = VistaModeloInicioSesion(), navController = navController)
>>>>>>> 503dbab95bbce6299bf372f0c3784ba91e0abad6
}
