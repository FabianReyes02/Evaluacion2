package com.example.clinicaveterinariaapp

/** Modelo simple de usuario para autenticación */
data class Usuario(
    val correo: String,
    val contrasena: String,
    val nombre: String? = null
)

