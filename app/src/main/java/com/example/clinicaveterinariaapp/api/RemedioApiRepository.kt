package com.example.clinicaveterinariaapp.api

class RemedioApiRepository(private val service: RemedioApiService) {
    suspend fun obtenerTodos() = service.obtenerTodos()
    suspend fun crear(dto: RemedioDto) = service.crear(dto)
    suspend fun eliminar(id: String) = service.eliminar(id)
}

