# Sprint 03 – Execution & Review

## 1. Resultados obtenidos

El objetivo de este sprint era implementar **persistencia de datos con Room (SQLite)**, **autenticación real con Firebase**, asociar los viajes al usuario autenticado y escribir tests para los DAOs. Todos los objetivos principales se han cumplido satisfactoriamente.

La aplicación ha pasado de usar un almacenamiento en memoria (FakeDB) y un usuario hardcoded a tener una **base de datos SQLite persistente** con cuatro tablas (viajes, actividades, usuarios y log de accesos), autenticación real con Firebase (login, registro con verificación de email y recuperación de contraseña), y los viajes correctamente asociados a cada usuario. Los datos persisten entre sesiones.

Además de las tareas planificadas, se implementaron pantallas completas de gestión de perfil (ver, completar y editar), se añadió validación de nombre duplicado en el propio formulario de creación de viaje, y se corrigieron varios bugs de flujo de autenticación (error al intentar acceder con email no verificado, excepción de demasiadas peticiones en el reenvío de verificación, y limpieza de campos del formulario de registro al cerrar sesión).

---

## 2. Tareas completadas

| ID   | Tarea                                                                        | Completada | Comentarios |
|------|------------------------------------------------------------------------------|------------|-------------|
| T1.0 | Implementar persistencia SQLite con Room                                     | Sí         | Base de datos `travel_sigma_db` v3 con 4 tablas |
| T1.1 | Crear la clase Room Database                                                 | Sí         | `TravelSigmaDatabase` con 4 entidades |
| T1.2 | Definir las entidades para Trip e ItineraryItem                              | Sí         | `TripEntity`, `ActivityEntity`, `UserEntity`, `AccessLogEntity` |
| T1.3 | Crear los DAOs para las operaciones de base de datos                         | Sí         | `TripDao`, `ItineraryActivityDao`, `UserDao`, `AccessLogDao` |
| T1.4 | Implementar CRUD mediante DAOs                                               | Sí         | CRUD completo para viajes y actividades; insert para usuarios y logs |
| T1.5 | Modificar los ViewModels para que usen Room en lugar de FakeDB               | Sí         | `RoomTripRepositoryImpl` inyectado por Hilt; FakeDB mantenido solo para tests unitarios |
| T1.6 | Asegurar que la UI se actualiza cuando cambian los datos                     | Sí         | `Flow<List<TripWithActivities>>` de Room reactualiza la UI automáticamente |
| T2.0 | Implementar Login y Logout con Firebase                                      | Sí         | `AuthViewModel` gestiona todas las operaciones de autenticación |
| T2.1 | Conectar la aplicación a Firebase                                            | Sí         | `google-services.json`, dependencias Firebase Auth y Firestore |
| T2.2 | Diseñar la pantalla de login (formulario)                                    | Sí         | `LoginScreen` con email, contraseña, botón de recuperación |
| T2.3 | Implementar la autenticación con Firebase (email y contraseña)               | Sí         | Login real con Firebase; errores de autenticación mostrados al usuario |
| T2.4 | Implementar la acción de logout                                              | Sí         | Logout desde `PreferencesScreen`; limpia sesión y redirige al login |
| T2.5 | Usar Logcat para registrar operaciones y errores de autenticación            | Sí         | `Log.d/i/w/e` en `AuthViewModel` para todas las operaciones |
| T3.0 | Implementar Registro y Recuperación de contraseña                            | Sí         | Flujo completo desde registro hasta acceso a la app |
| T3.1 | Diseñar la pantalla de registro (formulario)                                 | Sí         | `RegisterScreen` con nombre, username, fecha de nacimiento, email y contraseña |
| T3.2 | Implementar el registro con Firebase con verificación por email              | Sí         | `EmailVerificationScreen` con polling automático y verificación manual |
| T3.3 | Implementar recuperación de contraseña                                       | Sí         | Envío de email de reset desde `LoginScreen` |
| T4.0 | Persistir información del usuario y asociar viajes al usuario                | Sí         | Tabla `users` y columna `userId` en tabla `trips` |
| T4.1 | Persistir la información del usuario en la base de datos local               | Sí         | `UserEntity` guardada en Room en el momento del registro |
| T4.2 | Modificar la tabla de viajes para soportar múltiples usuarios                | Sí         | `getAllTripsWithActivities(userId)` filtra por usuario activo |
| T4.3 | Actualizar design.md con el esquema de base de datos                         | Sí         | `design.md` y `data-model.md` completamente reescritos |
| T4.4 | Persistir el log de accesos (login/logout con userId y datetime)             | Sí         | `AccessLogEntity` + `AccessLogDao` + `AccessLogRepositoryImpl` |
| T5.0 | Testing y Debugging                                                          | Sí         | 82 tests unitarios JVM + 6 tests instrumentados de DAO |
| T5.1 | Escribir tests unitarios para DAOs                                           | Sí         | `TripDaoTest` con `Room.inMemoryDatabaseBuilder` (6 tests en dispositivo real) |
| T5.2 | Implementar validación de nombres duplicados y fechas válidas                | Sí         | Validación en UI (paso de nombre), ViewModel y repositorio |
| T5.3 | Usar Logcat para registrar operaciones de base de datos y errores            | Sí         | `Log.d/i/w/e` en todas las capas: DAOs (a través de los repos), ViewModels y DataSource |
| T5.4 | Actualizar design.md con el esquema y la estrategia de migración             | Sí         | Consolidado con T4.3 |

