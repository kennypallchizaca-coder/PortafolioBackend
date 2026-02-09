# 🧪 Ejercicios de Pruebas: Seguridad Avanzada y Endpoints

Este archivo es tu guía de entrenamiento para dominar la seguridad en Spring Boot. Irás desde lo básico hasta validaciones complejas de identidad.

---

## ⚠️ REGLA DE ORO
**NO crees archivos nuevos.** Trabaja directamente en tus controladores existentes (`UserController.java`, `AdvisoryController.java`, etc.) y en `SecurityConfig.java`.

---

## 🟢 NIVEL 1: Calentamiento (Básico)

### 📝 Ejercicio 1.1: Endpoint Público sin Autenticación
**Misión:** En `UserController.java`, crea un endpoint, pero hazlo **PÚBLICO**.

*   **Ruta:** `/api/users/prueba/publico` (Nota: El controlador ya tiene prefijo `/api/users`)
*   **Reto:** Tienes que modificar `SecurityConfig.java` para quitarle el candado a esta ruta específica usando `.permitAll()`.

✅ **Resultado Esperado:**
*   **Petición:** `GET /api/users/prueba/publico` (SIN TOKEN)
*   **Status:** `200 OK`
*   **Mensaje:** `"¡Hola! Soy un endpoint público."`

---

### 📝 Ejercicio 1.2: Endpoint Protegido (Cualquier Usuario Logueado)
**Misión:** En `UserController.java`, crea un endpoint sencillo.

*   **Ruta:** `/api/users/prueba/protegido-basico`
*   **Reto:** NO toques `SecurityConfig`. Por defecto, todo está cerrado. Solo intenta acceder con un token válido y verifica que funciona.

✅ **Resultado Esperado:**
*   **Petición:** `GET /api/users/prueba/protegido-basico` + Header `Authorization: Bearer TU_TOKEN`
*   **Status:** `200 OK`
*   **Mensaje:** `"Has entrado a la zona segura básica".`

❌ **Resultado (Error):**
*   **Sin Token:** `401 Unauthorized` (No tienes permiso).

---

## 🟡 NIVEL 2: Roles (Intermedio)

### 📝 Ejercicio 2.1: El Club VIP (Solo ADMIN)
**Misión:** Un endpoint que rechace a todos excepto a los administradores.

*   **Ruta:** `/api/users/prueba/zona-admin`
*   **Herramienta:** Usa la anotación `@PreAuthorize`.

✅ **Resultado Esperado (Usuario ADMIN):**
*   **Status:** `200 OK`
*   **Mensaje:** `"Bienvenido al club VIP, Admin."`.

❌ **Resultado (Usuario USER/PROGRAMMER):**
*   **Status:** `403 Forbidden`

---

### 📝 Ejercicio 2.2: Acceso Compartido (ADMIN o PROGRAMMER)
**Misión:** Un endpoint que permita entrar a administradores O programadores.

*   **Ruta:** `/api/users/prueba/staff-interno`
*   **Pista:** `@PreAuthorize` con `hasAnyRole`.

✅ **Resultado Esperado (ADMIN o PROGRAMMER):**
*   **Status:** `200 OK`

❌ **Resultado (USER):**
*   **Status:** `403 Forbidden`

---

## 🔴 NIVEL 3: Propiedad y Lógica Avanzada (Difícil)

### 📝 Ejercicio 3.1: "Solo puedo ver MI propia información"
**Misión:** Crea un endpoint que reciba un id por URL y solo te deje pasar si ese ID coincide con el ID de tu token.

*   **Ruta:** `/api/users/prueba/{uid}/mi-secreto`
*   **Lógica:**
    1.  Recibe el `uid` de la URL (`@PathVariable`).
    2.  Recibe al usuario logueado: `@AuthenticationPrincipal String currentUserId`.
    3.  **Comparación:** `if (!currentUserId.equals(uid))` -> Error 403.

✅ **Resultado Esperado:**
*   **UID Token == UID URL:** `200 OK`
*   **UID Token != UID URL:** `403 Forbidden`

---

### 📝 Ejercicio 3.2: Endpoint Híbrido (Difícil)
**Misión:** Crea un endpoint que responda diferente si eres ADMIN o si eres un mortal.

*   **Ruta:** `/api/users/prueba/nivel-acceso`
*   **Lógica:** Inyecta `HttpServletRequest request` y usa `request.isUserInRole("ADMIN")`.

✅ **Resultado Esperado (ADMIN):**
*   **Mensaje:** `"Bienvenido Jefe Supremo."`

