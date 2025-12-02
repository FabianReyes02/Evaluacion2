package com.example.clinicaveterinariaapp

import com.example.clinicaveterinariaapp.datos.Profesional
import org.junit.Assert.*
import org.junit.Test

/**
 * Pruebas unitarias para el modelo Profesional
 */
class ProfesionalTest {

    @Test
    fun `crear profesional con datos validos`() {
        val prof = Profesional(
            id = "123",
            nombre = "Dr. Juan Pérez",
            especialidad = "Cirugía",
            contacto = "+56912345678",
            descripcion = "Especialista en cirugía veterinaria"
        )

        assertEquals("123", prof.id)
        assertEquals("Dr. Juan Pérez", prof.nombre)
        assertEquals("Cirugía", prof.especialidad)
        assertEquals("+56912345678", prof.contacto)
        assertNotNull(prof.descripcion)
    }

    @Test
    fun `profesional sin contacto es valido`() {
        val prof = Profesional(
            id = "456",
            nombre = "Dra. Ana López",
            especialidad = "Dermatología"
        )

        assertNull(prof.contacto)
        assertNull(prof.descripcion)
        assertEquals("Dermatología", prof.especialidad)
    }

    @Test
    fun `copy de profesional mantiene valores`() {
        val original = Profesional(
            id = "789",
            nombre = "Dr. Carlos",
            especialidad = "General"
        )

        val copia = original.copy(contacto = "+56987654321")

        assertEquals(original.id, copia.id)
        assertEquals(original.nombre, copia.nombre)
        assertEquals("+56987654321", copia.contacto)
    }
}
