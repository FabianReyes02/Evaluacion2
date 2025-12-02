package com.example.clinicaveterinariaapp.vista_modelo

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clinicaveterinariaapp.api.RemedioApiRepository
import com.example.clinicaveterinariaapp.api.RemedioApiService
import com.example.clinicaveterinariaapp.api.RemedioDto
import com.example.clinicaveterinariaapp.api.RetrofitClient
import com.example.clinicaveterinariaapp.api.ExternalApiClient
import com.example.clinicaveterinariaapp.api.VetDrugApiService
import kotlinx.coroutines.launch
import java.util.*

class VistaModeloRemedios : ViewModel() {
    private val apiService: RemedioApiService = RetrofitClient.instance.create(RemedioApiService::class.java)
    private val apiRepo = RemedioApiRepository(apiService)
    private val externalApi: VetDrugApiService = ExternalApiClient.vetDrugService

    var remedios = mutableStateListOf<RemedioDto>()
        private set
    var estaCargando = mutableStateOf(false)
        private set
    var errorMsg = mutableStateOf<String?>(null)
        private set
    
    var remediosExternos = mutableStateListOf<String>()
        private set
    var cargandoExternos = mutableStateOf(false)
        private set

    // Fallback local con la lista que proporcionaste
    private val fallback = listOf(
        "Abamectina", "Ácido tolfenámico", "Adenosina trifosfato", "Albendazol", "Amoxicilina",
        "Ampicilina", "Antipirina", "Betametasona", "Biotina", "Bromhexina", "Carprofeno",
        "Cefadroxilo", "Cefalexina", "Cefoperazona", "Cefquinoma", "Ciclosporina", "Cipermetrina",
        "Ciprofloxacina", "Cloprostenol", "Cloranfenicol", "Clorsulón", "Closantel", "Cloxacilina",
        "Colistina", "Deltametrina", "Dexametasona", "Diminaceno", "Diclofenaco", "Dicloxacilina",
        "Diflubenzurón", "Dihidroestreptomicina", "Doramectina", "Doxiciclina", "Enrofloxacina",
        "Eritromicina", "Espectinomicina", "Espinosad", "Estreptomicina", "Fenbendazol", "Fenilbutazona",
        "Fipronil", "Florfenicol", "Fluazurón", "Flunixin", "Gentamicina", "Hidroclorotiazida",
        "Hierro dextrano", "Ivermectina", "Ketoprofeno", "L-Carnitina", "Levamisol", "Lincomicina",
        "Marbofloxacina", "Medroxiprogesterona", "Metamizol", "Metoclopramida", "Metronidazol",
        "Milbemicina", "Miltefosina", "Moxidectina", "Mupirocina", "Neomicina", "Neostigmina",
        "Nitroxinil", "Omeprazol", "Oxantel", "Oxfendazol", "Oxiclozanida", "Oxitetraciclina",
        "Oxitocina", "Paracetamol", "Penicilina", "Pimobendán", "Pirantel", "Piriproxifeno",
        "Praziquantel", "Ractopamina", "Rafoxanida", "Ranitidina", "Sulfadimethoxine", "Selamectina",
        "Sulfacloropiridazina", "Sulfadiazina", "Sulfadimetoxina", "Sulfadoxina", "Sulfametoxazol",
        "Sulfanilamida", "Sulfatiazol", "Sulfato de cobalto", "Tetraciclina", "Tiabendazol",
        "Tilmicosina", "Tilosina", "Toltrazurilo", "Tramadol", "Triclabendazol", "Trimetoprima",
        "Tulatromicina", "Vitaminas"
    )

    fun nombres(): List<String> {
        return if (remedios.isNotEmpty()) remedios.mapNotNull { it.nombre } else fallback
    }

    fun cargarRemedios() {
        estaCargando.value = true
        errorMsg.value = null
        viewModelScope.launch {
            try {
                val resp = apiRepo.obtenerTodos()
                if (resp.isSuccessful) {
                    remedios.clear()
                    resp.body()?.let { remedios.addAll(it) }
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

    fun crearRemedioRemoto(nombre: String, descripcion: String?, dosis: String?, presentacion: String?, onResultado: (Boolean, String?) -> Unit) {
        estaCargando.value = true
        viewModelScope.launch {
            try {
                val dto = RemedioDto(id = null, nombre = nombre, descripcion = descripcion, dosis = dosis, presentacion = presentacion)
                val resp = apiRepo.crear(dto)
                if (resp.isSuccessful) {
                    resp.body()?.let { remedios.add(0, it); onResultado(true, null) } ?: onResultado(false, "Respuesta vacía")
                } else {
                    onResultado(false, "Error API: ${resp.code()}")
                }
            } catch (e: Exception) { onResultado(false, e.message) }
            finally { estaCargando.value = false }
        }
    }

    fun eliminarRemedioRemoto(id: String, onResultado: (Boolean, String?) -> Unit) {
        estaCargando.value = true
        viewModelScope.launch {
            try {
                val resp = apiRepo.eliminar(id)
                if (resp.isSuccessful) {
                    remedios.removeIf { it.id == id }
                    onResultado(true, null)
                } else {
                    onResultado(false, "Error API: ${resp.code()}")
                }
            } catch (e: Exception) { onResultado(false, e.message) }
            finally { estaCargando.value = false }
        }
    }

    /**
     * Carga medicamentos veterinarios desde API externa (OpenFDA)
     */
    fun cargarRemediosExternos() {
        cargandoExternos.value = true
        viewModelScope.launch {
            try {
                val resp = externalApi.buscarMedicamentos(limit = 50)
                if (resp.isSuccessful) {
                    val drugs = resp.body()?.results
                        ?.flatMap { it.drug ?: emptyList() }
                        ?.mapNotNull { it.brand_name ?: it.generic_name }
                        ?.distinct()
                        ?.take(30) ?: emptyList()
                    
                    remediosExternos.clear()
                    remediosExternos.addAll(drugs)
                }
            } catch (e: Exception) {
                // Silencioso, la API externa es opcional
            } finally {
                cargandoExternos.value = false
            }
        }
    }
}
