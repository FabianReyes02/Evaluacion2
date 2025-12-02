package com.example.clinicaveterinariaapp.api

/** DTO para reservas */
data class ReservaDto(
    val id: String?,
    val nombreMascota: String,
    val nombrePropietario: String,
    val contacto: String,
    val fecha: String,
    val hora: String,
    val especialista: String,
    val remedio: String?,
    val notas: String?,
    val estado: String?
)
