@file:Suppress("unused")
package com.example.clinicaveterinariaapp.datos

/**
 * Repositorio en memoria para usuarios.
 *
 * Mejoras realizadas:
 * - Se devuelven copias de la lista para no exponer el mutable interno.
 * - Operaciones mutadoras sincronizadas para seguridad en acceso concurrente.
 * - Nuevas utilidades: obtenerPorCorreo y existeUsuario.
 */
object RepositorioUsuarios {
    private val usuarios = mutableListOf(
        Usuario("test@test.com", "123456", "Usuario de Prueba")
    )

    /**
     * Devuelve una lista inmutable copia de todos los usuarios.
     */
    fun obtenerTodos(): List<Usuario> = synchronized(usuarios) { usuarios.toList() }

    /**
     * Agrega un usuario al repositorio. Si ya existe un usuario con el mismo correo (case-insensitive),
     * no se agrega.
     */
    fun agregarUsuario(usuario: Usuario): Boolean = synchronized(usuarios) {
        val existe = usuarios.any { it.correo.equals(usuario.correo, ignoreCase = true) }
        return if (existe) {
            false
        } else {
            usuarios.add(usuario)
            true
        }
    }

    /**
     * Actualiza el usuario que tiene el correo `correoAnterior` por `usuarioActualizado`.
     * Devuelve true si se actualizó, false si no se encontró.
     */
    @Suppress("unused")
    fun actualizarUsuario(correoAnterior: String, usuarioActualizado: Usuario): Boolean = synchronized(usuarios) {
        val indice = usuarios.indexOfFirst { it.correo.equals(correoAnterior, ignoreCase = true) }
        return if (indice != -1) {
            usuarios[indice] = usuarioActualizado
            true
        } else {
            false
        }
    }

    /**
     * Elimina el usuario con el correo indicado. Devuelve true si se eliminó algún usuario.
     */
    @Suppress("unused")
    fun eliminarUsuario(correo: String): Boolean = synchronized(usuarios) {
        usuarios.removeIf { it.correo.equals(correo, ignoreCase = true) }
    }

    /**
     * Obtiene un usuario por su correo (case-insensitive) o null si no existe.
     */
    @Suppress("unused")
    fun obtenerPorCorreo(correo: String): Usuario? = synchronized(usuarios) {
        usuarios.firstOrNull { it.correo.equals(correo, ignoreCase = true) }
    }

    /**
     * Indica si existe un usuario con el correo dado.
     */
    @Suppress("unused")
    fun existeUsuario(correo: String): Boolean = synchronized(usuarios) {
        usuarios.any { it.correo.equals(correo, ignoreCase = true) }
    }
}
