package com.example.clinicaveterinariaapp

import android.os.Bundle
import androidx.activity.ComponentActivity

/**
 * Actividad mínima en español: se mantiene el archivo pero sin UI Compose compleja
 * para evitar el uso de APIs experimentales desde aquí. La actividad lanzadora será `MainActivity`.
 */
class ActividadPrincipal : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Intencionalmente no se establece contenido aquí.
    }
}