✅ **Resultado Esperado (Usuario Normal):**
*   **Mensaje:** `"Hola usuario normal."`

---

# 🕵️‍♂️ PISTAS DE CÓDIGO (SOLUCIONES PARCIALES)

Aquí tienes los fragmentos de código que te ayudarán si te atascas. ¡Inténtalo primero sin mirar!

<details>
<summary>👀 Ver Pista Ejercicio 1.1 (SecurityConfig)</summary>

En `SecurityConfig.java`, dentro de `filterChain`:

```java
.authorizeHttpRequests(auth -> auth
    // ... otras reglas ...
    .requestMatchers("/api/users/prueba/publico").permitAll() // <--- AGREGA ESTO
    // ... otras reglas ...
)
```
</details>

<details>
<summary>👀 Ver Pista Ejercicio 2.1 (@PreAuthorize)</summary>

En `UserController.java`:

```java
@GetMapping("/prueba/zona-admin")
@PreAuthorize("hasRole('ADMIN')") // <--- ESTA ES LA CLAVE
public ResponseEntity<String> zonaAdmin() {
    return ResponseEntity.ok("Bienvenido al club VIP, Admin.");
}
```
</details>

<details>
<summary>👀 Ver Pista Ejercicio 2.2 (hasAnyRole)</summary>

En `UserController.java`:

```java
@GetMapping("/prueba/staff-interno")
@PreAuthorize("hasAnyRole('ADMIN', 'PROGRAMMER')") // <--- ROLES MÚLTIPLES
public ResponseEntity<String> staffInterno() {
    return ResponseEntity.ok("Acceso concedido al Staff Interno");
}
```
</details>

<details>
<summary>👀 Ver Pista Ejercicio 3.1 (Validar Dueño)</summary>

En `UserController.java`:

```java
@GetMapping("/prueba/{uid}/mi-secreto")
public ResponseEntity<String> miSecreto(
        @PathVariable String uid, 
        @AuthenticationPrincipal String currentUserId // <--- ID DEL TOKEN
) {
    if (!currentUserId.equals(uid)) {
        return ResponseEntity.status(403).body("¡No puedes ver los secretos de otro usuario!");
    }
    return ResponseEntity.ok("Este es tu secreto UID: " + uid);
}
```
</details>

<details>
<summary>👀 Ver Pista Ejercicio 3.2 (Lógica Híbrida)</summary>

En `UserController.java`:

```java
@GetMapping("/prueba/nivel-acceso")
public ResponseEntity<String> nivelAcceso(HttpServletRequest request) {
    if (request.isUserInRole("ADMIN")) { // <--- VERIFICA ROL MANUALMENTE
        return ResponseEntity.ok("Bienvenido Jefe Supremo.");
    }
    return ResponseEntity.ok("Hola usuario normal.");
}
```
</details>

---

# 🚀 Endpoints Importantes del Proyecto (Para Postman)

## 🔐 Autenticación (`AuthController`)
*   **Registrar Usuario:** `POST /api/auth/register`
    *   Body: `{ "email": "...", "password": "...", "fullName": "..." }`
*   **Iniciar Sesión:** `POST /api/auth/login`
    *   Body: `{ "email": "...", "password": "..." }`
    *   **Respuesta:** Te dará el `token` que necesitas para los demás endpoints.
*   **Ver Mi Usuario:** `GET /api/auth/me`
    *   Header: `Authorization: Bearer <TOKEN>`

## 👥 Usuarios (`UserController`)
*   **Listar Programadores:** `GET /api/users/programmers`
*   **Ver Perfil de Usuario:** `GET /api/users/{uid}`
*   **Actualizar Perfil (PATCH):** `PATCH /api/users/{uid}`
    *   Body: `{ "bio": "...", "skills": ["Java", "Spring"] }`
*   **Subir Foto Perfil:** `POST /api/files/upload/profile` (Form-data: `file`)

## 📁 Portafolios (`PortfolioController`)
*   **Ver Portafolios Públicos:** `GET /api/portfolios/public`
*   **Ver Mi Portafolio:** `GET /api/portfolios/me`
*   **Crear Portafolio:** `POST /api/portfolios`

## 🛠️ Proyectos (`ProjectController`)
*   **Listar Proyectos:** `GET /api/projects`
*   **Mis Proyectos:** `GET /api/projects/my-projects`
*   **Crear Proyecto:** `POST /api/projects`

## 📅 Asesorías (`AdvisoryController`)
*   **Solicitar Asesoría:** `POST /api/advisories`
*   **Mis Asesorías:** `GET /api/advisories/my-advisories`
