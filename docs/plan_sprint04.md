# Sprint 04 – Planning Document

## 1. Sprint Goal

En este sprint el equipo se centrará en dos grandes áreas: **persistencia remota mediante Retrofit** y **gestión de galería de imágenes por viaje**.

El primer objetivo es integrar **Retrofit** para conectar la aplicación con la REST API del profesor (http://15.224.84.148:8090), permitiendo buscar hoteles disponibles en Londres, París o Barcelona por fechas, mostrar los resultados con sus imágenes, reservar habitaciones y guardar la información de la reserva localmente en la base de datos Room como un nuevo viaje.

El segundo objetivo es implementar una **galería de imágenes por viaje**: el usuario podrá adjuntar múltiples imágenes a cada viaje, almacenarlas en el dispositivo y visualizarlas en la pantalla de detalle del viaje.

Además, se creará una pantalla para **listar y cancelar reservas** existentes, mostrando las imágenes asociadas al hotel y habitación, y se actualizará la pantalla "Mis Viajes" para indicar si un viaje tiene una reserva de hotel vinculada. Finalmente, se escribirán **tests unitarios** que simulen la conexión remota mockeando Retrofit.

---

## 2. Sprint Backlog

| ID | Tarea | Responsable | Estimación (h) | Prioridad |
|----|-------|-------------|----------------|-----------|
| T1.0 | Configurar Retrofit e integración con la API | Aleix Ràfols | **6** | Alta |
| T1.1 | Añadir la dependencia de Retrofit y configurar el cliente HTTP | Aleix Ràfols | 1 | Alta |
| T1.2 | Crear los modelos de datos y las interfaces de la API para hoteles siguiendo MVVM | Aleix Ràfols | 2 | Alta |
| T1.3 | Crear la capa de repositorio para abstraer el uso de la API | Aleix Ràfols | 2 | Alta |
| T1.4 | Escribir tests unitarios mockeando la conexión remota | Aleix Ràfols | 1 | Media |
| T2.0 | Pantallas de búsqueda y reserva de hoteles | Aleix Ràfols | **9** | Alta |
| T2.1 | Crear pantalla de búsqueda de hoteles (ciudad, fecha inicio y fin) para Londres, París o Barcelona | Aleix Ràfols | 2 | Alta |
| T2.2 | Mostrar la lista de hoteles y habitaciones devueltos por la API (3 habitaciones por hotel) | Aleix Ràfols | 2.5 | Alta |
| T2.3 | Permitir al usuario reservar una habitación y guardar la info de reserva localmente como nuevo viaje | Aleix Ràfols | 3 | Alta |
| T2.4 | Mostrar todas las imágenes del hotel y las habitaciones en las pantallas de búsqueda y reserva | Aleix Ràfols | 1.5 | Alta |
| T3.0 | Galería de imágenes por viaje | Aleix Ràfols | **5** | Alta |
| T3.1 | Permitir al usuario adjuntar múltiples imágenes a un viaje | Aleix Ràfols | 2 | Alta |
| T3.2 | Guardar las imágenes localmente en la base de datos o en el almacenamiento del dispositivo | Aleix Ràfols | 1.5 | Alta |
| T3.3 | Mostrar la galería específica de cada viaje en la pantalla de detalle del viaje | Aleix Ràfols | 1.5 | Alta |
| T4.0 | Listado y cancelación de reservas | Aleix Ràfols | **6** | Alta |
| T4.1 | Crear pantalla para listar todas las reservas locales indicando el viaje relacionado | Aleix Ràfols | 2 | Alta |
| T4.2 | Añadir funcionalidad para eliminar una reserva localmente y vía API (si se requiere) | Aleix Ràfols | 1.5 | Alta |
| T4.3 | Mostrar imágenes del hotel y habitación en el listado de reservas | Aleix Ràfols | 1.5 | Media |
| T4.4 | Actualizar la pantalla "Mis Viajes" para indicar si un viaje tiene reserva de hotel vinculada | Aleix Ràfols | 1 | Media |

---

## 3. Definition of Done (DoD)

Una tarea se considerará completada cuando cumpla las siguientes condiciones:

- [ ] La funcionalidad está implementada y funciona correctamente en la aplicación.
- [ ] El código compila sin errores ni advertencias críticas.
- [ ] La funcionalidad sigue la arquitectura MVVM definida para el proyecto (View → ViewModel → Repository → DB/API).
- [ ] El código está organizado en las carpetas correctas del proyecto (`view`, `viewmodel`, `repo`, `di`, `data`).
- [ ] La persistencia local usa **Room** y la comunicación remota usa **Retrofit**.
- [ ] La inyección de dependencias está implementada con **Hilt**.
- [ ] Los campos de fecha usan **date pickers** (no campos de texto libre).
- [ ] Las imágenes del hotel y las habitaciones se muestran correctamente en las pantallas correspondientes.
- [ ] Los posibles errores de red se gestionan adecuadamente y se muestran al usuario.
- [ ] Las operaciones relevantes se registran en Logcat con el nivel adecuado (`Log.d/i/w/e`).
- [ ] La funcionalidad ha sido probada manualmente.
- [ ] Se han creado tests unitarios para la capa de repositorio mockeando Retrofit cuando aplica.
- [ ] El código es legible y sigue un estilo coherente con el resto del proyecto.
- [ ] Se ha hecho una release `v4.x.x` con el estado completo del proyecto al finalizar el sprint.
- [ ] Se ha grabado un vídeo demostrando todas las funcionalidades implementadas y guardado en `docs/evidence/v4.x.x`.

---

## 4. Riesgos identificados

- Falta de experiencia con **Retrofit** y el consumo de APIs REST en Android.
- La **configuración de la API** (endpoints, autenticación, formato de respuesta) puede diferir de lo esperado y requerir ajustes en los modelos de datos.
- La **carga y visualización de imágenes remotas** (URLs de la API) puede añadir complejidad; será necesario usar una librería como Coil o Glide.
- Gestionar los **estados de carga y error** (loading, error, success) en las corrutinas de Retrofit sin bloquear la UI puede resultar complejo.
- Guardar las **imágenes localmente** implica gestionar permisos de almacenamiento en Android, lo que puede variar según la versión de API.
- La integración de **Room + Retrofit** en la misma capa de repositorio requiere una gestión cuidadosa de las fuentes de datos (local vs. remota).
- Los **tests unitarios con mocks de Retrofit** requieren conocer herramientas como MockWebServer o Mockito, que pueden ser nuevas para el equipo.
- Mala estimación del tiempo para tareas que implican aprendizaje de nuevas tecnologías (Retrofit, gestión de imágenes).
- Posibles **cambios en la disponibilidad o el comportamiento de la API** del profesor durante el sprint.

---

⚠ Este documento no puede modificarse después del 30% del sprint.  
Fecha límite modificación: **11/05/2026**
