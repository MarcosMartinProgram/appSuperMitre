// /network/ApiService
package com.martinmarcos.supermitreapp.network

import com.martinmarcos.supermitreapp.model.Imagen
import com.martinmarcos.supermitreapp.model.LoginCredentials
import com.martinmarcos.supermitreapp.model.LoginResponse
import com.martinmarcos.supermitreapp.model.Producto
import com.martinmarcos.supermitreapp.model.RegisterResponse
import com.martinmarcos.supermitreapp.model.Rubro
import com.martinmarcos.supermitreapp.model.Ticket
import com.martinmarcos.supermitreapp.model.User
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("auth/login") // Asegúrate de que la ruta coincida con tu backend
    suspend fun loginUser(@Body credentials: LoginCredentials): LoginResponse

    @POST("auth/register") // Método para registro
    suspend fun registerUser(@Body user: User): RegisterResponse

    @GET("rubros")
    suspend fun getRubros(): List<Rubro>
    @GET("imagenAndroid/imagenes")
    suspend fun getImagenes(): Map<String, String>

    @GET("productos/por-rubro/{id_rubro}")
    suspend fun getProductosPorRubro(@Path("id_rubro") rubroId: Int): List<Producto>

    @POST("tickets") // Asegúrate de que la ruta coincida con tu backend
    suspend fun saveTicket(@Body ticket: Ticket): Response<Unit> // Cambia `Response<Unit>` por el tipo de respuesta que esperas
    companion object {
        private const val BASE_URL = "https://cacmarcos.alwaysdata.net/api/" // Cambia esto por la URL de tu backend

        fun create(): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}