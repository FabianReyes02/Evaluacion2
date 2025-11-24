package com.example.clinicaveterinariaapp.vista_modelo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clinicaveterinariaapp.datos.RepositorioUsuarios
import com.example.clinicaveterinariaapp.datos.Usuario
import com.example.clinicaveterinariaapp.validacion.ResultadoValidacion
import com.example.clinicaveterinariaapp.validacion.Validador
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class VistaModeloUsuarios : ViewModel() {
    var correo by mutableStateOf("")
        private set
    var contrasena by mutableStateOf("")
        private set
    var nombre by mutableStateOf("")
        private set

    var errorCorreo by mutableStateOf<String?>(null)
        private set
    var errorContrasena by mutableStateOf<String?>(null)
        private set
    var errorNombre by mutableStateOf<String?>(null)
        private set

    var estaCargando by mutableStateOf(false)
        private set

    private val _lista = mutableStateListOf<Usuario>()
    val lista: List<Usuario> get() = _lista

    init {
        _lista.addAll(RepositorioUsuarios.obtenerTodos())
    }

    fun alCambiarCorreo(v: String) { correo = v; if (errorCorreo != null) validarCorreo() }
    fun alCambiarContrasena(v: String) { contrasena = v; if (errorContrasena != null) validarContrasena() }
    fun alCambiarNombre(v: String) { nombre = v; if (errorNombre != null) validarNombre() }

    private fun validarCorreo(): Boolean {
        val resultado = Validador.validarCorreo(correo)
        errorCorreo = (resultado as? ResultadoValidacion.Error)?.mensaje
        return resultado is ResultadoValidacion.Exito
    }

    private fun validarContrasena(): Boolean {
        val resultado = Validador.validarContrasena(contrasena)
        errorContrasena = (resultado as? ResultadoValidacion.Error)?.mensaje
        return resultado is ResultadoValidacion.Exito
    }

    private fun validarNombre(): Boolean {
        val resultado = Validador.validarNombre(nombre)
        errorNombre = (resultado as? ResultadoValidacion.Error)?.mensaje
        return resultado is ResultadoValidacion.Exito
    }

    private fun validarTodoRegistro(): Boolean { return validarNombre() && validarCorreo() && validarContrasena() }

    fun crearUsuario(onResultado: (Boolean, String?) -> Unit) {
        if (!validarTodoRegistro()) {
            onResultado(false, "Por favor, corrige los errores"); return
        }
        estaCargando = true
        viewModelScope.launch {
            delay(300)
            val u = Usuario(correo = correo.trim(), contrasena = contrasena, nombre = nombre.trim())
            if (RepositorioUsuarios.obtenerTodos().any { it.correo.equals(u.correo, ignoreCase = true) }) {
                errorCorreo = "El correo ya está registrado"
                estaCargando = false
                onResultado(false, "El correo ya está en uso")
                return@launch
            }
            RepositorioUsuarios.agregarUsuario(u)
            _lista.add(0, u)
            estaCargando = false
            limpiarFormulario()
            onResultado(true, "¡Usuario registrado con éxito!")
        }
    }
    
    fun limpiarFormulario() { correo = ""; contrasena = ""; nombre = ""; errorCorreo = null; errorContrasena = null; errorNombre = null }
}
