package com.example.clinicaveterinariaapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaReservas(vm: VistaModeloReserva, innerPadding: PaddingValues) {
    var menuEspecialista by remember { mutableStateOf(false) }
    val especialistas = RepositorioProfesionales.obtenerNombres()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding) // Se aplica el padding del Scaffold principal
            .verticalScroll(rememberScrollState())
            .padding(16.dp) // Padding interno del contenido
    ) {
        Text("Nueva reserva", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = vm.nombreMascota,
            onValueChange = { vm.alCambiarNombreMascota(it) },
            label = { Text("Nombre mascota") },
            modifier = Modifier.fillMaxWidth(),
            isError = vm.errorNombreMascota != null,
            leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) }
        )
        if (vm.errorNombreMascota != null)
            Text(vm.errorNombreMascota ?: "", color = MaterialTheme.colorScheme.error)

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = vm.nombrePropietario,
            onValueChange = { vm.alCambiarNombrePropietario(it) },
            label = { Text("Nombre propietario") },
            modifier = Modifier.fillMaxWidth(),
            isError = vm.errorNombrePropietario != null
        )
        if (vm.errorNombrePropietario != null)
            Text(vm.errorNombrePropietario ?: "", color = MaterialTheme.colorScheme.error)

        Spacer(Modifier.height(8.dp))
        // ... (resto del formulario y la lista, sin cambios)
    }
}

@Preview(showBackground = true)
@Composable
fun VistaPreviaReservas() {
    // La preview ahora no tendrá la barra superior, lo cual es esperado.
    PantallaReservas(vm = VistaModeloReserva(), innerPadding = PaddingValues())
}
