package com.example.clinicaveterinariaapp.datos

object RepositorioProfesionales {
    private val profesionales = listOf(
        Profesional("Dr. Juan Pérez", "Cardiología"),
        Profesional("Dra. Ana Gómez", "Dermatología"),
        Profesional("Dr. Luis Torres", "Medicina Interna")
    )

    fun obtenerTodos(): List<Profesional> = profesionales

    fun obtenerNombres(): List<String> = profesionales.map { it.nombre }
}
