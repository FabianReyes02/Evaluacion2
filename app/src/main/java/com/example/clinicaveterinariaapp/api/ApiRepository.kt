package com.example.clinicaveterinariaapp.api

import retrofit2.Response

class ApiRepository(private val service: EjemploApiService) {
    suspend fun fetchPosts(): Response<List<PostDto>> = service.obtenerPosts()
}

