package com.martinmarcos.supermitreapp.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.martinmarcos.supermitreapp.model.Ticket
import com.martinmarcos.supermitreapp.network.ApiService
import com.martinmarcos.supermitreapp.ui.theme.SuperMitreAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CongratsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Obtener el intent que inició esta actividad
        val intent: Intent = intent
        val data: Uri? = intent.data

        // Verificar si el intent contiene datos (Deep Link)
        if (data != null) {
            val status = data.getQueryParameter("status") // Obtener el estado del pago

            // Mostrar la pantalla de confirmación
            setContent {
                CongratsScreen(status)
            }

            // Si el pago fue exitoso, enviar los detalles del ticket al backend
            if (status == "success") {
                // Datos del ticket (debes obtenerlos de donde los tengas almacenados)
                val productos = "Producto1, Producto2" // Reemplaza con los productos reales
                val descuento = 10.0 // Reemplaza con el descuento real
                val total = 100.0 // Reemplaza con el total real

                // Crear el objeto Ticket
                val ticket = Ticket(productos, descuento, total)

                // Enviar el ticket al backend
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val response = ApiService.create().saveTicket(ticket)
                        withContext(Dispatchers.Main) {
                            Log.d("CongratsActivity", "Ticket guardado en el backend")
                        }
                    } catch (e: Exception) {
                        Log.e("CongratsActivity", "Error al guardar el ticket", e)
                    }
                }
            }
        } else {
            // Si no hay datos, finalizar la actividad
            finish()
        }
    }
}

@Composable
fun CongratsScreen(status: String?) {
    SuperMitreAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (status) {
                    "success" -> {
                        Text("¡Pago exitoso!", style = MaterialTheme.typography.headlineMedium)
                        Spacer(modifier = Modifier.height(20.dp))
                        Text("Su compra ha sido procesada correctamente.")
                    }
                    "failure" -> {
                        Text("Pago fallido", style = MaterialTheme.typography.headlineMedium)
                        Spacer(modifier = Modifier.height(20.dp))
                        Text("Hubo un problema con su pago. Por favor, intente nuevamente.")
                    }
                    "pending" -> {
                        Text("Pago pendiente", style = MaterialTheme.typography.headlineMedium)
                        Spacer(modifier = Modifier.height(20.dp))
                        Text("Su pago está pendiente de confirmación.")
                    }
                    else -> {
                        Text("Estado desconocido", style = MaterialTheme.typography.headlineMedium)
                    }
                }
            }
        }
    }
}