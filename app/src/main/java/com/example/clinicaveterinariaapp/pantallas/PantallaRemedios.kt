package com.example.clinicaveterinariaapp.pantallas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.clinicaveterinariaapp.vista_modelo.VistaModeloRemedios
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaRemedios(innerPadding: PaddingValues) {
    val vm: VistaModeloRemedios = viewModel()
    val loading by vm.estaCargando
    val error by vm.errorMsg
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var dosis by remember { mutableStateOf("") }
    var presentacion by remember { mutableStateOf("") }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .padding(16.dp)
    ) {
        LaunchedEffect(Unit) { vm.cargarRemedios() }
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp)
        ) {
            LaunchedEffect(Unit) { vm.cargarRemedios() }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { vm.cargarRemedios() }) { Text("Refrescar") }
            }
            Spacer(modifier = Modifier.height(12.dp))

            // Formulario de creación
            Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(2.dp)) {
                Column(modifier = Modifier.padding(12.dp)) {
                    OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = descripcion, onValueChange = { descripcion = it }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = dosis, onValueChange = { dosis = it }, label = { Text("Dosis") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = presentacion, onValueChange = { presentacion = it }, label = { Text("Presentación") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = {
                            vm.crearRemedioRemoto(nombre.trim(), descripcion.trim().ifEmpty { null }, dosis.trim().ifEmpty { null }, presentacion.trim().ifEmpty { null }) { exito, msg ->
                                scope.launch { snackbarHostState.showSnackbar(msg ?: if (exito) "Remedio creado" else "Error") }
                                if (exito) {
                                    nombre = ""; descripcion = ""; dosis = ""; presentacion = ""
                                }
                            }
                        }, enabled = !loading) { Text("Crear (API)") }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (loading) { CircularProgressIndicator() }
            error?.let { Text("Error: $it", color = MaterialTheme.colorScheme.error) }

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(vm.remedios) { r ->
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Row(modifier = Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                Text(r.nombre, style = MaterialTheme.typography.bodyLarge)
                                r.descripcion?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
                            }
                            Column {
                                TextButton(onClick = { vm.eliminarRemedioRemoto(r.id ?: "") { exito, msg -> scope.launch { snackbarHostState.showSnackbar(msg ?: if (exito) "Eliminado" else "Error") } } }) { Text("Eliminar") }
                            }
                        }
                    }
                }
            }
        }

        SnackbarHost(hostState = snackbarHostState)
    }
}
