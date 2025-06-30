package com.martinmarcos.supermitreapp.model


data class User(
    val nombre: String,
    val email: String,
    val password: String,
    val role: String = "comprador",
    val numero_whatsapp: String,
    val direccion: String
)