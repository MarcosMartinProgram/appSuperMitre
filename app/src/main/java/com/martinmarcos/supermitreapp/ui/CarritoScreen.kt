//ui/Carrito/Screen.kt
package com.martinmarcos.supermitreapp.ui


import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import android.net.Uri

import androidx.compose.ui.unit.dp
import com.martinmarcos.supermitreapp.model.CarritoItem
import com.martinmarcos.supermitreapp.utils.Carrito
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.*
import okhttp3.*
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.martinmarcos.supermitreapp.viewmodel.ImagenViewModel

@Composable
fun CarritoScreen(carrito: Carrito, onCerrarCarrito: () -> Unit, imagenViewModel: ImagenViewModel = viewModel()) {
    var loading by remember { mutableStateOf(false) }
    var preferenceId by remember { mutableStateOf<String?>(null) }
    val contexto = LocalContext.current
    val imagenes by imagenViewModel.imagenes

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {


        imagenes["logo"]?.let { url ->
            AsyncImage(model = url, contentDescription = "Logo")
        }

        Text(text = "Carrito de Compras", style = MaterialTheme.typography.titleLarge)

        imagenes["baner"]?.let { url ->
            AsyncImage(model = url, contentDescription = "baner")
        }

        if (carrito.items.isEmpty()) {
            Text(text = "Tu carrito está vacío.", style = MaterialTheme.typography.bodyMedium)
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(carrito.items) { item -> CarritoItemRow(item, carrito) }
            }

            Text(
                text = "Total: $${carrito.calcularTotal()}",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            Button(
                onClick = {
                    loading = true
                    CoroutineScope(Dispatchers.IO).launch {
                        val response = crearPreferenciaEnBackend(carrito)
                        withContext(Dispatchers.Main) {
                            response?.let {
                                preferenceId = it
                                val url = "https://www.mercadopago.com.ar/checkout/v1/redirect?preference-id=$it"
                                val customTabsIntent = CustomTabsIntent.Builder().build()
                                customTabsIntent.launchUrl(contexto, Uri.parse(url))
                            } ?: Log.e("MercadoPago", "Error al obtener la preferencia")
                            loading = false
                        }
                    }
                },
                enabled = !loading
            ) {
                if (loading) {
                    CircularProgressIndicator()
                } else {
                    Text("Pagar con Mercado Pago")
                }
            }
        }
    }
}

suspend fun crearPreferenciaEnBackend(carrito: Carrito): String? {
    return try {
        val url = "https://cacmarcos.alwaysdata.net/api/mercadopago/crear-preferencia"
        val requestBody = JSONObject().apply {
            put("carrito", JSONArray(carrito.items.map { item ->
                JSONObject().apply {
                    put("nombre", item.producto.nombre)
                    put("precio", item.producto.precio)
                    put("cantidad", item.cantidad)
                }
            }))
        }
        val response = withContext(Dispatchers.IO) {
            val request = Request.Builder()
                .url(url)
                .post(requestBody.toString().toRequestBody("application/json".toMediaType()))
                .build()
            OkHttpClient().newCall(request).execute()
        }
        if (response.isSuccessful) {
            val jsonResponse = JSONObject(response.body?.string() ?: "{}")
            jsonResponse.getString("id") // ID de la preferencia
        } else {
            null
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}



@Composable
fun CarritoItemRow(item: CarritoItem, carrito: Carrito) {
    val cantidadState = rememberUpdatedState(item.cantidad) // Asegura la recomposición

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = item.producto.nombre, style = MaterialTheme.typography.titleMedium)
            Text(text = "Cantidad: ${cantidadState.value}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Precio unitario: $${item.producto.precio}", style = MaterialTheme.typography.bodyMedium)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { carrito.agregarProducto(item.producto) }) {
                    Text("+")
                }
                Button(onClick = { carrito.disminuirProducto(item.producto) }) {
                    Text("-")
                }
                Button(onClick = { carrito.eliminarProducto(item.producto) }) {
                    Text("Eliminar")
                }
            }
        }
    }
}

