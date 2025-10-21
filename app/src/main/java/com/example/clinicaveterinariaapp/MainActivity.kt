package com.example.clinicaveterinariaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.* // <-- Esto es importante
import androidx.compose.ui.text.input.PasswordVisualTransformation
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Manejo simple de estado: mostrar login o pantalla de reservas
            val loginVm = LoginViewModel()
            var loggedIn by remember { mutableStateOf(false) }

            if (!loggedIn) {
                LoginScreen(vm = loginVm, onLoginSuccess = { success -> if (success) loggedIn = true })
            } else {
                val resVm = ReservationViewModel()
                ReservationScreen(vm = resVm)
            }
        }
    }
}

// Composición de la pantalla de Login (consume el ViewModel)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(vm: LoginViewModel, onLoginSuccess: (Boolean) -> Unit = {}) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        // Column es un contenedor que alinea los elementos verticalmente
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Título de la pantalla
            Text(text = "Bienvenido a VetApp \uD83D\uDC3E", style = MaterialTheme.typography.headlineMedium)

            // Espacio entre los elementos
            Spacer(modifier = Modifier.height(32.dp))

            // Campo de texto para el correo electrónico
            OutlinedTextField(
                value = vm.email,
                onValueChange = { vm.onEmailChange(it) },
                label = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = vm.emailError != null,
                leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) }
            )
            if (vm.emailError != null) {
                Text(
                    text = vm.emailError ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(top = 4.dp)
                )
            }

            // Campo de texto para la contraseña
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = vm.password,
                onValueChange = { vm.onPasswordChange(it) },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = vm.passwordError != null,
                visualTransformation = PasswordVisualTransformation(),
                leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) }
            )
            if (vm.passwordError != null) {
                Text(
                    text = vm.passwordError ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(top = 4.dp)
                )
            }

            // Espacio entre los campos y el botón
            Spacer(modifier = Modifier.height(16.dp))

            // Botón de inicio de sesión
            Button(
                onClick = {
                    vm.login { success, message ->
                        scope.launch {
                            snackbarHostState.showSnackbar(message ?: if (success) "Inicio de sesión correcto" else "Error")
                        }
                        if (success) onLoginSuccess(true)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !vm.isLoading
            ) {
                if (vm.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(18.dp)
                            .padding(end = 8.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Text("Iniciar sesión")
            }

            // Texto para ir al registro
            Spacer(modifier = Modifier.height(12.dp))
            TextButton(
                onClick = { /* Aquí va la lógica para registro */ },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "¿No tienes cuenta? Regístrate", color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

// Preview para ver cómo se verá en el diseño
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen(vm = LoginViewModel())
}
//avance00000000