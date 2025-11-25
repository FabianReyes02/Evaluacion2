package com.example.clinicaveterinariaapp

import com.example.clinicaveterinariaapp.datos.RepositorioProfesionales
import com.example.clinicaveterinariaapp.datos.RepositorioUsuarios
import com.example.clinicaveterinariaapp.datos.Usuario
import com.example.clinicaveterinariaapp.datos.Profesional
import com.example.clinicaveterinariaapp.validacion.Validador
import com.example.clinicaveterinariaapp.validacion.ResultadoValidacion
import org.junit.Assert.*
import org.junit.Test
import java.util.*

class PruebasSimples {

    @Test
    fun validarNombre_vacio_devuelveError() {
        val res = Validador.validarNombre("")
        assertTrue(res is ResultadoValidacion.Error)
        assertEquals("El nombre es obligatorio", (res as ResultadoValidacion.Error).mensaje)
    }

    @Test
    fun validarCorreo_valido_devuelveExito() {
        val res = Validador.validarCorreo("usuario@dominio.com")
        assertTrue(res is ResultadoValidacion.Exito)
    }

    @Test
    fun validarCorreo_invalido_devuelveError() {
        val res = Validador.validarCorreo("no_es_correo")
        assertTrue(res is ResultadoValidacion.Error)
    }

    @Test
    fun validarContrasena_corta_devuelveError() {
        val res = Validador.validarContrasena("123")
        assertTrue(res is ResultadoValidacion.Error)
    }

    @Test
    fun usuario_data_class_igualdad_por_campos() {
        val a = Usuario("a@a.com", "pass", "Nombre")
        val b = Usuario("a@a.com", "pass", "Nombre")
        assertEquals(a, b)
    }

    @Test
    fun repositorioUsuarios_contiene_usuario_prueba() {
        val todos = RepositorioUsuarios.obtenerTodos()
        assertTrue(todos.any { it.correo.equals("test@test.com", ignoreCase = true) })
    }

    @Test
    fun repositorioUsuarios_obtenerPorCorreo_devuelve_usuario() {
        val u = RepositorioUsuarios.obtenerPorCorreo("test@test.com")
        assertNotNull(u)
        assertEquals("test@test.com", u!!.correo)
    }

    @Test
    fun repositorioUsuarios_agregarDuplicado_devuelveFalse() {
        val agregado = RepositorioUsuarios.agregarUsuario(Usuario("test@test.com", "123456", "Usuario de Prueba"))
        assertFalse(agregado)
    }

    @Test
    fun repositorioProfesionales_obtenerNombres_contiene_semilla() {
        val nombres = RepositorioProfesionales.obtenerNombres()
        assertTrue(nombres.any { it.contains("Dr.") || it.contains("Dra.") })
    }

    @Test
    fun repositorioProfesionales_agregar_y_eliminar() {
        val id = UUID.randomUUID().toString()
        val p = Profesional(id = id, nombre = "Test Pro", especialidad = "Test")
        RepositorioProfesionales.agregar(p)
        val encontrado = RepositorioProfesionales.obtenerPorId(id)
        assertNotNull(encontrado)
        val eliminado = RepositorioProfesionales.eliminar(id)
        assertTrue(eliminado)
        val encontradoDespues = RepositorioProfesionales.obtenerPorId(id)
        assertNull(encontradoDespues)
    }
}

