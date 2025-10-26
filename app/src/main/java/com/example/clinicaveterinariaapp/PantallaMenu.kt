
package com.example.clinicaveterinariaapp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun PantallaMenu(
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Menú Principal", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = { navController.navigate("reservas") }, modifier = Modifier.fillMaxWidth()) {
            Text("Agendar / Ver Citas")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.navigate("usuarios") }, modifier = Modifier.fillMaxWidth()) {
            Text("Ver Usuarios")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.navigate("profesionales") }, modifier = Modifier.fillMaxWidth()) {
            Text("Ver Profesionales")
        }

        Spacer(modifier = Modifier.weight(1f)) // Empuja el botón de logout al fondo

        Button(onClick = {
            navController.navigate("login") {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Cerrar Sesión")
        }
    }
}
