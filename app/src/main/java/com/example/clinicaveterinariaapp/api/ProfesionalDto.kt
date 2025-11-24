package com.example.clinicaveterinariaapp.api

/** DTO usado para transferir profesionales al/desde la API */
data class ProfesionalDto(
    val id: String?,
    val nombre: String,
    val especialidad: String,
    val contacto: String?,
    val descripcion: String?
)