**Tareas adicionales realizadas (no planificadas):**

| ID          | Tarea                                                                         | Comentarios |
|-------------|-------------------------------------------------------------------------------|-------------|
| T-extra.1   | Implementar `CompleteProfileScreen`                                           | Pantalla para completar datos opcionales (teléfono, dirección, país) tras el primer registro |
| T-extra.2   | Implementar `ProfileScreen` y `EditProfileScreen`                             | Visualización y edición del perfil de usuario desde Preferencias |
| T-extra.3   | Gestión de errores de autenticación en `EmailVerificationScreen`              | Error visible si el usuario pulsa "verificado" sin haber verificado; error por demasiadas peticiones al reenviar |
| T-extra.4   | Filtrado de espacios en el campo username del registro                        | El campo no acepta espacios en blanco |
| T-extra.5   | Limpieza de campos del formulario de registro al hacer logout                 | Los campos del registro se vacían al cerrar sesión para no mostrar datos de otro usuario |
| T-extra.6   | Mover fecha de nacimiento de opcional a obligatoria                           | `dateOfBirth` se desplazó de `CompleteProfileScreen` a `RegisterScreen` como campo obligatorio |
| T-extra.7   | Validación de nombre duplicado a nivel de UI en el flujo de creación          | El paso de nombre en `NewTripScreen` comprueba el duplicado antes de avanzar y muestra error inline |

---

## 3. Desviaciones

### Trabajo adicional no planificado

La implementación de Firebase Auth con email de verificación requirió un flujo de pantallas más complejo de lo previsto: además de las pantallas de login y registro, fue necesario crear `EmailVerificationScreen`, `CompleteProfileScreen`, `ProfileScreen` y `EditProfileScreen`. Estas pantallas no aparecían en el Sprint Backlog pero eran necesarias para ofrecer una experiencia de usuario completa y coherente.

Asimismo, surgieron varios bugs durante el desarrollo que no estaban previstos en la planificación:
- Acceso directo a la app con email no verificado si el usuario había iniciado sesión previamente.
- Excepción `FirebaseTooManyRequestsException` no capturada al reenviar el email de verificación.
- Campos del formulario de registro que persistían entre sesiones.

Estos bugs se corrigieron durante el sprint, lo que consumió tiempo adicional no estimado.

### Sin desviaciones en el alcance

En este sprint **no se descartó ninguna tarea** del backlog original. Todas las funcionalidades planificadas se implementaron.

---

## 4. Retrospectiva

### Qué funcionó bien

