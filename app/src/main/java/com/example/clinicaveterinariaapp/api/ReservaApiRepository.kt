package com.example.clinicaveterinariaapp.api

class ReservaApiRepository(private val service: ReservaApiService) {
    suspend fun obtenerTodas() = service.obtenerTodas()
    suspend fun obtenerPorEstado(estado: String) = service.obtenerPorEstado(estado)
    suspend fun obtenerPorFecha(fecha: String) = service.obtenerPorFecha(fecha)
    suspend fun crear(dto: ReservaDto) = service.crear(dto)
    suspend fun actualizar(id: String, dto: ReservaDto) = service.actualizar(id, dto)
    suspend fun eliminar(id: String) = service.eliminar(id)
}
