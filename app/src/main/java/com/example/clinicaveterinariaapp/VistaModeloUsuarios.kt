package com.example.clinicaveterinariaapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    var errorNombre by mutableStateOf<String?>(null) // Añadido
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
    fun alCambiarNombre(v: String) { nombre = v; if (errorNombre != null) validarNombre() } // Modificado

    private fun validarCorreo(): Boolean {
        return if (correo.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) { errorCorreo = "Correo inválido"; false } else { errorCorreo = null; true }
    }

    private fun validarContrasena(): Boolean {
        return if (contrasena.isBlank() || contrasena.length < 6) { errorContrasena = "Contraseña inválida (mín. 6 caracteres)"; false } else { errorContrasena = null; true }
    }

    private fun validarNombre(): Boolean { // Añadido
        return if (nombre.isBlank()) { errorNombre = "Nombre obligatorio"; false } else { errorNombre = null; true }
    }

    private fun validarTodoRegistro(): Boolean { return validarNombre() && validarCorreo() && validarContrasena() }

    fun crearUsuario(onResultado: (Boolean, String?) -> Unit) {
        if (!validarTodoRegistro()) { onResultado(false, "Por favor, corrige los errores"); return }
        estaCargando = true
        viewModelScope.launch {
            delay(300)
            val u = Usuario(correo = correo.trim(), contrasena = contrasena, nombre = nombre.trim())
            // Simular verificación de correo existente
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
    
    // ... (El resto del ViewModel no necesita cambios para este fix)

    fun limpiarFormulario() { correo = ""; contrasena = ""; nombre = ""; errorCorreo = null; errorContrasena = null; errorNombre = null }
    // ... (El resto de las funciones)
    fun iniciarSesion(onResultado: (Boolean, String?) -> Unit) {
        if (!validarCorreo() || !validarContrasena()) {
            onResultado(false, "Corrige los errores")
            return
        }

        estaCargando = true
        viewModelScope.launch {
            delay(300)
            val usuario = RepositorioUsuarios.obtenerTodos()
                .find { it.correo.equals(correo.trim(), ignoreCase = true) && it.contrasena == contrasena }

            estaCargando = false
            if (usuario != null) {
                onResultado(true, "Inicio de sesión correcto")
                limpiarFormulario()
            } else {
                onResultado(false, "Correo o contraseña incorrectos")
            }
        }
    }

    fun actualizarUsuario(correoAnterior: String, onResultado: (Boolean, String?) -> Unit) {
        if (!validarTodoRegistro()) { onResultado(false, "Corrige los errores"); return }
        estaCargando = true
        viewModelScope.launch {
            delay(300)
            val actualizado = Usuario(correo = correo.trim(), contrasena = contrasena, nombre = if (nombre.isBlank()) null else nombre.trim())
            val ok = RepositorioUsuarios.actualizarUsuario(correoAnterior, actualizado)
            if (ok) {
                val idx = _lista.indexOfFirst { it.correo.equals(correoAnterior, ignoreCase = true) }
                if (idx != -1) _lista[idx] = actualizado
                estaCargando = false
                limpiarFormulario()
                onResultado(true, null)
            } else {
                estaCargando = false
                onResultado(false, "Usuario no encontrado")
            }
        }
    }

    fun eliminarUsuario(correo: String, onResultado: (Boolean, String?) -> Unit) {
        estaCargando = true
        viewModelScope.launch {
            delay(200)
            val ok = RepositorioUsuarios.eliminarUsuario(correo)
            if (ok) _lista.removeIf { it.correo.equals(correo, ignoreCase = true) }
            estaCargando = false
            onResultado(ok, if (ok) null else "No se pudo eliminar")
        }
    }

    fun cargarParaEdicion(usuario: Usuario) {
        correo = usuario.correo
        contrasena = usuario.contrasena
        nombre = usuario.nombre ?: ""
    }

}
