package com.martinmarcos.supermitreapp.model

data class LoginResponse(
    val token: String,
    val user: UserResponse
)

data class UserResponse(
    val id: Int,
    val nombre: String,
    val rol: String
)