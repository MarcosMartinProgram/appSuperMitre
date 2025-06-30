package com.martinmarcos.supermitreapp.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.martinmarcos.supermitreapp.model.Rubro
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun RubrosCarousel(rubros: List<Rubro>, onRubroSelected: (Rubro) -> Unit) {
    if (rubros.isEmpty()) {
        Text(
            text = "No hay rubros disponibles",
            style = MaterialTheme.typography.bodyMedium
        )
        return
    }

    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp), // Acerca las flechas a los bordes
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                    }
                },
                enabled = pagerState.currentPage > 0
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Anterior")
            }

            HorizontalPager(
                state = pagerState,
                count = rubros.size,
                modifier = Modifier.weight(1f) // Ocupar el espacio disponible
            ) { page ->
                val rubro = rubros[page]
                Button(
                    onClick = {
                        Log.d("RubrosCarousel", "Rubro seleccionado: ${rubro.nombre}, ID: ${rubro.id_rubro}")
                        onRubroSelected(rubro)
                    },
                    modifier = Modifier.width(150.dp) // Tamaño fijo para que uno siempre quede centrado
                ) {
                    Text(
                        text = rubro.nombre,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            IconButton(
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                },
                enabled = pagerState.currentPage < rubros.size - 1
            ) {
                Icon(Icons.Default.ArrowForward, contentDescription = "Siguiente")
            }
        }

        // Indicador de página
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(rubros.size) { index ->
                val color = if (index == pagerState.currentPage) Color.Black else Color.Gray
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .padding(2.dp)
                        .background(color, shape = CircleShape)
                )
            }
        }
    }
}
