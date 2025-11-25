# 🐾 VetApp (ClinicaVeterinariaApp)

Proyecto de la asignatura Desarrollo de Aplicaciones Móviles.

## 📝 Descripción breve

Aplicación móvil para gestionar elementos básicos de una clínica veterinaria: usuarios, profesionales y remedios. Este repo incluye la app Android (Jetpack Compose + Kotlin) y un microservicio de desarrollo sencillo (Node.js + Express) para probar las integraciones API localmente.

## 🔧 Qué incluye

- App Android (módulo `app`) escrita en Kotlin con Jetpack Compose.
- Microservicio local para desarrollo en `microservice/` (endpoints REST en memoria).
- Tests unitarios simples en `app/src/test/...`.

## 🧭 Microservicio de desarrollo (rápido)

El microservicio es intencionadamente ligero y mantiene datos en memoria. Sirve para pruebas y demos.

Ruta: `microservice/index.js`

Comandos (PowerShell):

```powershell
cd microservice
npm install
npm start
```

Salida esperada:

```
Microservicio corriendo en http://localhost:3000
```

> Nota: para desarrollo con un emulador Android, la app debe apuntar a `http://10.0.2.2:3000/`. Ya se configuró `BuildConfig.API_BASE_URL` en `app/build.gradle.kts` con ese valor para el emulador.

## 📚 Endpoints disponibles (microservicio)

- GET /posts
  - Demo: lista de posts (usado por `PantallaApiDemo`).
- GET /profesionales
  - Lista de profesionales.
- POST /profesionales
  - Crear profesional (envíe JSON con campos: `nombre`, `especialidad`, `contacto`, `descripcion`).
- DELETE /profesionales/:id
  - Eliminar profesional por id.
- GET /remedios
  - Lista de remedios.
- POST /remedios
  - Crear remedio (campos: `nombre`, `descripcion`, `dosis`, `presentacion`).
- DELETE /remedios/:id
  - Eliminar remedio por id.

Estos endpoints devuelven/aceptan JSON.

## 📱 Ejecutar la app Android (emulador)

1. Asegúrate de tener el microservicio corriendo (`npm start`) si quieres probar integración.
2. Abrir el proyecto en Android Studio o usar Gradle desde la raíz del repo.

Comandos útiles (PowerShell):

```powershell
# Compilar
.\gradlew.bat clean assembleDebug

# Instalar en emulador conectado
.\gradlew.bat installDebug

# Ejecutar tests unitarios
.\gradlew.bat testDebugUnitTest

# Ejecutar lint
.\gradlew.bat lintDebug
```

Si ejecutas la app en un dispositivo físico, edita `app/build.gradle.kts` y configura `API_BASE_URL` con la IP de tu máquina (por ejemplo "http://192.168.0.123:3000/") antes de compilar la app.

## ✅ Probar manualmente (curl)

Desde la máquina host (PowerShell):

```powershell
curl http://localhost:3000/posts
curl http://localhost:3000/profesionales
# POST ejemplo (remplaza comillas según shell)
curl -Method POST -ContentType 'application/json' -Body '{"nombre":"Dr Test","especialidad":"Cardio"}' http://localhost:3000/profesionales
```

Desde el emulador (si el microservicio corre en la misma máquina):

```powershell
curl http://10.0.2.2:3000/posts
```

## 🧪 Tests

- Los tests unitarios simples están en `app/src/test/java/com/example/clinicaveterinariaapp/PruebasSimples.kt`.
- Ejecuta `.\gradlew.bat testDebugUnitTest` para correr la suite local.

## 🔐 Notas de seguridad y producción

- El microservicio incluido es solo para desarrollo; NO usar en producción (datos en memoria, CORS abierto, sin autenticación).
- Para producción: añadir persistencia (BD), autenticación (JWT/OAuth), validación de entrada y HTTPS.

## 👥 Integrantes

| Integrante      | Rol                |
|-----------------|--------------------|
| Benjamín Ibañez | Backend/Frontend   |
| Fabián Reyes    | Backend/Frontend   |
| Matías Vargas   | Backend/Frontend   |


