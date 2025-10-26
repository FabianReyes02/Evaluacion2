package com.example.clinicaveterinariaapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun PantallaProfesionales(vm: VistaModeloProfesionales, innerPadding: PaddingValues) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding) // Padding del Scaffold principal
            .padding(16.dp) // Padding del contenido
    ) {
        // Formulario para crear/editar profesional
        item {
            Column {
                Text(text = "Crear o Editar Profesional", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(value = vm.nombre, onValueChange = { vm.alCambiarNombre(it) }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth(), singleLine = true, leadingIcon = { Icon(Icons.Filled.Person, null) })
                if (vm.errorNombre != null) Text(vm.errorNombre ?: "", color = MaterialTheme.colorScheme.error)

                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = vm.especialidad, onValueChange = { vm.alCambiarEspecialidad(it) }, label = { Text("Especialidad") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                if (vm.errorEspecialidad != null) Text(vm.errorEspecialidad ?: "", color = MaterialTheme.colorScheme.error)

                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = vm.contacto, onValueChange = { vm.alCambiarContacto(it) }, label = { Text("Contacto (opcional)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)

                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = vm.descripcion, onValueChange = { vm.alCambiarDescripcion(it) }, label = { Text("Descripción (opcional)") }, modifier = Modifier.fillMaxWidth())

                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { vm.crearProfesional { exito, msg -> scope.launch { snackbarHostState.showSnackbar(msg ?: if (exito) "Profesional creado/actualizado" else "Error") } } }, enabled = !vm.estaCargando) { Text("Guardar") }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Divider()
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Lista de profesionales", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        // Lista de profesionales
        if (vm.lista.isEmpty()) {
            item {
                Text("No hay profesionales")
            }
        } else {
            items(vm.lista) { p ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = p.nombre, style = MaterialTheme.typography.bodyLarge)
                            Text(text = p.especialidad, style = MaterialTheme.typography.bodySmall)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            TextButton(onClick = { vm.cargarParaEdicion(p) }) { Text("Editar") }
                            TextButton(onClick = { vm.eliminarProfesional(p.id) { exito, msg -> scope.launch { snackbarHostState.showSnackbar(msg ?: if (exito) "Eliminado" else "Error") } } }) { Text("Eliminar") }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VistaPreviaProfesionales() {
    PantallaProfesionales(vm = VistaModeloProfesionales(), innerPadding = PaddingValues())
}
