package com.example.clinicaveterinariaapp.pantallas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.clinicaveterinariaapp.vista_modelo.VistaModeloApi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaApiDemo(navToHome: () -> Unit) {
    val vm: VistaModeloApi = viewModel()
    val loading by vm.estaCargando
    val error by vm.errorMsg

    Scaffold(
        topBar = { TopAppBar(title = { Text("API Demo") }, colors = TopAppBarDefaults.topAppBarColors()) }
    ) { innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { vm.cargarPosts() }) {
                    Text("Cargar posts")
                }
                TextButton(onClick = navToHome) { Text("Volver") }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (loading) {
                CircularProgressIndicator()
            }

            error?.let { Text("Error: $it", color = MaterialTheme.colorScheme.error) }

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(vm.posts) { p ->
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(p.title, style = MaterialTheme.typography.bodyLarge)
                        Text(p.body, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
