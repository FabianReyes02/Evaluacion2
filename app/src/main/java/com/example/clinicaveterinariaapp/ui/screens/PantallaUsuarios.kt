package com.example.clinicaveterinariaapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.clinicaveterinariaapp.viewmodel.VistaModeloUsuarios

@Composable
fun PantallaUsuarios(vm: VistaModeloUsuarios, innerPadding: PaddingValues) {
    val usuarios by vm.lista.collectAsState()

    LazyColumn(
        contentPadding = innerPadding,
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(usuarios) {
            Card(elevation = CardDefaults.cardElevation(2.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(it.nombre ?: "Sin nombre", style = MaterialTheme.typography.bodyLarge)
                    Text(it.correo, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
