package com.example.clinicaveterinariaapp

/** Repositorio en memoria de usuarios; contiene un usuario por defecto para pruebas y operaciones CRUD. */
object RepositorioUsuarios {
    private val usuarios = mutableListOf(
        Usuario(correo = "test@vet.com", contrasena = "123456", nombre = "Usuario Prueba")
    )

    // CRUD básico
    fun agregarUsuario(usuario: Usuario) {
        // evitar duplicados por correo
        if (usuarios.none { it.correo.equals(usuario.correo, ignoreCase = true) }) {
            usuarios.add(usuario)
        }
    }

    fun actualizarUsuario(correoAnterior: String, usuarioActualizado: Usuario): Boolean {
        val idx = usuarios.indexOfFirst { it.correo.equals(correoAnterior, ignoreCase = true) }
        return if (idx != -1) {
            usuarios[idx] = usuarioActualizado
            true
        } else false
    }

    fun eliminarUsuario(correo: String): Boolean {
        return usuarios.removeIf { it.correo.equals(correo, ignoreCase = true) }
    }

    fun obtenerTodos(): List<Usuario> = usuarios.toList()

    fun validarCredenciales(correo: String, contrasena: String): Boolean {
        return usuarios.any { it.correo.equals(correo.trim(), ignoreCase = true) && it.contrasena == contrasena }
    }

    fun obtenerUsuarioPorDefecto(): Usuario = usuarios.first()
}
