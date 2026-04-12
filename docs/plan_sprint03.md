# Sprint 03 – Planning Document

## 1. Sprint Goal

En este sprint el equipo se centrará en dos grandes áreas: **persistencia de datos con SQLite mediante Room** y **autenticación real de usuarios mediante Firebase**.

El primer objetivo es reemplazar el almacenamiento en memoria (FakeDB) del Sprint 02 por una base de datos SQLite persistente utilizando la librería **Room**, definiendo las entidades, los DAOs y migrando los ViewModels para que trabajen con la nueva capa de datos. Esto garantizará que los viajes e itinerarios se conserven aunque la aplicación se cierre.

El segundo objetivo es implementar un sistema completo de **autenticación con Firebase**: login con email y contraseña, registro de nuevos usuarios con verificación por email, recuperación de contraseña, y logout. El usuario hardcoded del Sprint 02 quedará sustituido por autenticación real.

Además, se persistirá la información del usuario en la base de datos local, se asociarán los viajes al usuario autenticado, y se registrarán los accesos (login/logout) en una tabla de log. Finalmente, se escribirán **tests unitarios** para los DAOs y se actualizará la documentación con el esquema de base de datos.

---

## 2. Sprint Backlog

| ID | Tarea | Responsable | Estimación (h) | Prioridad |
|----|-------|-------------|----------------|-----------|
| T1.0 | Implementar persistencia SQLite con Room | Aleix Ràfols | **10** | Alta |
| T1.1 | Crear la clase Room Database | Aleix Ràfols | 1 | Alta |
| T1.2 | Definir las entidades (Entities) para Trip e ItineraryItem | Aleix Ràfols | 1.5 | Alta |
| T1.3 | Crear los Data Access Objects (DAOs) para las operaciones de base de datos | Aleix Ràfols | 2 | Alta |
| T1.4 | Implementar las operaciones CRUD mediante los DAOs para viajes e itinerarios | Aleix Ràfols | 2 | Alta |
| T1.5 | Modificar los ViewModels para que usen Room en lugar del almacenamiento en memoria | Aleix Ràfols | 2 | Alta |
| T1.6 | Asegurar que la UI se actualiza correctamente cuando cambian los datos de la base de datos | Aleix Ràfols | 1.5 | Alta |
| T2.0 | Implementar Login y Logout con Firebase | Aleix Ràfols | **5** | Alta |
| T2.1 | Conectar la aplicación a Firebase | Aleix Ràfols | 1 | Alta |
| T2.2 | Diseñar la pantalla de login (formulario) | Aleix Ràfols | 1 | Alta |
| T2.3 | Implementar la autenticación con Firebase (email y contraseña) | Aleix Ràfols | 2 | Alta |
| T2.4 | Implementar la acción de logout en la aplicación | Aleix Ràfols | 0.5 | Alta |
| T2.5 | Usar Logcat para registrar todas las operaciones y errores de autenticación | Aleix Ràfols | 0.5 | Media |
| T3.0 | Implementar Registro y Recuperación de contraseña | Aleix Ràfols | **5** | Alta |
| T3.1 | Diseñar la pantalla de registro (formulario) | Aleix Ràfols | 1 | Alta |
| T3.2 | Implementar el registro con Firebase (email y contraseña) con verificación por email | Aleix Ràfols | 2.5 | Alta |
| T3.3 | Implementar la funcionalidad y la vista de recuperación de contraseña | Aleix Ràfols | 1.5 | Media |
| T4.0 | Persistir información del usuario y asociar viajes al usuario | Aleix Ràfols | **6** | Alta |
| T4.1 | Persistir la información del usuario en la base de datos local (tabla de usuarios) | Aleix Ràfols | 2 | Alta |
| T4.2 | Modificar la tabla de viajes para soportar múltiples usuarios y mostrar solo los del usuario activo | Aleix Ràfols | 1.5 | Alta |
| T4.3 | Actualizar design.md con el esquema de base de datos y su uso | Aleix Ràfols | 1 | Media |
| T4.4 | Persistir el log de accesos (tabla que registra cada login y logout con userId y datetime) | Aleix Ràfols | 1.5 | Media |
| T5.0 | Testing y Debugging | Aleix Ràfols | **5** | Media |
| T5.1 | Escribir tests unitarios para DAOs e interacciones con la base de datos | Aleix Ràfols | 2 | Media |
| T5.2 | Implementar validaciones de datos (evitar nombres de viaje duplicados, fechas válidas) | Aleix Ràfols | 1.5 | Media |
| T5.3 | Usar Logcat para registrar las operaciones de base de datos y errores | Aleix Ràfols | 0.5 | Media |
| T5.4 | Actualizar design.md con el esquema de base de datos y la estrategia de migración | Aleix Ràfols | 1 | Baja |

---

## 3. Definition of Done (DoD)

Una tarea se considerará completada cuando cumpla las siguientes condiciones:

- [ ] La funcionalidad está implementada y funciona correctamente en la aplicación.
- [ ] El código compila sin errores ni advertencias críticas.
- [ ] La funcionalidad sigue la arquitectura MVVM definida para el proyecto.
- [ ] El código está organizado en las carpetas correctas del proyecto.
- [ ] Los datos persisten correctamente entre sesiones (Room) o se autentican correctamente (Firebase).
- [ ] Se han añadido validaciones de datos cuando sea necesario (p. ej. no permitir nombres de viaje duplicados, fechas inválidas).
- [ ] Los posibles errores se gestionan adecuadamente y, cuando corresponde, se muestran al usuario.
- [ ] Las operaciones relevantes se registran en Logcat con el nivel adecuado (`Log.d/i/w/e`).
- [ ] La funcionalidad ha sido probada manualmente.
- [ ] Se han creado tests unitarios para los DAOs y la validación de datos cuando aplica.
- [ ] El código es legible y sigue un estilo coherente con el resto del proyecto.
- [ ] Se ha hecho una release final con el estado completo del proyecto al finalizar el sprint.

---

## 4. Riesgos identificados

- Falta de experiencia con **Room** y con la configuración de base de datos en Android (Entities, DAOs, migraciones).
- Complejidad de **migrar los ViewModels** del Sprint 02 (basados en FakeDB) a Room sin romper la arquitectura MVVM existente.
- Dificultades al configurar **Firebase** en el proyecto (archivo `google-services.json`, dependencias, reglas de autenticación).
- La implementación de **verificación por email** con Firebase puede requerir flujos asíncronos complejos y difíciles de testear.
- La **asociación de viajes a usuarios** implica cambiar el esquema de la base de datos, lo que puede requerir una migración de Room y complicar las pruebas.
- Los **tests unitarios para DAOs** con Room requieren usar `Room.inMemoryDatabaseBuilder` o instrumentación, lo que puede añadir complejidad al entorno de tests.
- Mala estimación del tiempo para las tareas que implican aprendizaje de nuevas tecnologías (Room, Firebase).
- Posibles conflictos entre la lógica de autenticación de Firebase y la persistencia de sesión ya implementada con SharedPreferences en el Sprint 02.

---

⚠ Este documento no puede modificarse después del 30% del sprint.  
Fecha límite modificación: **12/04/2026**
