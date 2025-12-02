package com.example.clinicaveterinariaapp

import com.example.clinicaveterinariaapp.datos.EstadoReserva
import com.example.clinicaveterinariaapp.datos.Reserva
import org.junit.Assert.*
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

/**
 * Pruebas unitarias para el modelo Reserva
 */
class ReservaTest {

    @Test
    fun `crear reserva con estado por defecto`() {
        val reserva = Reserva(
            id = "R001",
            nombreMascota = "Firulais",
            nombrePropietario = "Juan Pérez",
            contacto = "+56912345678",
            fecha = "2025-12-15",
            hora = "10:30",
            especialista = "Dr. González"
        )

        assertEquals(EstadoReserva.AGENDADA, reserva.estado)
        assertNull(reserva.remedio)
        assertNull(reserva.notas)
    }

    @Test
    fun `reserva con remedio y notas`() {
        val reserva = Reserva(
            id = "R002",
            nombreMascota = "Michi",
            nombrePropietario = "Ana López",
            contacto = "+56987654321",
            fecha = "2025-12-20",
            hora = "15:00",
            especialista = "Dra. Silva",
            remedio = "Amoxicilina",
            notas = "Control postoperatorio"
        )

        assertNotNull(reserva.remedio)
        assertNotNull(reserva.notas)
        assertEquals("Amoxicilina", reserva.remedio)
    }

    @Test
    fun `cambiar estado de reserva a cancelada`() {
        val reserva = Reserva(
            id = "R003",
            nombreMascota = "Rex",
            nombrePropietario = "Pedro Gómez",
            contacto = "+56911223344",
            fecha = "2025-12-18",
            hora = "09:00",
            especialista = "Dr. Rojas"
        )

        val cancelada = reserva.copy(estado = EstadoReserva.CANCELADA)

        assertEquals(EstadoReserva.AGENDADA, reserva.estado)
        assertEquals(EstadoReserva.CANCELADA, cancelada.estado)
    }

    @Test
    fun `validar formato de fecha`() {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        sdf.isLenient = false

        val fechaValida = "2025-12-15"
        val fechaInvalida = "2025-13-40"

        assertNotNull(kotlin.runCatching { sdf.parse(fechaValida) }.getOrNull())
        assertNull(kotlin.runCatching { sdf.parse(fechaInvalida) }.getOrNull())
    }

    @Test
    fun `validar formato de hora`() {
        val horaRegex = Regex("^([01]\\d|2[0-3]):([0-5]\\d)$")

        assertTrue(horaRegex.matches("10:30"))
        assertTrue(horaRegex.matches("00:00"))
        assertTrue(horaRegex.matches("23:59"))
        assertFalse(horaRegex.matches("24:00"))
        assertFalse(horaRegex.matches("10:60"))
    }
}
