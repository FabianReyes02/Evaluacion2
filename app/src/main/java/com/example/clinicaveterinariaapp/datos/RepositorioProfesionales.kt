package com.example.clinicaveterinariaapp.datos

import java.util.*

/** Repositorio en memoria para profesionales/especialistas */
object RepositorioProfesionales {
    private val profesionales = mutableListOf(
        Profesional(id = UUID.randomUUID().toString(), nombre = "Dr. Juan Pérez", especialidad = "General", contacto = "+56912345678", descripcion = "Atiende consultas generales para mascotas"),
        Profesional(id = UUID.randomUUID().toString(), nombre = "Dra. Ana López", especialidad = "Dermatología", contacto = "+56987654321", descripcion = "Especialista en problemas de piel")
    )

    fun agregar(profesional: Profesional) {
        if (profesionales.none { it.id == profesional.id }) profesionales.add(profesional)
    }

    fun actualizar(id: String, actualizado: Profesional): Boolean {
        val idx = profesionales.indexOfFirst { it.id == id }
        return if (idx != -1) {
            profesionales[idx] = actualizado
            true
        } else false
    }

    fun eliminar(id: String): Boolean {
        return profesionales.removeIf { it.id == id }
    }

    fun obtenerTodos(): List<Profesional> = profesionales.toList()

    fun obtenerNombres(): List<String> = profesionales.map { it.nombre }

    fun obtenerPorId(id: String): Profesional? = profesionales.find { it.id == id }
}
