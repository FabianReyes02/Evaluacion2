package com.example.clinicaveterinariaapp.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RemedioApiService {
    @GET("/remedios")
    suspend fun obtenerTodos(): Response<List<RemedioDto>>

    @POST("/remedios")
    suspend fun crear(@Body remedio: RemedioDto): Response<RemedioDto>

    @DELETE("/remedios/{id}")
    suspend fun eliminar(@Path("id") id: String): Response<Unit>
}

