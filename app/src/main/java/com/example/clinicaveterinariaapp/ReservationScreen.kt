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
fun ReservationScreen(vm: ReservationViewModel) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val specialists = listOf("General", "Cirugía", "Vacunación", "Dermatología")
    var specialistMenuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Reservar hora - Veterinaria") })
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(padding)
        ) {
            // Formulario
            Text(text = "Nueva reserva", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = vm.petName,
                onValueChange = { vm.onPetNameChange(it) },
                label = { Text("Nombre mascota") },
                modifier = Modifier.fillMaxWidth(),
                isError = vm.petNameError != null,
                leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) }
            )
            if (vm.petNameError != null) Text(vm.petNameError ?: "", color = MaterialTheme.colorScheme.error)

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = vm.ownerName,
                onValueChange = { vm.onOwnerNameChange(it) },
                label = { Text("Nombre propietario") },
                modifier = Modifier.fillMaxWidth(),
                isError = vm.ownerNameError != null
            )
            if (vm.ownerNameError != null) Text(vm.ownerNameError ?: "", color = MaterialTheme.colorScheme.error)

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = vm.contact,
                onValueChange = { vm.onContactChange(it) },
                label = { Text("Contacto (teléfono)") },
                modifier = Modifier.fillMaxWidth(),
                isError = vm.contactError != null
            )
            if (vm.contactError != null) Text(vm.contactError ?: "", color = MaterialTheme.colorScheme.error)

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = vm.date,
                    onValueChange = { vm.onDateChange(it) },
                    label = { Text("Fecha (YYYY-MM-DD)") },
                    modifier = Modifier.weight(1f),
                    isError = vm.dateError != null
                )

                OutlinedTextField(
                    value = vm.time,
                    onValueChange = { vm.onTimeChange(it) },
                    label = { Text("Hora (HH:mm)") },
                    modifier = Modifier.weight(1f),
                    isError = vm.timeError != null
                )
            }
            if (vm.dateError != null) Text(vm.dateError ?: "", color = MaterialTheme.colorScheme.error)
            if (vm.timeError != null) Text(vm.timeError ?: "", color = MaterialTheme.colorScheme.error)

            Spacer(modifier = Modifier.height(8.dp))

            // Specialist dropdown
            Box {
                OutlinedTextField(
                    value = vm.specialist,
                    onValueChange = { vm.onSpecialistChange(it) },
                    label = { Text("Especialista") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { specialistMenuExpanded = true }) {
                            Icon(Icons.Filled.Edit, contentDescription = null)
                        }
                    },
                    isError = vm.specialistError != null
                )
                DropdownMenu(expanded = specialistMenuExpanded, onDismissRequest = { specialistMenuExpanded = false }) {
                    specialists.forEach { s ->
                        DropdownMenuItem(text = { Text(s) }, onClick = { vm.onSpecialistChange(s); specialistMenuExpanded = false })
                    }
                }
            }
            if (vm.specialistError != null) Text(vm.specialistError ?: "", color = MaterialTheme.colorScheme.error)

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = vm.notes,
                onValueChange = { vm.onNotesChange(it) },
                label = { Text("Notas (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = {
                    // rellenar fecha/hora rápida usando SimpleDateFormat para compatibilidad
                    val sdfDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val sdfTime = SimpleDateFormat("HH:mm", Locale.getDefault())
                    val now = Date()
                    vm.onDateChange(sdfDate.format(now))
                    vm.onTimeChange(sdfTime.format(now))
                }, enabled = !vm.isLoading) {
                    Text("Hoy ahora")
                }

                Button(onClick = {
                    vm.createReservation { success, message ->
                        scope.launch {
                            snackbarHostState.showSnackbar(message ?: if (success) "Reserva creada" else "Error")
                        }
                    }
                }, enabled = !vm.isLoading) {
                    if (vm.isLoading) CircularProgressIndicator(modifier = Modifier.size(18.dp))
                    Text("Reservar", modifier = Modifier.padding(start = 6.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Historial de reservas", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            if (vm.reservations.isEmpty()) {
                Text(text = "No hay reservas aún.")
            } else {
                LazyColumn(modifier = Modifier.fillMaxHeight()) {
                    items(vm.reservations) { r ->
                        Card(modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                        ) {
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = "${r.petName} - ${r.ownerName}", style = MaterialTheme.typography.bodyLarge)
                                    Text(text = "${r.date} ${r.time} - ${r.specialist}", style = MaterialTheme.typography.bodySmall)
                                    Text(text = "Estado: ${r.status}", style = MaterialTheme.typography.bodySmall)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    if (r.status == ReservationStatus.SCHEDULED) {
                                        TextButton(onClick = {
                                            vm.cancelReservation(r.id) { success, message ->
                                                scope.launch { snackbarHostState.showSnackbar(message ?: if (success) "Reserva cancelada" else "Error") }
                                            }
                                        }) {
                                            Text("Cancelar")
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
}

@Preview(showBackground = true)
@Composable
fun PreviewReservationScreen() {
    ReservationScreen(vm = ReservationViewModel())
}
