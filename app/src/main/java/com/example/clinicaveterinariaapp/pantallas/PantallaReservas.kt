package com.example.clinicaveterinariaapp.pantallas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.clinicaveterinariaapp.datos.EstadoReserva
import com.example.clinicaveterinariaapp.datos.RepositorioProfesionales
import com.example.clinicaveterinariaapp.vista_modelo.VistaModeloReserva
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaReservas(vm: VistaModeloReserva, innerPadding: PaddingValues, onShowSnackbar: (String) -> Unit) {

    val especialistas = RepositorioProfesionales.obtenerNombres()
    var menuEspecialista by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("Nueva reserva", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = vm.nombreMascota,
            onValueChange = { vm.alCambiarNombreMascota(it) },
            label = { Text("Nombre mascota") },
            modifier = Modifier.fillMaxWidth(),
            isError = vm.errorNombreMascota != null,
            leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) },
            supportingText = { vm.errorNombreMascota?.let { Text(it, color = MaterialTheme.colorScheme.error) } }
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = vm.nombrePropietario,
            onValueChange = { vm.alCambiarNombrePropietario(it) },
            label = { Text("Nombre propietario") },
            modifier = Modifier.fillMaxWidth(),
            isError = vm.errorNombrePropietario != null,
            supportingText = { vm.errorNombrePropietario?.let { Text(it, color = MaterialTheme.colorScheme.error) } }
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = vm.contacto,
            onValueChange = { vm.alCambiarContacto(it) },
            label = { Text("Contacto (teléfono)") },
            modifier = Modifier.fillMaxWidth(),
            isError = vm.errorContacto != null,
            supportingText = { vm.errorContacto?.let { Text(it, color = MaterialTheme.colorScheme.error) } }
        )
        Spacer(Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = vm.fecha,
                onValueChange = { vm.alCambiarFecha(it) },
                label = { Text("Fecha (YYYY-MM-DD)") },
                modifier = Modifier.weight(1f),
                isError = vm.errorFecha != null,
                supportingText = { vm.errorFecha?.let { Text(it, color = MaterialTheme.colorScheme.error) } }
            )
            OutlinedTextField(
                value = vm.hora,
                onValueChange = { vm.alCambiarHora(it) },
                label = { Text("Hora (HH:mm)") },
                modifier = Modifier.weight(1f),
                isError = vm.errorHora != null,
                supportingText = { vm.errorHora?.let { Text(it, color = MaterialTheme.colorScheme.error) } }
            )
        }
        Spacer(Modifier.height(8.dp))

        Box {
            OutlinedTextField(
                value = vm.especialista,
                onValueChange = { vm.alCambiarEspecialista(it) },
                label = { Text("Especialista") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                trailingIcon = { IconButton(onClick = { menuEspecialista = true }) { Icon(Icons.Filled.Edit, "Seleccionar") } },
                isError = vm.errorEspecialista != null,
                supportingText = { vm.errorEspecialista?.let { Text(it, color = MaterialTheme.colorScheme.error) } }
            )
            DropdownMenu(expanded = menuEspecialista, onDismissRequest = { menuEspecialista = false }) {
                especialistas.forEach { s ->
                    DropdownMenuItem(text = { Text(s) }, onClick = { vm.alCambiarEspecialista(s); menuEspecialista = false })
                }
            }
        }
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = vm.notas,
            onValueChange = { vm.alCambiarNotas(it) },
            label = { Text("Notas (opcional)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            OutlinedButton(onClick = {
                val sdfFecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val sdfHora = SimpleDateFormat("HH:mm", Locale.getDefault())
                val ahora = Date()
                vm.alCambiarFecha(sdfFecha.format(ahora))
                vm.alCambiarHora(sdfHora.format(ahora))
            }, enabled = !vm.estaCargando, modifier = Modifier.weight(1f)) {
                Text("Hoy y ahora")
            }
            Button(onClick = { vm.crearReserva { exito, msg -> onShowSnackbar(msg ?: "Error") } }, enabled = !vm.estaCargando, modifier = Modifier.weight(1f)) {
                if (vm.estaCargando) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary, strokeWidth = 2.dp)
                } else {
                    Text("Reservar")
                }
            }
        }
        Spacer(Modifier.height(24.dp))

        Text("Historial de reservas", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))

        if (vm.reservas.isEmpty()) {
            Text("No hay reservas aún.")
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                vm.reservas.forEach { r ->
                    Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(2.dp)) {
                        Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("${r.nombreMascota} - ${r.nombrePropietario}", style = MaterialTheme.typography.bodyLarge)
                                Text("${r.fecha} ${r.hora} - ${r.especialista}", style = MaterialTheme.typography.bodySmall)
                                Text("Estado: ${r.estado}", style = MaterialTheme.typography.bodySmall, color = when (r.estado) {
                                    EstadoReserva.AGENDADA -> MaterialTheme.colorScheme.primary
                                    EstadoReserva.CANCELADA -> MaterialTheme.colorScheme.error
                                    EstadoReserva.CUMPLIDA -> MaterialTheme.colorScheme.secondary
                                })
                            }
                            if (r.estado == EstadoReserva.AGENDADA) {
                                TextButton(onClick = { vm.cancelarReserva(r.id) { _, msg -> onShowSnackbar(msg ?: "Error") } }) { Text("Cancelar") }
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
    PantallaReservas(vm = VistaModeloReserva(), innerPadding = PaddingValues(0.dp), onShowSnackbar = {})
}
