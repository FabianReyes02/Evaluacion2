package com.example.clinicaveterinariaapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaUsuarios(vm: VistaModeloUsuarios) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(topBar = { TopAppBar(title = { Text("Usuarios") }) }, snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(padding)) {

            Text(text = "Crear usuario", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(value = vm.correo, onValueChange = { vm.alCambiarCorreo(it) }, label = { Text("Correo") }, modifier = Modifier.fillMaxWidth(), singleLine = true, leadingIcon = { Icon(Icons.Filled.Email, null) })
            if (vm.errorCorreo != null) Text(vm.errorCorreo ?: "", color = MaterialTheme.colorScheme.error)

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = vm.contrasena, onValueChange = { vm.alCambiarContrasena(it) }, label = { Text("Contraseña") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            if (vm.errorContrasena != null) Text(vm.errorContrasena ?: "", color = MaterialTheme.colorScheme.error)

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = vm.nombre, onValueChange = { vm.alCambiarNombre(it) }, label = { Text("Nombre (opcional)") }, modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { vm.crearUsuario { exito, msg -> scope.launch { snackbarHostState.showSnackbar(msg ?: if (exito) "Usuario creado" else "Error") } } }, enabled = !vm.estaCargando) { Text("Crear") }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Usuarios registrados", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            if (vm.lista.isEmpty()) Text("No hay usuarios") else {
                LazyColumn { items(vm.lista) { u ->
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = u.correo, style = MaterialTheme.typography.bodyLarge)
                                Text(text = u.nombre ?: "-", style = MaterialTheme.typography.bodySmall)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                TextButton(onClick = { vm.cargarParaEdicion(u) }) { Text("Editar") }
                                TextButton(onClick = { vm.eliminarUsuario(u.correo) { exito, msg -> scope.launch { snackbarHostState.showSnackbar(msg ?: if (exito) "Eliminado" else "Error") } } }) { Text("Eliminar") }
                            }
                        }
                    }
                } }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VistaPreviaUsuarios() { PantallaUsuarios(vm = VistaModeloUsuarios()) }

