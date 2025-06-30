// /ui/CompraScreen.kt
package com.martinmarcos.supermitreapp.ui

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.martinmarcos.supermitreapp.model.Rubro
import com.martinmarcos.supermitreapp.network.ApiService
import com.martinmarcos.supermitreapp.utils.Carrito
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import com.martinmarcos.supermitreapp.viewmodel.ImagenViewModel

@Composable
fun CompraScreen() {
    var rubros by remember { mutableStateOf<List<Rubro>>(emptyList()) }
    var selectedRubro by remember { mutableStateOf<Rubro?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val carrito = remember { Carrito() }
    var enCarrito by remember { mutableStateOf(false) }  // Estado para alternar entre pantallas

    // Obtener rubros al iniciar la pantalla
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                rubros = ApiService.create().getRubros()
                Log.d("CompraScreen", "Rubros obtenidos: ${rubros.size}")
            } catch (e: Exception) {
                Log.e("CompraScreen", "Error al obtener rubros", e)
            }
        }
    }
    val imagenViewModel: ImagenViewModel = viewModel()

    // Alternar entre pantalla de compra y carrito
    if (enCarrito) {
        CarritoScreen(carrito = carrito, onCerrarCarrito = { enCarrito = false }, imagenViewModel = imagenViewModel) // Mostrar el carrito
    } else {
        // Pantalla de compra
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (rubros.isNotEmpty()) {
                RubrosCarousel(
                    rubros = rubros,
                    onRubroSelected = { rubro ->
                        selectedRubro = rubro
                        Log.d("CompraScreen", "Rubro seleccionado: ${rubro.nombre}")
                    }
                )
            } else {
                Text(
                    text = "Cargando rubros...",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Contenedor con Scroll para productos
            Box(modifier = Modifier.weight(1f)) {
                selectedRubro?.let { rubro ->
                    ProductosList(
                        rubro = rubro,
                        onAgregarAlCarrito = { producto ->
                            carrito.agregarProducto(producto)
                            Log.d("CompraScreen", "Producto agregado al carrito: ${producto.nombre}")
                        }
                    )
                } ?: Text(
                    text = "Selecciona un rubro para ver los productos",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sección fija del total del carrito y botón de ir al carrito
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primaryContainer,
                shadowElevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Total del carrito: $${carrito.calcularTotal()}",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Button(
                        onClick = { enCarrito = true },
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                    ) {
                        Text(text = "Ver Carrito")
                    }
                }
            }
        }
    }
}
