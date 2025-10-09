package com.example.clinicaveterinariaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.runtime.* // <-- Esto es importante
import androidx.compose.ui.text.input.PasswordVisualTransformation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Llamamos a la función que crea la pantalla
            LoginScreen()
        }
    }
}

// Composición de la pantalla de Login
@Composable
fun LoginScreen() {
    // Column es un contenedor que alinea los elementos verticalmente
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Título de la pantalla
        Text(text = "Bienvenido a VetApp \uD83D\uDC3E", style = MaterialTheme.typography.headlineMedium)

        // Espacio entre los elementos
        Spacer(modifier = Modifier.height(32.dp))

        // Campo de texto para el correo electrónico
        var emailState = remember { mutableStateOf(TextFieldValue()) }
        TextField(
            value = emailState.value,
            onValueChange = { emailState.value = it },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // Campo de texto para la contraseña
        var passwordState = remember { mutableStateOf(TextFieldValue()) }
        TextField(
            value = passwordState.value,
            onValueChange = { passwordState.value = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )

        // Espacio entre los campos y el botón
        Spacer(modifier = Modifier.height(16.dp))

        // Botón de inicio de sesión
        Button(
            onClick = { /* Aquí va la lógica para iniciar sesión */ },
            modifier = Modifier.fillMaxWidth()
        ) {
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

// Preview para ver cómo se verá en el diseño
@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen()
}
//avance00000000