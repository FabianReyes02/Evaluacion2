package com.example.clinicaveterinariaapp.datos

/** Modelo para profesionales/especialistas */
data class Profesional(
    val id: String,
    val nombre: String,
    val especialidad: String,
    val contacto: String? = null,
    val descripcion: String? = null
)
