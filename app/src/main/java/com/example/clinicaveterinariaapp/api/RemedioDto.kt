package com.example.clinicaveterinariaapp.api

/** DTO para remedios/medicamentos */
data class RemedioDto(
    val id: String?,
    val nombre: String,
    val descripcion: String?,
    val dosis: String?,
    val presentacion: String?
)