- **La migración de FakeDB a Room fue más limpia de lo esperado.** Gracias al patrón de repositorio establecido en el Sprint 02, solo fue necesario crear una nueva implementación (`RoomTripRepositoryImpl`) y cambiar el binding de Hilt. Los ViewModels y la UI no necesitaron ningún cambio para adaptarse al nuevo origen de datos.
- **El `Flow<List<TripWithActivities>>` de Room** mantuvo la UI reactiva de forma automática. Cada vez que cambian los datos de la base de datos, las pantallas se actualizan sin necesidad de refrescos manuales.
- **La separación de la FakeDB para los tests unitarios** fue una buena decisión. Al mantener `TripRepositoryImpl` (fake) junto a `RoomTripRepositoryImpl` y vincular solo la segunda en producción mediante Hilt, los tests unitarios siguieron siendo JVM puros y rápidos sin necesitar dispositivo.
- **Los tests instrumentados de DAO** (`TripDaoTest`) con `Room.inMemoryDatabaseBuilder` funcionaron a la primera en el dispositivo real, validando que las queries SQL y las relaciones entre tablas son correctas.
- **La estrategia `seedIfEmpty()`** para regenerar los viajes de muestra en el primer login de cada usuario fue una solución práctica que evitó tener que implementar migraciones complejas.

### Qué no funcionó

- **Los flujos asíncronos de Firebase Auth** generaron más casos borde de los esperados: el polling de verificación de email, la excepción `FirebaseTooManyRequestsException`, el acceso con email no verificado desde una sesión previa... Cada uno requirió tratamiento específico y tiempo no previsto.
- **La validación del nombre duplicado** inicialmente era asíncrona pero silent: el ViewModel abortaba la creación sin que la UI lo supiera, y el snackbar de éxito aparecía igualmente. Fue necesario añadir la comprobación directamente en el paso de nombre del formulario para bloquearlo antes de avanzar.
- **La estimación de tiempo** para el flujo de autenticación (T2+T3) fue optimista. No se tuvo en cuenta la complejidad de gestionar los estados intermedios de Firebase (esperando verificación, sesión persistente, reenvío de email) ni los bugs asociados.

### Qué mejoraremos en el próximo sprint

- Anticipar que los flujos de autenticación con servicios externos tienen muchos casos borde y reservar tiempo específico para su tratamiento.
- Cuando hay validaciones asíncronas que pueden bloquear una acción, implementarlas directamente en la UI (blocking en el formulario) desde el principio, no como defensa posterior en el ViewModel.
- Incluir en el Sprint Backlog las pantallas secundarias (como las de perfil) que se deriven de las funcionalidades principales, en lugar de implementarlas como trabajo no planificado.

---

## 5. Autoevaluación del equipo (0-10)

**Nota: 9**

**Justificación:** Se han completado todas las tareas planificadas del backlog, incluyendo la persistencia con Room, la autenticación completa con Firebase, la asociación de datos al usuario, los tests de DAO instrumentados y la actualización de la documentación. Además, se realizaron numerosas tareas adicionales (pantallas de perfil, errores de autenticación, validaciones de UI) que mejoraron la calidad y robustez de la aplicación. Se resta un punto por la estimación optimista del tiempo para el flujo de Firebase Auth, que generó bugs inesperados y trabajo adicional no planificado que presionó el sprint.

---

## Anexo – Detalle técnico de la implementación

### Base de datos Room

La base de datos `travel_sigma_db` está en la **versión 3** y contiene cuatro tablas con sus relaciones:

```
users          trips              itinerary_activities     access_log
─────────      ──────────────     ────────────────────     ──────────
userId (PK)    id (PK, auto)      id (PK, auto)            id (PK, auto)
name           name               tripId (FK → trips.id)   userId
email          destinationId      time                     action
username       hotelId            title                    dateTime
dateOfBirth    startDate          subtitle
phone          endDate            cost
address        persons            tag (nullable)
country        heroColor          date
acceptsEmails  userId (FK)
language
theme
notifEnabled
```

La clave foránea `itinerary_activities.tripId → trips.id` tiene `CASCADE DELETE`, de forma que al borrar un viaje se borran automáticamente sus actividades.

> **Nota sobre las columnas `language`, `theme` y `notificationsEnabled` de `users`:** estas columnas existen en `UserEntity` y se rellenan con los valores por defecto en el momento del registro, pero **no se leen ni actualizan en tiempo de ejecución**. Las preferencias de la aplicación (tema, idioma, notificaciones) se gestionan íntegramente mediante `SharedPreferences` a través de `UserPreferencesRepositoryImpl`. Las columnas de Room están presentes por diseño pero quedan sin uso activo en este sprint; su sincronización con SharedPreferences sería una mejora para un sprint futuro.

