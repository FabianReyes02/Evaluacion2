package com.example.clinicaveterinariaapp

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * ViewModel (en español) que contiene la lógica de validación y estado para el inicio de sesión.
 */
class VistaModeloInicioSesion : ViewModel() {
    var correo by mutableStateOf("")
        private set

    var contrasena by mutableStateOf("")
        private set

    var errorCorreo by mutableStateOf<String?>(null)
        private set

    var errorContrasena by mutableStateOf<String?>(null)
        private set

    var estaCargando by mutableStateOf(false)
        private set

    fun alCambiarCorreo(nuevo: String) {
        correo = nuevo
        if (errorCorreo != null) validarCorreo()
    }

    fun alCambiarContrasena(nuevo: String) {
        contrasena = nuevo
        if (errorContrasena != null) validarContrasena()
    }

    private fun validarCorreo(): Boolean {
        return when {
            correo.isBlank() -> {
                errorCorreo = "El correo es obligatorio"
                false
            }
            !Patterns.EMAIL_ADDRESS.matcher(correo).matches() -> {
                errorCorreo = "Formato de correo inválido"
                false
            }
            else -> {
                errorCorreo = null
                true
            }
        }
    }

    private fun validarContrasena(): Boolean {
        return when {
            contrasena.isBlank() -> {
                errorContrasena = "La contraseña es obligatoria"
                false
            }
            contrasena.length < 6 -> {
                errorContrasena = "La contraseña debe tener al menos 6 caracteres"
                false
            }
            else -> {
                errorContrasena = null
                true
            }
        }
    }

    fun validarTodo(): Boolean {
        val c = validarCorreo()
        val p = validarContrasena()
        return c && p
    }

    fun iniciarSesion(onResultado: (exito: Boolean, mensaje: String?) -> Unit) {
        if (!validarTodo()) {
            onResultado(false, "Corrige los errores")
            return
        }
        estaCargando = true
        viewModelScope.launch {
            delay(800)
            estaCargando = false
            // Validar contra el repositorio de usuarios en memoria
            val esValido = RepositorioUsuarios.validarCredenciales(correo, contrasena)
            if (esValido) {
                onResultado(true, null)
            } else {
                onResultado(false, "Credenciales inválidas")
            }
        }
    }
}
