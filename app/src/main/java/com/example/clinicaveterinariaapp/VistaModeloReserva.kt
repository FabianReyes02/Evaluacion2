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

/**
 * ViewModel (en español) que maneja el formulario de reserva y la lista en memoria.
 */
class VistaModeloReserva : ViewModel() {
    var nombreMascota by mutableStateOf("")
        private set
    var nombrePropietario by mutableStateOf("")
        private set
    var contacto by mutableStateOf("")
        private set
    var fecha by mutableStateOf("") // YYYY-MM-DD
        private set
    var hora by mutableStateOf("") // HH:mm
        private set
    var especialista by mutableStateOf("")
        private set
    var notas by mutableStateOf("")
        private set

    var errorNombreMascota by mutableStateOf<String?>(null)
        private set
    var errorNombrePropietario by mutableStateOf<String?>(null)
        private set
    var errorContacto by mutableStateOf<String?>(null)
        private set
    var errorFecha by mutableStateOf<String?>(null)
        private set
    var errorHora by mutableStateOf<String?>(null)
        private set
    var errorEspecialista by mutableStateOf<String?>(null)
        private set

    var estaCargando by mutableStateOf(false)
        private set

    private val _reservas = mutableStateListOf<Reserva>()
    val reservas: List<Reserva> get() = _reservas

    fun alCambiarNombreMascota(valor: String) {
        nombreMascota = valor
        if (errorNombreMascota != null) validarNombreMascota()
    }

    fun alCambiarNombrePropietario(valor: String) {
        nombrePropietario = valor
        if (errorNombrePropietario != null) validarNombrePropietario()
    }

    fun alCambiarContacto(valor: String) {
        contacto = valor
        if (errorContacto != null) validarContacto()
    }

    fun alCambiarFecha(valor: String) {
        fecha = valor
        if (errorFecha != null) validarFecha()
    }

    fun alCambiarHora(valor: String) {
        hora = valor
        if (errorHora != null) validarHora()
    }

    fun alCambiarEspecialista(valor: String) {
        especialista = valor
        if (errorEspecialista != null) validarEspecialista()
    }

    fun alCambiarNotas(valor: String) {
        notas = valor
    }

    private fun validarNombreMascota(): Boolean {
        return if (nombreMascota.isBlank()) {
            errorNombreMascota = "Nombre de la mascota obligatorio"
            false
        } else {
            errorNombreMascota = null
            true
        }
    }

    private fun validarNombrePropietario(): Boolean {
        return if (nombrePropietario.isBlank()) {
            errorNombrePropietario = "Nombre del propietario obligatorio"
            false
        } else {
            errorNombrePropietario = null
            true
        }
    }

    private fun validarContacto(): Boolean {
        val numeroLimpio = contacto.replace(" ", "") // Ignorar espacios
        // Regex para validar números de celular chilenos (9 dígitos, con o sin +56)
        val phoneRegex = Regex("^(?:\\+?56)?9\\d{8}$")
        return if (numeroLimpio.isBlank()) {
            errorContacto = "Contacto obligatorio"
            false
        } else if (!phoneRegex.matches(numeroLimpio)) {
            errorContacto = "Formato de celular inválido (ej: 912345678)"
            false
        } else {
            errorContacto = null
            true
        }
    }

    private fun validarFecha(): Boolean {
        if (fecha.isBlank()) {
            errorFecha = "Fecha obligatoria (YYYY-MM-DD)"
            return false
        }
        val partes = fecha.split("-")
        if (partes.size != 3) {
            errorFecha = "Formato de fecha inválido"
            return false
        }
        try {
            val year = partes[0].toInt()
            val month = partes[1].toInt()
            val day = partes[2].toInt()
            val hoy = Calendar.getInstance()
            hoy.set(Calendar.HOUR_OF_DAY, 0)
            hoy.set(Calendar.MINUTE, 0)
            hoy.set(Calendar.SECOND, 0)
            hoy.set(Calendar.MILLISECOND, 0)

            val objetivo = Calendar.getInstance()
            objetivo.set(Calendar.YEAR, year)
            objetivo.set(Calendar.MONTH, month - 1)
            objetivo.set(Calendar.DAY_OF_MONTH, day)
            objetivo.set(Calendar.HOUR_OF_DAY, 0)
            objetivo.set(Calendar.MINUTE, 0)
            objetivo.set(Calendar.SECOND, 0)
            objetivo.set(Calendar.MILLISECOND, 0)

            if (objetivo.before(hoy)) {
                errorFecha = "La fecha debe ser hoy o futura"
                return false
            }
            errorFecha = null
            return true
        } catch (_: NumberFormatException) {
            errorFecha = "Formato de fecha inválido"
            return false
        }
    }

    private fun validarHora(): Boolean {
        if (hora.isBlank()) {
            errorHora = "Hora obligatoria (HH:mm)"
            return false
        }
        val horaRegex = Regex("^[0-2][0-9]:[0-5][0-9]$")
        return if (!horaRegex.matches(hora)) {
            errorHora = "Formato de hora inválido (HH:mm)"
            false
        } else {
            errorHora = null
            true
        }
    }

    private fun validarEspecialista(): Boolean {
        return if (especialista.isBlank()) {
            errorEspecialista = "Selecciona un especialista"
            false
        } else {
            errorEspecialista = null
            true
        }
    }

    fun validarTodo(): Boolean {
        val a = validarNombreMascota()
        val b = validarNombrePropietario()
        val c = validarContacto()
        val d = validarFecha()
        val e = validarHora()
        val f = validarEspecialista()
        return a && b && c && d && e && f
    }

    fun crearReserva(onResultado: (Boolean, String?) -> Unit) {
        if (!validarTodo()) {
            onResultado(false, "Corrige los errores del formulario")
            return
        }
        estaCargando = true
        viewModelScope.launch {
            delay(600)
            val id = UUID.randomUUID().toString()
            val r = Reserva(
                id = id,
                nombreMascota = nombreMascota.trim(),
                nombrePropietario = nombrePropietario.trim(),
                contacto = contacto.trim(),
                fecha = fecha.trim(),
                hora = hora.trim(),
                especialista = especialista.trim(),
                notas = if (notas.isBlank()) null else notas.trim()
            )
            _reservas.add(0, r)
            estaCargando = false
            limpiarFormulario()
            onResultado(true, null)
        }
    }

    fun cancelarReserva(id: String, onResultado: (Boolean, String?) -> Unit) {
        val idx = _reservas.indexOfFirst { it.id == id }
        if (idx == -1) {
            onResultado(false, "Reserva no encontrada")
            return
        }
        estaCargando = true
        viewModelScope.launch {
            delay(400)
            val anterior = _reservas[idx]
            _reservas[idx] = anterior.copy(estado = EstadoReserva.CANCELADA)
            estaCargando = false
            onResultado(true, null)
        }
    }

    fun limpiarFormulario() {
        nombreMascota = ""
        nombrePropietario = ""
        contacto = ""
        fecha = ""
        hora = ""
        especialista = ""
        notas = ""
        errorNombreMascota = null
        errorNombrePropietario = null
        errorContacto = null
        errorFecha = null
        errorHora = null
        errorEspecialista = null
    }
}
