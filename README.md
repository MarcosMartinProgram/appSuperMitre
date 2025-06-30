# 🛒 App Super Mitre – Tienda de Comestibles

Aplicación Android desarrollada con Kotlin que permite a los usuarios visualizar productos, gestionar su carrito, iniciar sesión o registrarse, y realizar pedidos. Conectada a una base de datos MySQL y backend propio, simula la funcionalidad de una tienda de comestibles con distintos rubros.

---

## 📱 Funcionalidades principales

- Vista de productos organizados por rubros (almacén, bebidas, carnes, panificados, etc.)
- Registro e inicio de sesión de usuarios
- Carrito de compras con posibilidad de agregar, quitar y modificar cantidades
- Pantalla de resumen de compra y confirmación
- Conexión con backend y base de datos MySQL externa
- Imagen de promociones centralizada y actualizable sin recompilar la app
- Interfaz con logo superior en todas las pantallas

---

## 🛠️ Tecnologías y herramientas

- **Lenguaje:** Kotlin
- **Plataforma:** Android Studio
- **Base de datos:** MySQL
- **Backend:** PHP / Node.js (según configuración)
- **HTTP:** Retrofit o llamadas HTTP (a configurar)
- **Persistencia local:** SharedPreferences (para login)
- **Diseño UI:** ConstraintLayout, RecyclerView

---

## 🚀 Cómo correr el proyecto

1. Clonar este repositorio:
```bash
git clone https://github.com/MarcosMartinProgram/appSuperMitre.git
```
2. Abrir el proyecto en Android Studio.
3. Configurar el archivo de conexión (URL del backend/API).
4. Ejecutar en emulador o dispositivo físico.
   
🌐 Backend y base de datos
Requiere un servidor con acceso a la base de datos comestibles_db que contenga las tablas de productos, usuarios y pedidos.

El backend debe exponer las rutas necesarias para:

Autenticación de usuarios

Listado de productos por rubro

Registro de pedidos

🧪 Estado del proyecto
✅ Módulo de login y registro
✅ Pantalla principal y rubros
✅ Carrito de compras
🔄 En desarrollo: integración completa con backend y envío de pedidos

📩 Contacto
📧 martinmarcospablo@gmail.com
🔗 www.linkedin.com/in/marcosmartinprogram 
