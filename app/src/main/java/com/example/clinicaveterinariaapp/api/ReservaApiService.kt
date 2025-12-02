package com.example.clinicaveterinariaapp.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ReservaApiService {
    @GET("/reservas")
    suspend fun obtenerTodas(): Response<List<ReservaDto>>

    @GET("/reservas/estado/{estado}")
    suspend fun obtenerPorEstado(@Path("estado") estado: String): Response<List<ReservaDto>>

    @GET("/reservas/fecha/{fecha}")
    suspend fun obtenerPorFecha(@Path("fecha") fecha: String): Response<List<ReservaDto>>

    @POST("/reservas")
    suspend fun crear(@Body dto: ReservaDto): Response<ReservaDto>

    @PUT("/reservas/{id}")
    suspend fun actualizar(@Path("id") id: String, @Body dto: ReservaDto): Response<ReservaDto>

    @DELETE("/reservas/{id}")
    suspend fun eliminar(@Path("id") id: String): Response<Unit>
}
