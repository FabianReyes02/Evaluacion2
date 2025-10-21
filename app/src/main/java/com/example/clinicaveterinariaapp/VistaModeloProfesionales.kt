package com.example.clinicaveterinariaapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class VistaModeloProfesionales : ViewModel() {
    var nombre by mutableStateOf("")
        private set
    var especialidad by mutableStateOf("")
        private set
    var contacto by mutableStateOf("")
        private set
    var descripcion by mutableStateOf("")
        private set

    var errorNombre by mutableStateOf<String?>(null)
        private set
    var errorEspecialidad by mutableStateOf<String?>(null)
        private set

    var estaCargando by mutableStateOf(false)
        private set

    private val _lista = mutableStateListOf<Profesional>()
    val lista: List<Profesional> get() = _lista

    init {
        // inicializar desde repositorio existente
        _lista.addAll(RepositorioProfesionales.obtenerTodos())
    }

    fun alCambiarNombre(v: String) {
        nombre = v
        if (errorNombre != null) validarNombre()
    }

    fun alCambiarEspecialidad(v: String) {
        especialidad = v
        if (errorEspecialidad != null) validarEspecialidad()
    }

    fun alCambiarContacto(v: String) {
        contacto = v
    }

    fun alCambiarDescripcion(v: String) {
        descripcion = v
    }

    private fun validarNombre(): Boolean {
        return if (nombre.isBlank()) {
            errorNombre = "Nombre obligatorio"
            false
        } else {
            errorNombre = null
            true
        }
    }

    private fun validarEspecialidad(): Boolean {
        return if (especialidad.isBlank()) {
            errorEspecialidad = "Especialidad obligatoria"
            false
        } else {
            errorEspecialidad = null
            true
        }
    }

    fun validarTodo(): Boolean {
        val a = validarNombre()
        val b = validarEspecialidad()
        return a && b
    }

    fun crearProfesional(onResultado: (Boolean, String?) -> Unit) {
        if (!validarTodo()) {
            onResultado(false, "Corrige los errores")
            return
        }
        estaCargando = true
        viewModelScope.launch {
            delay(400)
            val p = Profesional(
                id = UUID.randomUUID().toString(),
                nombre = nombre.trim(),
                especialidad = especialidad.trim(),
                contacto = if (contacto.isBlank()) null else contacto.trim(),
                descripcion = if (descripcion.isBlank()) null else descripcion.trim()
            )
            RepositorioProfesionales.agregar(p)
            _lista.add(0, p)
            estaCargando = false
            limpiarFormulario()
            onResultado(true, null)
        }
    }

    fun actualizarProfesional(id: String, onResultado: (Boolean, String?) -> Unit) {
        if (!validarTodo()) { onResultado(false, "Corrige los errores"); return }
        estaCargando = true
        viewModelScope.launch {
            delay(300)
            val actualizado = Profesional(
                id = id,
                nombre = nombre.trim(),
                especialidad = especialidad.trim(),
                contacto = if (contacto.isBlank()) null else contacto.trim(),
                descripcion = if (descripcion.isBlank()) null else descripcion.trim()
            )
            val ok = RepositorioProfesionales.actualizar(id, actualizado)
            if (ok) {
                val idx = _lista.indexOfFirst { it.id == id }
                if (idx != -1) _lista[idx] = actualizado
                estaCargando = false
                limpiarFormulario()
                onResultado(true, null)
            } else {
                estaCargando = false
                onResultado(false, "Profesional no encontrado")
            }
        }
    }

    fun eliminarProfesional(id: String, onResultado: (Boolean, String?) -> Unit) {
        estaCargando = true
        viewModelScope.launch {
            delay(200)
            val ok = RepositorioProfesionales.eliminar(id)
            if (ok) {
                _lista.removeIf { it.id == id }
                estaCargando = false
                onResultado(true, null)
            } else {
                estaCargando = false
                onResultado(false, "No se pudo eliminar")
            }
        }
    }

    fun cargarParaEdicion(profesional: Profesional) {
        nombre = profesional.nombre
        especialidad = profesional.especialidad
        contacto = profesional.contacto ?: ""
        descripcion = profesional.descripcion ?: ""
    }

    fun limpiarFormulario() {
        nombre = ""
        especialidad = ""
        contacto = ""
        descripcion = ""
        errorNombre = null
        errorEspecialidad = null
    }
}

