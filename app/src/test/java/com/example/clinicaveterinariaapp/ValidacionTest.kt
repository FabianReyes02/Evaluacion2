package com.example.clinicaveterinariaapp

import org.junit.Assert.*
import org.junit.Test

/**
 * Pruebas unitarias para validaciones de formularios
 */
class ValidacionTest {

    @Test
    fun `validar telefono chileno correcto`() {
        val phoneRegex = Regex("^(?:\\+?56)?9\\d{8}$")

        assertTrue(phoneRegex.matches("+56912345678"))
        assertTrue(phoneRegex.matches("56987654321"))
        assertTrue(phoneRegex.matches("912345678"))
    }

    @Test
    fun `validar telefono chileno incorrecto`() {
        val phoneRegex = Regex("^(?:\\+?56)?9\\d{8}$")

        assertFalse(phoneRegex.matches("12345678")) // No empieza con 9
        assertFalse(phoneRegex.matches("+5691234567")) // Faltan dígitos
        assertFalse(phoneRegex.matches("abc123")) // Contiene letras
    }

    @Test
    fun `validar nombre no vacio`() {
        val nombreVacio = ""
        val nombreBlanco = "   "
        val nombreValido = "Dr. Juan Pérez"

        assertTrue(nombreVacio.isBlank())
        assertTrue(nombreBlanco.isBlank())
        assertFalse(nombreValido.isBlank())
    }

    @Test
    fun `validar formato fecha YYYY-MM-DD`() {
        val fechaRegex = Regex("^\\d{4}-\\d{2}-\\d{2}$")

        assertTrue(fechaRegex.matches("2025-12-15"))
        assertTrue(fechaRegex.matches("2025-01-01"))
        assertFalse(fechaRegex.matches("15-12-2025"))
        assertFalse(fechaRegex.matches("2025/12/15"))
    }

    @Test
    fun `validar formato hora HH-mm`() {
        val horaRegex = Regex("^([01]\\d|2[0-3]):([0-5]\\d)$")

        assertTrue(horaRegex.matches("10:30"))
        assertTrue(horaRegex.matches("00:00"))
        assertTrue(horaRegex.matches("23:59"))
        assertFalse(horaRegex.matches("24:00"))
        assertFalse(horaRegex.matches("10:60"))
        assertFalse(horaRegex.matches("10-30"))
    }

    @Test
    fun `trimear espacios en campos`() {
        val nombre = "  Juan Pérez  "
        val trimmed = nombre.trim()

        assertEquals("Juan Pérez", trimmed)
        assertNotEquals(nombre, trimmed)
    }
}
