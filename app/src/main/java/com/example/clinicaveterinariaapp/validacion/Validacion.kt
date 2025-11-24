package com.example.clinicaveterinariaapp.validacion

import android.util.Patterns

/**
 * Representa el resultado de una operación de validación.
 */
sealed class ResultadoValidacion {
    /**
     * Indica que la validación fue exitosa.
     */
    object Exito : ResultadoValidacion()

    /**
     * Indica que la validación falló, y contiene el mensaje de error.
     * @property mensaje El mensaje de error que se debe mostrar.
     */
    data class Error(val mensaje: String) : ResultadoValidacion()
}

/**
 * Un objeto singleton que contiene funciones de validación reutilizables para el formulario.
 */
object Validador {

    /**
     * Valida el nombre de un usuario o mascota.
     * @param nombre El nombre a validar.
     * @return [ResultadoValidacion.Exito] si es válido, o [ResultadoValidacion.Error] si no lo es.
     */
    fun validarNombre(nombre: String): ResultadoValidacion {
        return if (nombre.isBlank()) {
            ResultadoValidacion.Error("El nombre es obligatorio")
        } else {
            ResultadoValidacion.Exito
        }
    }

    /**
     * Valida una dirección de correo electrónico.
     * @param correo El correo a validar.
     * @return [ResultadoValidacion.Exito] si es válido, o [ResultadoValidacion.Error] si no lo es.
     */
    fun validarCorreo(correo: String): ResultadoValidacion {
        return when {
            correo.isBlank() -> ResultadoValidacion.Error("El correo es obligatorio")
            !Patterns.EMAIL_ADDRESS.matcher(correo).matches() -> ResultadoValidacion.Error("El formato del correo es inválido")
            else -> ResultadoValidacion.Exito
        }
    }

    /**
     * Valida una contraseña.
     * @param contrasena La contraseña a validar.
     * @return [ResultadoValidacion.Exito] si es válida, o [ResultadoValidacion.Error] si no lo es.
     */
    fun validarContrasena(contrasena: String): ResultadoValidacion {
        return when {
            contrasena.isBlank() -> ResultadoValidacion.Error("La contraseña es obligatoria")
            contrasena.length < 6 -> ResultadoValidacion.Error("La contraseña debe tener al menos 6 caracteres")
            else -> ResultadoValidacion.Exito
        }
    }
}
