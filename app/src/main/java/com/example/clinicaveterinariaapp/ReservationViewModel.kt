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
import java.text.SimpleDateFormat

/**
 * ViewModel que maneja el formulario de reserva y una lista en memoria de reservas.
 * Contiene validaciones desacopladas que la UI consume.
 */
class ReservationViewModel : ViewModel() {
    // Campos del formulario
    var petName by mutableStateOf("")
        private set
    var ownerName by mutableStateOf("")
        private set
    var contact by mutableStateOf("")
        private set
    var date by mutableStateOf("") // YYYY-MM-DD
        private set
    var time by mutableStateOf("") // HH:mm
        private set
    var specialist by mutableStateOf("")
        private set
    var notes by mutableStateOf("")
        private set

    // Errores por campo
    var petNameError by mutableStateOf<String?>(null)
        private set
    var ownerNameError by mutableStateOf<String?>(null)
        private set
    var contactError by mutableStateOf<String?>(null)
        private set
    var dateError by mutableStateOf<String?>(null)
        private set
    var timeError by mutableStateOf<String?>(null)
        private set
    var specialistError by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    // Lista en memoria de reservas
    private val _reservations = mutableStateListOf<Reservation>()
    val reservations: List<Reservation> get() = _reservations

    // Funciones para actualizar campos
    fun onPetNameChange(value: String) {
        petName = value
        if (petNameError != null) validatePetName()
    }

    fun onOwnerNameChange(value: String) {
        ownerName = value
        if (ownerNameError != null) validateOwnerName()
    }

    fun onContactChange(value: String) {
        contact = value
        if (contactError != null) validateContact()
    }

    fun onDateChange(value: String) {
        date = value
        if (dateError != null) validateDate()
    }

    fun onTimeChange(value: String) {
        time = value
        if (timeError != null) validateTime()
    }

    fun onSpecialistChange(value: String) {
        specialist = value
        if (specialistError != null) validateSpecialist()
    }

    fun onNotesChange(value: String) {
        notes = value
    }

    // Validaciones por campo
    private fun validatePetName(): Boolean {
        return if (petName.isBlank()) {
            petNameError = "Nombre de la mascota obligatorio"
            false
        } else {
            petNameError = null
            true
        }
    }

    private fun validateOwnerName(): Boolean {
        return if (ownerName.isBlank()) {
            ownerNameError = "Nombre del propietario obligatorio"
            false
        } else {
            ownerNameError = null
            true
        }
    }

    private fun validateContact(): Boolean {
        val phoneRegex = Regex("^\\+?[0-9]{7,15}$")
        return if (contact.isBlank()) {
            contactError = "Contacto obligatorio"
            false
        } else if (!phoneRegex.matches(contact)) {
            contactError = "Teléfono inválido (solo números, opcional +)"
            false
        } else {
            contactError = null
            true
        }
    }

    private fun validateDate(): Boolean {
        if (date.isBlank()) {
            dateError = "Fecha obligatoria (YYYY-MM-DD)"
            return false
        }
        // Validar formato básico YYYY-MM-DD
        val parts = date.split("-")
        if (parts.size != 3) {
            dateError = "Formato de fecha inválido"
            return false
        }
        try {
            val year = parts[0].toInt()
            val month = parts[1].toInt()
            val day = parts[2].toInt()
            val cal = Calendar.getInstance()
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)

            val target = Calendar.getInstance()
            target.set(Calendar.YEAR, year)
            target.set(Calendar.MONTH, month - 1)
            target.set(Calendar.DAY_OF_MONTH, day)
            target.set(Calendar.HOUR_OF_DAY, 0)
            target.set(Calendar.MINUTE, 0)
            target.set(Calendar.SECOND, 0)
            target.set(Calendar.MILLISECOND, 0)

            if (target.before(cal)) {
                dateError = "La fecha debe ser hoy o futura"
                return false
            }
            dateError = null
            return true
        } catch (ex: NumberFormatException) {
            dateError = "Formato de fecha inválido"
            return false
        }
    }

    private fun validateTime(): Boolean {
        if (time.isBlank()) {
            timeError = "Hora obligatoria (HH:mm)"
            return false
        }
        val timeRegex = Regex("^[0-2][0-9]:[0-5][0-9]$")
        return if (!timeRegex.matches(time)) {
            timeError = "Formato de hora inválido (HH:mm)"
            false
        } else {
            timeError = null
            true
        }
    }

    private fun validateSpecialist(): Boolean {
        return if (specialist.isBlank()) {
            specialistError = "Selecciona un especialista"
            false
        } else {
            specialistError = null
            true
        }
    }

    fun validateAll(): Boolean {
        val p = validatePetName()
        val o = validateOwnerName()
        val c = validateContact()
        val d = validateDate()
        val t = validateTime()
        val s = validateSpecialist()
        return p && o && c && d && t && s
    }

    // CRUD básico en memoria
    fun createReservation(onResult: (Boolean, String?) -> Unit) {
        if (!validateAll()) {
            onResult(false, "Corrige los errores del formulario")
            return
        }

        isLoading = true
        viewModelScope.launch {
            delay(600)
            val id = UUID.randomUUID().toString()
            val r = Reservation(
                id = id,
                petName = petName.trim(),
                ownerName = ownerName.trim(),
                contact = contact.trim(),
                date = date.trim(),
                time = time.trim(),
                specialist = specialist.trim(),
                notes = if (notes.isBlank()) null else notes.trim()
            )
            _reservations.add(0, r)
            isLoading = false
            clearForm()
            onResult(true, null)
        }
    }

    fun cancelReservation(id: String, onResult: (Boolean, String?) -> Unit) {
        val idx = _reservations.indexOfFirst { it.id == id }
        if (idx == -1) {
            onResult(false, "Reserva no encontrada")
            return
        }
        isLoading = true
        viewModelScope.launch {
            delay(400)
            val old = _reservations[idx]
            _reservations[idx] = old.copy(status = ReservationStatus.CANCELLED)
            isLoading = false
            onResult(true, null)
        }
    }

    fun clearForm() {
        petName = ""
        ownerName = ""
        contact = ""
        date = ""
        time = ""
        specialist = ""
        notes = ""
        petNameError = null
        ownerNameError = null
        contactError = null
        dateError = null
        timeError = null
        specialistError = null
    }
}
