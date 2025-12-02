package com.example.clinicaveterinariaapp.vista_modelo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clinicaveterinariaapp.datos.Profesional
import com.example.clinicaveterinariaapp.datos.RepositorioProfesionales
import com.example.clinicaveterinariaapp.api.ProfesionalApiRepository
import com.example.clinicaveterinariaapp.api.ProfesionalApiService
import com.example.clinicaveterinariaapp.api.ProfesionalDto
import com.example.clinicaveterinariaapp.api.RetrofitClient
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

    // Mensajes relacionados a operaciones remotas
    var mensajeRemoto by mutableStateOf<String?>(null)
        private set

    private val _lista = mutableStateListOf<Profesional>()
    val lista: List<Profesional> get() = _lista

    // Cliente API
    private val apiService: ProfesionalApiService = RetrofitClient.instance.create(ProfesionalApiService::class.java)
    private val apiRepo = ProfesionalApiRepository(apiService)

    fun alCambiarNombre(v: String) { nombre = v; if (errorNombre != null) validarNombre() }
    fun alCambiarEspecialidad(v: String) { especialidad = v; if (errorEspecialidad != null) validarEspecialidad() }
    fun alCambiarContacto(v: String) { contacto = v }
    fun alCambiarDescripcion(v: String) { descripcion = v }

    private fun validarNombre(): Boolean {
        return if (nombre.isBlank()) { errorNombre = "Nombre obligatorio"; false } else { errorNombre = null; true }
    }

    private fun validarEspecialidad(): Boolean {
        return if (especialidad.isBlank()) { errorEspecialidad = "Especialidad obligatoria"; false } else { errorEspecialidad = null; true }
    }

    fun validarTodo(): Boolean { val a = validarNombre(); val b = validarEspecialidad(); return a && b }

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

    // ------------------ Operaciones remotas con la API (opcionales) ------------------

    /**
     * Carga la lista de profesionales desde la API remota y la reemplaza localmente.
     * Devuelve true en onResultado si la operación fue exitosa.
     */
    fun cargarDesdeApi(onResultado: (Boolean, String?) -> Unit = { _, _ -> }) {
        estaCargando = true
        mensajeRemoto = null
        viewModelScope.launch {
            try {
                val resp = apiRepo.obtenerTodos()
                if (resp.isSuccessful) {
                    val body = resp.body() ?: emptyList()
                    // Mapear DTO -> Modelo local
                    _lista.clear()
                    body.forEach { dto ->
                        _lista.add(
                            Profesional(
                                id = dto.id ?: UUID.randomUUID().toString(),
                                nombre = dto.nombre,
                                especialidad = dto.especialidad,
                                contacto = dto.contacto,
                                descripcion = dto.descripcion
                            )
                        )
                    }
                    mensajeRemoto = "Cargado ${_lista.size} profesionales"
                    onResultado(true, null)
                } else {
                    mensajeRemoto = "Error API: ${resp.code()}"
                    onResultado(false, mensajeRemoto)
                }
            } catch (e: Exception) {
                mensajeRemoto = e.message ?: "Error de red"
                onResultado(false, mensajeRemoto)
            } finally {
                estaCargando = false
            }
        }
    }

    /**
     * Intenta crear un profesional en la API y, si es exitoso, lo agrega localmente.
     */
    fun crearProfesionalRemoto(onResultado: (Boolean, String?) -> Unit) {
        if (!validarTodo()) { onResultado(false, "Corrige los errores"); return }
        estaCargando = true
        mensajeRemoto = null
        viewModelScope.launch {
            try {
                val dto = ProfesionalDto(
                    id = null,
                    nombre = nombre.trim(),
                    especialidad = especialidad.trim(),
                    contacto = if (contacto.isBlank()) null else contacto.trim(),
                    descripcion = if (descripcion.isBlank()) null else descripcion.trim()
                )
                val resp = apiRepo.crear(dto)
                if (resp.isSuccessful) {
                    val creado = resp.body()
                    if (creado != null) {
                        val p = Profesional(
                            id = creado.id ?: UUID.randomUUID().toString(),
                            nombre = creado.nombre,
                            especialidad = creado.especialidad,
                            contacto = creado.contacto,
                            descripcion = creado.descripcion
                        )
                        RepositorioProfesionales.agregar(p)
                        _lista.add(0, p)
                        limpiarFormulario()
                        onResultado(true, null)
                    } else {
                        onResultado(false, "Respuesta vacía")
                    }
                } else {
                    onResultado(false, "Error API: ${resp.code()}")
                }
            } catch (e: Exception) {
                onResultado(false, e.message)
            } finally {
                estaCargando = false
            }
        }
    }

    /**
     * Eliminar profesional de la API y localmente si la operación remota tiene éxito.
     */
    fun eliminarProfesionalRemoto(id: String, onResultado: (Boolean, String?) -> Unit) {
        estaCargando = true
        mensajeRemoto = null
        viewModelScope.launch {
            try {
                val resp = apiRepo.eliminar(id)
                if (resp.isSuccessful) {
                    // borrar localmente
                    RepositorioProfesionales.eliminar(id)
                    _lista.removeIf { it.id == id }
                    onResultado(true, null)
                } else {
                    onResultado(false, "Error API: ${resp.code()}")
                }
            } catch (e: Exception) {
                onResultado(false, e.message)
            } finally {
                estaCargando = false
            }
        }
    }
}
