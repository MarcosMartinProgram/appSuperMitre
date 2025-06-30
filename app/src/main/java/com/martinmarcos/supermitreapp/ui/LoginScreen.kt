// /ui/LoginScreen.kt
package com.martinmarcos.supermitreapp.ui

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.martinmarcos.supermitreapp.network.ApiService
import com.martinmarcos.supermitreapp.model.LoginCredentials
import com.martinmarcos.supermitreapp.utils.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    // Obtén el contexto para usar SessionManager
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Iniciar Sesión",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                val credentials = LoginCredentials(email, password)
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val response = ApiService.create().loginUser(credentials)
                        withContext(Dispatchers.Main) {
                            // Guardar el token en SessionManager
                            sessionManager.saveAuthToken(response.token)
                            Log.d("LoginScreen", "Token guardado: ${response.token}") // Log para depuración
                            message = "Inicio de sesión exitoso. "

                            // Navegar a la pantalla de compra
                            navController.navigate("compra") {
                                popUpTo("login") { inclusive = true } // Elimina la pantalla de login del stack
                            }
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            message = "Error: ${e.message}"
                            Log.e("LoginScreen", "Error en el login", e) // Log para depuración
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Iniciar Sesión")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = {
                // Navegar a la pantalla de registro
                navController.navigate("register")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Registrarse")
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = message)
    }
}