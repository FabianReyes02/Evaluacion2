package com.example.clinicaveterinariaapp.api

class ProfesionalApiRepository(private val service: ProfesionalApiService) {
    suspend fun obtenerTodos() = service.obtenerTodos()
    suspend fun crear(dto: ProfesionalDto) = service.crear(dto)
    suspend fun eliminar(id: String) = service.eliminar(id)
}

