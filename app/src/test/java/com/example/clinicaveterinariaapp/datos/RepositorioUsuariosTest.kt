package com.example.clinicaveterinariaapp.datos

import org.junit.Assert.*
import org.junit.Test
import java.util.UUID

class RepositorioUsuariosTest {

    @Test
    fun agregarObtenerEliminar_usuario_nuevo_funciona() {
        val correo = "test+${UUID.randomUUID()}@example.com"
        val usuario = Usuario(correo, "pass123", "Usuario Test")

        // Agregar
        val agregado = RepositorioUsuarios.agregarUsuario(usuario)
        assertTrue("El usuario debería agregarse correctamente", agregado)

        // Existe
        val existe = RepositorioUsuarios.existeUsuario(correo)
        assertTrue("El usuario debería existir después de agregarse", existe)

        // Obtener
        val obtenido = RepositorioUsuarios.obtenerPorCorreo(correo)
        assertNotNull("Debería encontrarse el usuario por correo", obtenido)
        assertEquals(correo, obtenido?.correo)

        // Actualizar
        val usuarioActualizado = Usuario(correo, "newpass", "Usuario Actualizado")
        val actualizado = RepositorioUsuarios.actualizarUsuario(correo, usuarioActualizado)
        assertTrue("La actualización debería devolver true", actualizado)
        val obtenido2 = RepositorioUsuarios.obtenerPorCorreo(correo)
        assertEquals("Usuario Actualizado", obtenido2?.nombre)

        // Eliminar
        val eliminado = RepositorioUsuarios.eliminarUsuario(correo)
        assertTrue("La eliminación debería devolver true", eliminado)

        // Ya no existe
        val existe2 = RepositorioUsuarios.existeUsuario(correo)
        assertFalse("El usuario no debería existir tras la eliminación", existe2)
    }
}
