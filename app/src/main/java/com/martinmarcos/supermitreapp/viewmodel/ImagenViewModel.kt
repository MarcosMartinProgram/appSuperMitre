// /viewmodel/ImagenViewModel
package com.martinmarcos.supermitreapp.viewmodel

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martinmarcos.supermitreapp.network.ApiService
import kotlinx.coroutines.launch

class ImagenViewModel : ViewModel() {
    private val apiService = ApiService.create()
    var imagenes = mutableStateOf(mapOf<String, String>())
        private set

    init {
        viewModelScope.launch {
            try {
                imagenes.value = apiService.getImagenes()
            } catch (e: Exception) {
                Log.e("ImagenViewModel", "Error al cargar imágenes: ${e.message}")
            }
        }
    }
}

