package com.example.clinicaveterinariaapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaReservas(vm: VistaModeloReserva) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Obtener nombres de especialistas dinámicamente desde el repositorio (no recordar)
    val especialistas = RepositorioProfesionales.obtenerNombres()
    var menuEspecialista by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Reservas - Veterinaria") }) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(padding)) {

            Text(text = "Nueva reserva", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = vm.nombreMascota,
                onValueChange = { vm.alCambiarNombreMascota(it) },
                label = { Text("Nombre mascota") },
                modifier = Modifier.fillMaxWidth(),
                isError = vm.errorNombreMascota != null,
                leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) }
            )
            if (vm.errorNombreMascota != null) Text(vm.errorNombreMascota ?: "", color = MaterialTheme.colorScheme.error)

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = vm.nombrePropietario,
                onValueChange = { vm.alCambiarNombrePropietario(it) },
                label = { Text("Nombre propietario") },
                modifier = Modifier.fillMaxWidth(),
                isError = vm.errorNombrePropietario != null
            )
            if (vm.errorNombrePropietario != null) Text(vm.errorNombrePropietario ?: "", color = MaterialTheme.colorScheme.error)

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = vm.contacto,
                onValueChange = { vm.alCambiarContacto(it) },
                label = { Text("Contacto (teléfono)") },
                modifier = Modifier.fillMaxWidth(),
                isError = vm.errorContacto != null
            )
            if (vm.errorContacto != null) Text(vm.errorContacto ?: "", color = MaterialTheme.colorScheme.error)

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = vm.fecha,
                    onValueChange = { vm.alCambiarFecha(it) },
                    label = { Text("Fecha (YYYY-MM-DD)") },
                    modifier = Modifier.weight(1f),
                    isError = vm.errorFecha != null
                )

                OutlinedTextField(
                    value = vm.hora,
                    onValueChange = { vm.alCambiarHora(it) },
                    label = { Text("Hora (HH:mm)") },
                    modifier = Modifier.weight(1f),
                    isError = vm.errorHora != null
                )
            }
            if (vm.errorFecha != null) Text(vm.errorFecha ?: "", color = MaterialTheme.colorScheme.error)
            if (vm.errorHora != null) Text(vm.errorHora ?: "", color = MaterialTheme.colorScheme.error)

            Spacer(modifier = Modifier.height(8.dp))

            Box {
                OutlinedTextField(
                    value = vm.especialista,
                    onValueChange = { vm.alCambiarEspecialista(it) },
                    label = { Text("Especialista") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { menuEspecialista = true }) {
                            Icon(Icons.Filled.Edit, contentDescription = null)
                        }
                    },
                    isError = vm.errorEspecialista != null
                )
                DropdownMenu(expanded = menuEspecialista, onDismissRequest = { menuEspecialista = false }) {
                    especialistas.forEach { s ->
                        DropdownMenuItem(text = { Text(s) }, onClick = { vm.alCambiarEspecialista(s); menuEspecialista = false })
                    }
                }
            }
            if (vm.errorEspecialista != null) Text(vm.errorEspecialista ?: "", color = MaterialTheme.colorScheme.error)

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = vm.notas,
                onValueChange = { vm.alCambiarNotas(it) },
                label = { Text("Notas (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = {
                    val sdfFecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val sdfHora = SimpleDateFormat("HH:mm", Locale.getDefault())
                    val ahora = Date()
                    vm.alCambiarFecha(sdfFecha.format(ahora))
                    vm.alCambiarHora(sdfHora.format(ahora))
                }, enabled = !vm.estaCargando) { Text("Hoy ahora") }

                Button(onClick = {
                    vm.crearReserva { exito, mensaje ->
                        scope.launch { snackbarHostState.showSnackbar(mensaje ?: if (exito) "Reserva creada" else "Error") }
                    }
                }, enabled = !vm.estaCargando) {
                    if (vm.estaCargando) CircularProgressIndicator(modifier = Modifier.size(18.dp))
                    Text("Reservar", modifier = Modifier.padding(start = 6.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Historial de reservas", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            if (vm.reservas.isEmpty()) {
                Text(text = "No hay reservas aún.")
            } else {
                LazyColumn(modifier = Modifier.fillMaxHeight()) {
                    items(vm.reservas) { r ->
                        Card(modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                        ) {
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = "${r.nombreMascota} - ${r.nombrePropietario}", style = MaterialTheme.typography.bodyLarge)
                                    Text(text = "${r.fecha} ${r.hora} - ${r.especialista}", style = MaterialTheme.typography.bodySmall)
                                    Text(text = "Estado: ${r.estado}", style = MaterialTheme.typography.bodySmall)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    if (r.estado == EstadoReserva.AGENDADA) {
                                        TextButton(onClick = {
                                            vm.cancelarReserva(r.id) { exito, mensaje ->
                                                scope.launch { snackbarHostState.showSnackbar(mensaje ?: if (exito) "Reserva cancelada" else "Error") }
                                            }
                                        }) { Text("Cancelar") }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VistaPreviaReservas() {
    PantallaReservas(vm = VistaModeloReserva())
}
