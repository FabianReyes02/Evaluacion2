package com.example.clinicaveterinariaapp.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * API externa: OpenFDA Animal & Veterinary
 * Documentación: https://open.fda.gov/apis/animalandveterinary/event/
 */
interface VetDrugApiService {
    @GET("animal/event.json")
    suspend fun buscarMedicamentos(
        @Query("search") query: String = "*",
        @Query("limit") limit: Int = 20
    ): Response<VetDrugResponse>
}

data class VetDrugResponse(
    val results: List<VetDrugResult>?
)

data class VetDrugResult(
    val drug: List<VetDrugInfo>?,
    val reaction: List<VetReaction>?
)

data class VetDrugInfo(
    val brand_name: String?,
    val generic_name: String?,
    val manufacturer_name: String?,
    val product_ndc: String?
)

data class VetReaction(
    val veddra_term_name: String?
)

// Mantener para compatibilidad
interface EjemploApiService {
    @GET("/posts")
    suspend fun obtenerPosts(): Response<List<PostDto>>
}
