# API propia – Grupo 1 (PA3 Android) · pregunta 2.7

API REST hecha en **Node.js + Express**. Tiene login con token **JWT** y el **CRUD de productos**.
Los datos se guardan en `db.json`. Si no existe, se crea copiando `db.seed.json`.

## Cómo levantarla

```bash
cd api
npm install      # solo la primera vez
npm start
```

La consola muestra la URL que debe usar el celular, por ejemplo `http://192.168.1.45:3000/`.

- **Celular real:** tiene que estar en la **misma Wi-Fi** que la PC. En
  `app/.../data/datasource/remote/RetrofitClient.kt` cambia `BASE_URL` por la IP que muestra la consola.
- **Emulador:** también puedes usar `http://10.0.2.2:3000/`.
- Si macOS pregunta si permite conexiones entrantes a `node`, elige **Permitir**.

Usuario de prueba (sale de `db.seed.json`): **admin** / **admin123**

## Endpoints

| Método | Ruta | Token | Qué hace |
|---|---|---|---|
| POST | `/api/auth/login` | no | Body `{ "usuario", "clave" }` → `{ token, usuario }` |
| GET | `/api/productos` | sí | Lista los productos |
| GET | `/api/productos/{codigo}` | sí | Obtiene un producto |
| POST | `/api/productos` | sí | Crea `{ nombre, categoria, precio, stock }` |
| PUT | `/api/productos/{codigo}` | sí | Actualiza un producto |
| DELETE | `/api/productos/{codigo}` | sí | Elimina un producto (responde 204) |

El token va en la cabecera `Authorization: Bearer <token>`. En la app lo agrega solo el `AuthInterceptor`.

## Para la 2.5 (pantalla del CRUD)

Ya están hechas las capas (2.6). La pantalla solo tiene que usar `ProductoViewModel`:

```kotlin
val vm: ProductoViewModel = viewModel(factory = AppViewModelProvider.Factory)
val state by vm.uiState.collectAsState()   // state.productos, state.isLoading, state.error, state.seleccionado

// Toast "Información guardada correctamente" (y otros mensajes)
val context = LocalContext.current
LaunchedEffect(Unit) {
    vm.eventos.collect { e ->
        when (e) {
            is ProductoEvento.Mensaje -> Toast.makeText(context, e.texto, Toast.LENGTH_SHORT).show()
            ProductoEvento.Guardado -> { /* cerrar el formulario / volver a la lista */ }
        }
    }
}

vm.cargar()                                   // consulta (lista)
vm.seleccionar(codigo)                        // consulta (detalle) -> state.seleccionado
vm.guardar(Producto(nombre = "...", categoria = "...", precio = 10.0, stock = 5))   // inserción (codigo = 0)
vm.guardar(productoEditado)                   // actualización (codigo > 0)
vm.eliminar(codigo)                           // eliminación
```
