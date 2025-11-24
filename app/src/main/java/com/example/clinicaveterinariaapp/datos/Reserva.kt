package com.example.clinicaveterinariaapp.datos

enum class EstadoReserva { AGENDADA, CUMPLIDA, CANCELADA }

data class Reserva(
    val id: String,
    val nombreMascota: String,
    val nombrePropietario: String,
    val contacto: String,
    val fecha: String, // YYYY-MM-DD
    val hora: String, // HH:mm
    val especialista: String,
    val notas: String? = null,
    val estado: EstadoReserva = EstadoReserva.AGENDADA
)
