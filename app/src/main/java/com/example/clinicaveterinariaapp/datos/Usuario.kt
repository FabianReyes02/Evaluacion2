package com.example.clinicaveterinariaapp.datos

data class Usuario(
    val correo: String,
    val contrasena: String,
    val nombre: String? = null
)
