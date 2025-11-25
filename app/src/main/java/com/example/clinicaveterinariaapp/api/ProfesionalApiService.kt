package com.example.clinicaveterinariaapp.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ProfesionalApiService {
    @GET("/profesionales")
    suspend fun obtenerTodos(): Response<List<ProfesionalDto>>

    @POST("/profesionales")
    suspend fun crear(@Body dto: ProfesionalDto): Response<ProfesionalDto>

    @DELETE("/profesionales/{id}")
    suspend fun eliminar(@Path("id") id: String): Response<Unit>
}
