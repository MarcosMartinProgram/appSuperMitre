package com.martinmarcos.supermitreapp.model

data class Ticket(
    val productos: String, // Puedes usar un tipo más específico si es necesario
    val descuento: Double,
    val total: Double
)