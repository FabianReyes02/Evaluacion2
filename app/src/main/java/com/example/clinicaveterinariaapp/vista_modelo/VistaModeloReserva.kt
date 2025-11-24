package com.example.clinicaveterinariaapp.vista_modelo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clinicaveterinariaapp.datos.EstadoReserva
import com.example.clinicaveterinariaapp.datos.Reserva
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID

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
    var remedio by mutableStateOf<String?>(null)
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

    fun alCambiarNombreMascota(valor: String) { nombreMascota = valor; errorNombreMascota = null }
    fun alCambiarNombrePropietario(valor: String) { nombrePropietario = valor; errorNombrePropietario = null }
    fun alCambiarContacto(valor: String) { contacto = valor; errorContacto = null }
    fun alCambiarFecha(valor: String) { fecha = valor; errorFecha = null }
    fun alCambiarHora(valor: String) { hora = valor; errorHora = null }
    fun alCambiarEspecialista(valor: String) { especialista = valor; errorEspecialista = null }
    fun alCambiarRemedio(valor: String?) { remedio = valor }
    fun alCambiarNotas(valor: String) { notas = valor }

    private fun validarNombreMascota(): Boolean {
        return if (nombreMascota.isBlank()) { errorNombreMascota = "Nombre de mascota obligatorio"; false } else { errorNombreMascota = null; true }
    }

    private fun validarNombrePropietario(): Boolean {
        return if (nombrePropietario.isBlank()) { errorNombrePropietario = "Nombre de propietario obligatorio"; false } else { errorNombrePropietario = null; true }
    }

    private fun validarContacto(): Boolean {
        val phoneRegex = Regex("^(?:\\+?56)?9\\d{8}$")
        return if (contacto.isBlank()) { errorContacto = "Contacto obligatorio"; false }
        else if (!phoneRegex.matches(contacto.replace(" ", ""))) { errorContacto = "Formato de celular inválido"; false }
        else { errorContacto = null; true }
    }

    private fun validarFecha(): Boolean {
        if (fecha.isBlank()) { errorFecha = "Fecha obligatoria"; return false }
        try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            sdf.isLenient = false
            val parsedDate = sdf.parse(fecha)
            val calendar = Calendar.getInstance()
            calendar.time = parsedDate!!
            val today = Calendar.getInstance()
            today.set(Calendar.HOUR_OF_DAY, 0)
            today.set(Calendar.MINUTE, 0)
            today.set(Calendar.SECOND, 0)
            today.set(Calendar.MILLISECOND, 0)
            if (calendar.before(today)) { errorFecha = "La fecha no puede ser pasada"; return false }
        } catch (e: Exception) { errorFecha = "Formato de fecha inválido"; return false }
        errorFecha = null
        return true
    }

    private fun validarHora(): Boolean {
        val timeRegex = Regex("^([01]\\d|2[0-3]):([0-5]\\d)$")
        return if (hora.isBlank()) { errorHora = "Hora obligatoria"; false }
        else if (!timeRegex.matches(hora)) { errorHora = "Formato de hora inválido"; false }
        else { errorHora = null; true }
    }

    private fun validarEspecialista(): Boolean {
        return if (especialista.isBlank()) { errorEspecialista = "Especialista obligatorio"; false } else { errorEspecialista = null; true }
    }

    private fun validarTodo(): Boolean {
        val v1 = validarNombreMascota()
        val v2 = validarNombrePropietario()
        val v3 = validarContacto()
        val v4 = validarFecha()
        val v5 = validarHora()
        val v6 = validarEspecialista()
        return v1 && v2 && v3 && v4 && v5 && v6
    }

    fun crearReserva(onResultado: (Boolean, String?) -> Unit) {
        if (!validarTodo()) {
            onResultado(false, "Por favor, corrige los errores.")
            return
        }
        estaCargando = true
        viewModelScope.launch {
            delay(600)
            val r = Reserva(UUID.randomUUID().toString(), nombreMascota, nombrePropietario, contacto, fecha, hora, especialista, remedio, notas.takeIf { it.isNotBlank() })
            _reservas.add(0, r)
            estaCargando = false
            limpiarFormulario()
            onResultado(true, "Reserva creada con éxito")
        }
    }

    fun cancelarReserva(id: String, onResultado: (Boolean, String?) -> Unit) {
        val index = _reservas.indexOfFirst { it.id == id }
        if (index != -1) {
            estaCargando = true
            viewModelScope.launch {
                delay(400)
                val original = _reservas[index]
                _reservas[index] = original.copy(estado = EstadoReserva.CANCELADA)
                estaCargando = false
                onResultado(true, "Reserva cancelada")
            }
        } else {
            onResultado(false, "No se encontró la reserva")
        }
    }

    fun limpiarFormulario() {
        nombreMascota = ""
        nombrePropietario = ""
        contacto = ""
        fecha = ""
        hora = ""
        especialista = ""
        remedio = null
        notas = ""
        errorNombreMascota = null
        errorNombrePropietario = null
        errorContacto = null
        errorFecha = null
        errorHora = null
        errorEspecialista = null
    }
}
