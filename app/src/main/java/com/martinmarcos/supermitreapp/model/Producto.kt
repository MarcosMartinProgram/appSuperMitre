package com.martinmarcos.supermitreapp.model

data class Producto(
    val codigo_barras: Long,
    val nombre: String,
    val descripcion: String,
    val precio: String, // El precio es un String en la API
    val stock: Int,
    val id_rubro: Int,
    val imagen_url: String,
    val created_at: String?, // Fecha en formato String
    val updated_at: String? // Fecha en formato String
)