### Datos de muestra (seeding)

Al hacer login, `TripRepository.seedIfEmpty(userId)` comprueba si el usuario ya tiene viajes en Room. Si la cuenta es nueva (sin viajes), inserta automáticamente **3 viajes de muestra** con sus actividades:

| Viaje | Fechas | Actividades |
|-------|--------|-------------|
| Iceland Adventure | Jun 2024 | 0 (sin itinerario) |
| Italian Getaway | Sep 2026 | 0 (sin itinerario) |
| Japan Highlights | Mar 2027 | 32 actividades en 7 días |

Esto permite que la aplicación tenga contenido visible desde el primer acceso, facilitando la evaluación y las demos. En un entorno de producción real este comportamiento se eliminaría.

### Autenticación Firebase

| Operación            | Implementación |
|----------------------|----------------|
| Login                | `FirebaseAuth.signInWithEmailAndPassword` |
| Registro             | `FirebaseAuth.createUserWithEmailAndPassword` + `sendEmailVerification` |
| Verificación         | Polling periódico con `currentUser.reload()` + verificación manual |
| Logout               | `FirebaseAuth.signOut` + limpieza de `SharedPreferences` |
| Reset contraseña     | `FirebaseAuth.sendPasswordResetEmail` |
| Persistencia sesión  | `SharedPreferences` guarda el UID del usuario logado |


### Validación de datos 

Se añadió una nueva validación respecto al Sprint 02:

- **Nombre de viaje duplicado:** se comprueba en tres niveles:
  1. **UI (`NameStep`):** cuando el usuario pulsa "Siguiente" en el paso de nombre, se hace una llamada suspend al repositorio y, si el nombre ya existe, se muestra un error inline en el campo de texto y no se avanza al siguiente paso.
  2. **ViewModel (`TripViewModel.createTrip`):** segunda barrera de seguridad dentro del `viewModelScope.launch`.
  3. **Repositorio (`RoomTripRepositoryImpl.isTripNameTaken`):** consulta `SELECT COUNT(*) FROM trips WHERE userId = :userId AND name = :name`.

### Logging

Los logs siguen el mismo esquema del Sprint 02 (`Log.d/i/w/e`), extendido a las nuevas capas:

| Capa | TAG | Ejemplos |
|------|-----|---------|
| `AuthViewModel` | `AuthViewModel` | `"login success: uid=abc123"`, `"login failed: invalid credentials"` |
| `RoomTripRepositoryImpl` | `RoomTripRepository` | `"addTrip: name='Iceland', userId=abc"`, `"isTripNameTaken: 'Iceland' already exists"` |
| `AccessLogRepositoryImpl` | `AccessLogRepository` | `"logAccess: LOGIN for userId=abc123"` |
| `TripViewModel` | `TripViewModel` | `"createTrip aborted: duplicate name"`, `"Trip deleted: id=3"` |

### Tests

| Clase de test               | Tipo          | Tests | Qué valida |
|-----------------------------|---------------|-------|------------|
| `TripUtilsTest`             | JVM (unit)    | 22    | Validación de nombre, fechas, personas |
| `ItineraryUtilsTest`        | JVM (unit)    | 21    | Validación de título, hora, coste, día dentro del rango |
| `TripRepositoryTest`        | JVM (unit)    | 18    | CRUD de viajes, `isTripNameTaken`, propiedades computed |
| `ItineraryRepositoryTest`   | JVM (unit)    | 20    | CRUD de actividades, ciclo de vida completo |
| `ExampleUnitTest`           | JVM (unit)    | 1     | Test de ejemplo por defecto |
| `TripDaoTest`               | Instrumentado | 6     | Queries SQL con Room in-memory en dispositivo real |
| **Total**                   |               | **88** | |

Los tests instrumentados de `TripDaoTest` se ejecutan con `@RunWith(AndroidJUnit4::class)` y `Room.inMemoryDatabaseBuilder`, lo que permite validar las queries SQL con el motor Room real sin persistir datos entre ejecuciones. Se añadió la dependencia `androidx.room:room-testing` para soportar este tipo de tests.
