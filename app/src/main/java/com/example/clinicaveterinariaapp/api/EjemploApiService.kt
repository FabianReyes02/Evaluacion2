package com.example.clinicaveterinariaapp.api

import retrofit2.Response
import retrofit2.http.GET

interface EjemploApiService {
    @GET("/posts")
    suspend fun obtenerPosts(): Response<List<PostDto>>
}
