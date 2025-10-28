package com.example.clinicaveterinariaapp

import android.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.clinicaveterinariaapp.ui.theme.AppBackground
import com.example.clinicaveterinariaapp.ui.theme.ClinicaVeterinariaAppTheme
import androidx.compose.ui.graphics.Color as UiColor

@Composable
fun PantallaMenu(
    navController: NavController
) {
    AppBackground(alpha = 1f, blurDp = 20.dp) {

        // Contenido encima de la imagen
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp, end = 16.dp, top = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Menú Principal🐾", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(230.dp))

            Button(onClick = { navController.navigate("reservas") },
                modifier = Modifier.fillMaxWidth().height(55.dp),
                colors = ButtonDefaults.buttonColors(containerColor = androidx.compose.ui.graphics.Color(0xFF117373))
                ){
                Text("✏\uFE0F Agendar o Ver Citas", fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(40.dp))

            Button(onClick = { navController.navigate("usuarios") },
                modifier = Modifier.fillMaxWidth().height(55.dp),
                colors = ButtonDefaults.buttonColors(containerColor = androidx.compose.ui.graphics.Color(0xFF117373))
            ){
                Text("\uD83E\uDDD1 Ver Usuarios", fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(40.dp))

            Button(onClick = { navController.navigate("profesionales") },
                modifier = Modifier.fillMaxWidth().height(55.dp),
                colors = ButtonDefaults.buttonColors(containerColor = androidx.compose.ui.graphics.Color(0xFF117373))
            ){
                Text("\uD83D\uDC68\u200D⚕\uFE0F Ver Profesionales", fontSize = 16.sp)

            }

            Spacer(modifier = Modifier.weight(1f)) // Empuja el botón de logout al fondo

            Button(onClick = {
                navController.navigate("login") {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                }
            }, modifier = Modifier.fillMaxWidth().padding(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = androidx.compose.ui.graphics.Color(0xFF117373))
            ) {
                Text("Cerrar Sesión")
            }
        }
    }
}

// Preview agregado para ver el Menu en Android Studio
@Preview(showBackground = true)
@Composable
fun PreviewPantallaMenu() {
    val navController = rememberNavController()
    ClinicaVeterinariaAppTheme {
        PantallaMenu(navController = navController)
    }
}
