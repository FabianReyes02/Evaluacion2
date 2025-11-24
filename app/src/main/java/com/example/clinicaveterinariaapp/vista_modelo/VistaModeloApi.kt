package com.example.clinicaveterinariaapp.vista_modelo

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clinicaveterinariaapp.api.ApiRepository
import com.example.clinicaveterinariaapp.api.EjemploApiService
import com.example.clinicaveterinariaapp.api.PostDto
import com.example.clinicaveterinariaapp.api.RetrofitClient
import kotlinx.coroutines.launch

class VistaModeloApi : ViewModel() {
    private val service: EjemploApiService = RetrofitClient.instance.create(EjemploApiService::class.java)
    private val repo = ApiRepository(service)

    var posts = mutableStateListOf<PostDto>()
        private set
    var estaCargando = mutableStateOf(false)
        private set
    var errorMsg = mutableStateOf<String?>(null)
        private set

    fun cargarPosts() {
        estaCargando.value = true
        errorMsg.value = null
        viewModelScope.launch {
            try {
                val resp = repo.fetchPosts()
                if (resp.isSuccessful) {
                    posts.clear()
                    resp.body()?.let { posts.addAll(it) }
                } else {
                    errorMsg.value = "Error: ${resp.code()}"
                }
            } catch (e: Exception) {
                errorMsg.value = e.message
            } finally {
                estaCargando.value = false
            }
        }
    }
}
