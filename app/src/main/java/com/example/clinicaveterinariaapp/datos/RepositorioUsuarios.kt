package com.example.clinicaveterinariaapp.datos

object RepositorioUsuarios {
    private val usuarios = mutableListOf(
        Usuario("test@test.com", "123456", "Usuario de Prueba")
    )

    fun obtenerTodos(): List<Usuario> = usuarios

    fun agregarUsuario(usuario: Usuario) {
        usuarios.add(usuario)
    }

    fun actualizarUsuario(correoAnterior: String, usuarioActualizado: Usuario): Boolean {
        val indice = usuarios.indexOfFirst { it.correo.equals(correoAnterior, ignoreCase = true) }
        return if (indice != -1) {
            usuarios[indice] = usuarioActualizado
            true
        } else {
            false
        }
    }

    fun eliminarUsuario(correo: String): Boolean {
        return usuarios.removeIf { it.correo.equals(correo, ignoreCase = true) }
    }
}
