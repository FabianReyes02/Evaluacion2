package com.example.clinicaveterinariaapp.pantallas

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.clinicaveterinariaapp.datos.Usuario
import com.example.clinicaveterinariaapp.vista_modelo.VistaModeloUsuarios

@Composable
fun PantallaUsuarios(vm: VistaModeloUsuarios, innerPadding: PaddingValues) {
    // Se accede directamente a la lista, ya que es una lista de estado observable
    val usuarios = vm.lista

    LazyColumn(
        contentPadding = innerPadding,
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(usuarios) { usuario: Usuario ->
            Card(
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(usuario.nombre ?: "Sin nombre", style = MaterialTheme.typography.bodyLarge)
                    Text(usuario.correo, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
