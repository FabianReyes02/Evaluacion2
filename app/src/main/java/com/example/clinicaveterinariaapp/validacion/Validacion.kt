package com.example.clinicaveterinariaapp.validacion

import android.util.Patterns

sealed class ResultadoValidacion {
    object Exito : ResultadoValidacion()
    data class Error(val mensaje: String) : ResultadoValidacion()
}

object Validador {

    fun validarNombre(nombre: String): ResultadoValidacion {
        return if (nombre.isBlank()) {
            ResultadoValidacion.Error("El nombre es obligatorio")
        } else {
            ResultadoValidacion.Exito
        }
    }

    fun validarCorreo(correo: String): ResultadoValidacion {
        if (correo.isBlank()) return ResultadoValidacion.Error("El correo es obligatorio")

        val esValido = try {
            // En Android normal usamos Patterns.EMAIL_ADDRESS; en tests JVM esto puede ser null
            val pattern = Patterns.EMAIL_ADDRESS
            pattern?.matcher(correo)?.matches()
        } catch (t: Throwable) {
            null
        } ?: Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$").matches(correo)

        return if (!esValido) ResultadoValidacion.Error("El formato del correo es inválido")
        else ResultadoValidacion.Exito
    }

    fun validarContrasena(contrasena: String): ResultadoValidacion {
        return when {
            contrasena.isBlank() -> ResultadoValidacion.Error("La contraseña es obligatoria")
            contrasena.length < 6 -> ResultadoValidacion.Error("La contraseña debe tener al menos 6 caracteres")
            else -> ResultadoValidacion.Exito
        }
    }
}
