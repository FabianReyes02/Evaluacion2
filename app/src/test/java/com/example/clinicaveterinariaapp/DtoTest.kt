package com.example.clinicaveterinariaapp

import com.example.clinicaveterinariaapp.api.ProfesionalDto
import org.junit.Assert.*
import org.junit.Test

/**
 * Pruebas unitarias para DTOs y mapeos
 */
class DtoTest {

    @Test
    fun `mapear ProfesionalDto a modelo`() {
        val dto = ProfesionalDto(
            id = "P001",
            nombre = "Dr. Martínez",
            especialidad = "Oncología",
            contacto = "+56911111111",
            descripcion = "Especialista en cáncer animal"
        )

        assertEquals("P001", dto.id)
        assertEquals("Dr. Martínez", dto.nombre)
        assertNotNull(dto.descripcion)
    }

    @Test
    fun `crear dto sin id para POST`() {
        val dto = ProfesionalDto(
            id = null,
            nombre = "Dra. Fernández",
            especialidad = "Cardiología",
            contacto = null,
            descripcion = null
        )

        assertNull(dto.id)
        assertNull(dto.contacto)
        assertEquals("Cardiología", dto.especialidad)
    }

    @Test
    fun `validar campos obligatorios en dto`() {
        val dto = ProfesionalDto(
            id = "P002",
            nombre = "Dr. Torres",
            especialidad = "Traumatología",
            contacto = null,
            descripcion = null
        )

        assertTrue(dto.nombre.isNotBlank())
        assertTrue(dto.especialidad.isNotBlank())
    }
}
