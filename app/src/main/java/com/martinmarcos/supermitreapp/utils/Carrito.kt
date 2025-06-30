// /utils/Carrito.kt
package com.martinmarcos.supermitreapp.utils

import androidx.compose.runtime.mutableStateListOf
import com.martinmarcos.supermitreapp.model.Producto
import com.martinmarcos.supermitreapp.model.CarritoItem

class Carrito {
    val items = mutableStateListOf<CarritoItem>()

    // Agrega un producto al carrito
    fun agregarProducto(producto: Producto) {
        val itemIndex = items.indexOfFirst { it.producto.codigo_barras == producto.codigo_barras }
        if (itemIndex != -1) {
            // Reemplazamos el objeto para que Compose lo detecte como nuevo
            items[itemIndex] = items[itemIndex].copy(cantidad = items[itemIndex].cantidad + 1)
        } else {
            items.add(CarritoItem(producto, 1))
        }
    }

    // Elimina un producto completamente del carrito
    fun eliminarProducto(producto: Producto) {
        items.removeAll { it.producto.codigo_barras == producto.codigo_barras }
    }

    // Disminuye la cantidad de un producto o lo elimina si llega a 0
    fun disminuirProducto(producto: Producto) {
        val itemIndex = items.indexOfFirst { it.producto.codigo_barras == producto.codigo_barras }
        if (itemIndex != -1) {
            val nuevaCantidad = items[itemIndex].cantidad - 1
            if (nuevaCantidad > 0) {
                items[itemIndex] = items[itemIndex].copy(cantidad = nuevaCantidad)
            } else {
                items.removeAt(itemIndex) // Elimina el producto si la cantidad es 0
            }
        }
    }

    // Calcula el monto total del carrito
    fun calcularTotal(): Double {
        return items.sumOf { it.producto.precio.toDouble() * it.cantidad }
    }
}
