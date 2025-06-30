// /ui/ProductosList.kt
package com.martinmarcos.supermitreapp.ui

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.martinmarcos.supermitreapp.model.Producto
import com.martinmarcos.supermitreapp.model.Rubro
import com.martinmarcos.supermitreapp.network.ApiService
import kotlinx.coroutines.launch

@Composable
fun ProductosList(rubro: Rubro, onAgregarAlCarrito: (Producto) -> Unit) {
    var productos by remember { mutableStateOf<List<Producto>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()

    // Obtener productos del rubro seleccionado
    LaunchedEffect(rubro.id_rubro) {
        coroutineScope.launch {
            try {
                Log.d("ProductosList", "Solicitando productos para el rubro: ${rubro.nombre}, ID: ${rubro.id_rubro}") // Log aquí
                productos = ApiService.create().getProductosPorRubro(rubro.id_rubro)
                Log.d("ProductosList", "Productos obtenidos: ${productos.size}") // Log aquí
                productos.forEach { producto ->
                    Log.d("ProductosList", "Producto: ${producto.nombre}, Precio: ${producto.precio}") // Log aquí
                }
            } catch (e: Exception) {
                Log.e("ProductosList", "Error al obtener productos", e)
            }
        }
    }

    if (productos.isNotEmpty()) {
        LazyColumn {
            items(productos) { producto ->
                ProductoItem(producto = producto,
                    onAgregarAlCarrito = onAgregarAlCarrito)
            }
        }
    } else {
        Text(
            text = "No hay productos disponibles para este rubro",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun ProductoItem(producto: Producto, onAgregarAlCarrito: (Producto) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally // Centrar elementos en la columna
        ) {
            AsyncImage(
                model = producto.imagen_url,
                contentDescription = producto.nombre,
                modifier = Modifier
                    .fillMaxWidth() // Para que la imagen ocupe el ancho de la card
                    .height(150.dp) // Ajusta la altura para mantener una buena relación de aspecto
                    .clip(RoundedCornerShape(8.dp)), // Bordes redondeados
                contentScale = ContentScale.Crop // Ajusta la imagen al tamaño manteniendo proporciones
            )
            Spacer(modifier = Modifier.height(8.dp)) // Espacio entre la imagen y el texto
            Text(
                text = producto.nombre,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center // Centrar texto
            )
            Text(
                text = producto.descripcion,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
            Text(
                text = "$${producto.precio}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp)) // Espacio antes del botón
            Button(
                onClick = { onAgregarAlCarrito(producto) },
                modifier = Modifier.fillMaxWidth(0.8f) // Botón más pequeño pero centrado
            ) {
                Text(text = "Agregar al carrito")
            }
        }
    }
}