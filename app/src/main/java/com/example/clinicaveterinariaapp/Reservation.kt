package com.example.clinicaveterinariaapp

/** Modelo simple para una reserva de atención veterinaria */
data class Reservation(
    val id: String,
    val petName: String,
    val ownerName: String,
    val contact: String,
    val date: String, // formato YYYY-MM-DD
    val time: String, // formato HH:mm
    val specialist: String,
    val status: ReservationStatus = ReservationStatus.SCHEDULED,
    val notes: String? = null
)

enum class ReservationStatus { SCHEDULED, CANCELLED, COMPLETED }

