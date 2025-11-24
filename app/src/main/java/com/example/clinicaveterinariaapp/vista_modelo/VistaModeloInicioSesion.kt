package com.example.clinicaveterinariaapp.vista_modelo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clinicaveterinariaapp.datos.RepositorioUsuarios
import com.example.clinicaveterinariaapp.validacion.Validador
import com.example.clinicaveterinariaapp.validacion.ResultadoValidacion
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

    fun alCambiarCorreo(v: String) {
        correo = v
        errorCorreo = null
    }

    fun alCambiarContrasena(v: String) {
        contrasena = v
        errorContrasena = null
    }

    private fun validarCampos(): Boolean {
        val resultadoCorreo = Validador.validarCorreo(correo)
        val resultadoContrasena = Validador.validarContrasena(contrasena)

        errorCorreo = (resultadoCorreo as? ResultadoValidacion.Error)?.mensaje
        errorContrasena = (resultadoContrasena as? ResultadoValidacion.Error)?.mensaje

        return resultadoCorreo is ResultadoValidacion.Exito &&
               resultadoContrasena is ResultadoValidacion.Exito
    }

    fun iniciarSesion(onResultado: (exito: Boolean, mensaje: String?) -> Unit) {
        if (!validarCampos()) {
            onResultado(false, "Por favor, corrige los errores.")
            return
        }

        estaCargando = true
        viewModelScope.launch {
            delay(500)
            val usuario = RepositorioUsuarios.obtenerTodos().find { it.correo.equals(correo.trim(), ignoreCase = true) && it.contrasena == contrasena }

            estaCargando = false
            if (usuario != null) {
                onResultado(true, "¡Inicio de sesión exitoso!")
                limpiarCampos()
            } else {
                onResultado(false, "El correo o la contraseña son incorrectos.")
            }
        }
    }

    private fun limpiarCampos() {
        correo = ""
        contrasena = ""
        errorCorreo = null
        errorContrasena = null
    }
}
