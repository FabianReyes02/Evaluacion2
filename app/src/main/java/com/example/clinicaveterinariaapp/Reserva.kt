package com.example.clinicaveterinariaapp

/** Modelo simple para una reserva de atención veterinaria  */

data class Reserva(
    val id: String,
    val nombreMascota: String,
    val nombrePropietario: String,
    val contacto: String,
    val fecha: String, // formato YYYY-MM-DD
    val hora: String, // formato HH:mm
    val especialista: String,
    val estado: EstadoReserva = EstadoReserva.AGENDADA,
    val notas: String? = null
)

enum class EstadoReserva { AGENDADA, CANCELADA, COMPLETADA }

