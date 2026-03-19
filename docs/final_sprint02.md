# Sprint 02 – Execution & Review

## 1. Resultados obtenidos

El objetivo de este sprint era implementar la lógica principal de la aplicación: arquitectura **MVVM**, operaciones **CRUD** para viajes e itinerarios, **validación de datos**, **logs**, **SharedPreferences**, **multilenguaje** y **tests unitarios**. Todos estos objetivos se han cumplido satisfactoriamente.

La aplicación ha pasado de ser un conjunto de pantallas estáticas con datos de muestra a una app funcional donde el usuario puede crear y eliminar viajes, gestionar actividades del itinerario (crear, editar y eliminar), hacer login, cambiar el idioma y el tema, y todo ello con persistencia de preferencias mediante SharedPreferences.

Se ha implementado una arquitectura MVVM completa con **Hilt** para la inyección de dependencias, separando correctamente la lógica de negocio (ViewModels) de la interfaz (Compose) y del acceso a datos (Repositories + FakeDB). Además, se han escrito **80 tests unitarios** que validan las operaciones CRUD, la validación de datos y la integridad de los datos de muestra.

---

## 2. Tareas completadas

| ID   | Tarea                                                          | Completada | Comentarios |
|------|----------------------------------------------------------------|------------|-------------|
| T1.0 | Crear las pantallas principales de gestión de viajes           | Sí         | HomeScreen con selector de viajes, NewTripScreen, pantallas de itinerario |
| T1.1 | Implementar pantalla para mostrar la lista de viajes           | Sí         | Ya estaba muy avanzada del Sprint 01; se ajustó para ser coherente con múltiples viajes |
| T1.2 | Implementar pantalla de detalle de un viaje                    | Sí         | Los datos se muestran de forma coherente entre todas las pantallas gracias al ViewModel |
| T1.3 | Implementar funcionalidad para eliminar viajes                 | Sí         | Desde HomeScreen con confirmación |
| T1.4 | Crear pantalla para editar itinerarios                         | Sí         | AddActivityScreen y EditActivityScreen implementadas |
| T1.5 | Revisar y ajustar el modelo de datos del itinerario            | Sí         | ItineraryActivity con date, cost, tag (ActivityType) |
| T1.6 | Evaluar e implementar pantalla de edición de viaje             | No         | Se evaluó y se descartó (ver Desviaciones) |
| T1.7 | Implementar soporte multilenguaje en la aplicación             | Sí         | Inglés, castellano y catalán (strings.xml) |
| T1.8 | Implementar almacenamiento de preferencias con SharedPreferences | Sí       | Tema, idioma, estado de login y notificaciones |
| T1.9 | Implementar validaciones de datos en formularios               | Sí         | En la UI (botones deshabilitados) y en la capa de Utils |
| T2.0 | Integrar dependencias necesarias para la arquitectura MVVM     | Sí         | Hilt 2.59.2, KSP, Navigation Compose |
| T2.1 | Crear ViewModel para Trip                                      | Sí         | TripViewModel con create, delete, select, refresh |
| T2.2 | Crear ViewModel para Itinerary                                 | Sí         | ItineraryViewModel con add, update, remove, get |
| T2.3 | Crear interfaces Repository para Trip e Itinerary              | Sí         | TripRepository (interfaz en domain/) |
| T2.4 | Implementar RepositoryImplementation para Trip e Itinerary     | Sí         | TripRepositoryImpl con inyección de FakeTripDataSource |
| T2.5 | Crear Fake Databases para pruebas y desarrollo                 | Sí         | FakeTripDataSource con 3 viajes de muestra y 32 actividades |
| T2.6 | Implementar sistema de logs y gestión de errores               | Sí         | Log.d/i/w/e en todas las capas (ViewModel, Repository, DataSource, Utils) |
| T2.7 | Mostrar errores relevantes al usuario en la interfaz           | Sí         | Botones deshabilitados + mensajes de error en formularios |
| T3.0 | Implementar tests unitarios para ViewModels                    | Sí         | Validación delegada a Utils, testeada con 37 tests de validación |
| T3.1 | Implementar tests unitarios para Repositories                  | Sí         | 36 tests CRUD para trips y actividades |
| T4.0 | Evaluar si eliminar la funcionalidad de Packing List           | Sí         | Eliminada completamente (ver Desviaciones) |
| T4.1 | Evaluar si eliminar el indicador de clima (Weather Indicator)  | Sí         | Eliminado completamente (ver Desviaciones) |

**Tareas adicionales realizadas (no planificadas):**

| ID    | Tarea                                                    | Comentarios |
|-------|----------------------------------------------------------|-------------|
| T-extra.1 | Implementar pantalla de login                       | Login con usuario de prueba (hardcoded) |
| T-extra.2 | Crear AuthViewModel y PreferencesViewModel          | Para gestionar login y preferencias con MVVM |
| T-extra.3 | Crear UserPreferencesRepository + implementación    | Abstracción sobre SharedPreferences |
| T-extra.4 | Corregir inconsistencias de paquetes del Sprint 01  | Renombrar `ui.screen` → `ui.screens`, `model` → `domain`, etc. |

---

## 3. Desviaciones

### Funcionalidades eliminadas

- **Packing List (T4.0):** Se evaluó y se decidió eliminar completamente la funcionalidad de Packing List junto con su pantalla. Implementarla habría requerido un modelo de datos adicional (PackingCategory, PackingItem), su propio ViewModel y Repository, operaciones CRUD completas, y su propia pantalla con formularios. El coste de implementación era demasiado alto para el valor que aportaba al proyecto, y habría desviado tiempo de las funcionalidades principales (viajes e itinerarios).

- **Weather Indicator (T4.1):** Se eliminó el indicador de clima que estaba planificado para cada día de la pantalla de itinerario. La implementación habría requerido o bien una API externa de meteorología (complicando la arquitectura con llamadas de red, permisos, etc.) o bien que el usuario indicase la ciudad de cada día manualmente, lo cual era poco práctico y añadía complejidad innecesaria.

### Edición de viaje descartada

- **T1.6 (Edit Trip):** Se evaluó la viabilidad de implementar una pantalla de edición de viajes y se decidió que no valía la pena. Las razones principales son:
  1. Al crear un viaje, el usuario selecciona un hotel que queda reservado para las fechas del viaje, por lo que modificar las fechas implicaría lógica adicional de re-reserva.
  2. Cambiar las fechas podría dejar actividades del itinerario fuera del rango del viaje, generando inconsistencias de datos.
  3. Para el usuario resulta más intuitivo simplemente eliminar el viaje y crear uno nuevo con los datos correctos.

### Trabajo adicional no planificado

- Se implementó el sistema de login con un usuario de prueba (hardcoded), incluyendo `AuthViewModel`, `UserPreferencesRepository` y la pantalla de login. Aunque no estaba en el Sprint Backlog como tarea individual, era necesario para completar la gestión de preferencias y el flujo de usuario.
- Se corrigieron las inconsistencias de nombres de paquetes heredadas del Sprint 01 (`ui.screen` → `ui.screens`, `model` → `domain`, etc.), aprovechando la reestructuración del código para implementar MVVM.

---

## 4. Retrospectiva

### Qué funcionó bien

- **La arquitectura MVVM con Hilt** resultó ser una buena decisión. Una vez configurada la inyección de dependencias, añadir nuevos ViewModels y Repositories fue directo y ordenado. La separación de capas facilitó enormemente la escritura de tests unitarios.
- **El patrón UI → ViewModel → Repository → DataSource** mantuvo el código limpio y coherente. Las pantallas no acceden directamente a los datos, sino que todo pasa por el ViewModel correspondiente.
- **Delegar la validación a clases Utils** (`TripUtils`, `ItineraryUtils`) permitió tener lógica de validación reutilizable y fácilmente testeable, sin depender de la interfaz de Android para ejecutar los tests.
- **Los tests unitarios** (80 en total) se pudieron ejecutar como tests JVM puros sin necesidad de un emulador, lo que hizo el ciclo de desarrollo mucho más rápido.
- **El soporte multilenguaje** (EN, ES, CA) se implementó sin complicaciones gracias al sistema de `strings.xml` de Android.
- **Decidir pronto qué eliminar** (Packing List, Weather, Edit Trip) evitó perder tiempo en funcionalidades que no iban a aportar valor suficiente al proyecto.

### Qué no funcionó

- **Integrar el User con MVVM y SharedPreferences** fue complicado al principio. No estaba claro cómo combinar un usuario hardcoded con persistencia de sesión (¿guardar en SharedPreferences? ¿en el ViewModel? ¿en ambos?). Al final se resolvió creando `SharedPreferencesManager` como capa de persistencia y `UserPreferencesRepository` como abstracción, pero costó más tiempo del esperado.
- **Las inconsistencias de paquetes del Sprint 01** obligaron a hacer una reestructuración antes de poder implementar MVVM correctamente. Esto se podría haber evitado si se hubiera definido mejor la estructura desde el principio.
- **La estimación del tiempo** para algunas tareas fue optimista, especialmente para la integración de Hilt y la configuración inicial de MVVM que requirieron investigación y pruebas.

### Qué mejoraremos en el próximo sprint

- Definir la estructura de paquetes y la arquitectura antes de empezar a programar, para no tener que reestructurar a posteriori.
- Planificar mejor el tiempo de las tareas que implican aprendizaje de nuevas tecnologías (como fue el caso de Hilt en este sprint).
- Añadir más tests de integración que prueben flujos completos (crear viaje → añadir actividad → editar → eliminar) además de los tests unitarios individuales.

---

## 5. Autoevaluación del equipo (0-10)

**Nota: 9**

**Justificación:** Se han completado todas las tareas planificadas salvo la edición de viaje (T1.6), que se descartó de forma justificada y consensuada. Se ha implementado una arquitectura MVVM limpia y completa, con 80 tests unitarios que pasan correctamente, logs en todas las capas, validación de datos robusta, soporte multilenguaje y persistencia de preferencias. Además, se realizaron tareas adicionales no planificadas (login, AuthViewModel, corrección de paquetes) que mejoraron la calidad general de la aplicación. Se resta un punto por la complicación con la integración de User + SharedPreferences, que podría haberse planificado mejor.

---

## Anexo – Detalle técnico de la implementación

### Arquitectura MVVM

La implementación sigue el flujo:

```
UI (Screens) → ViewModel → TripRepository (interface) → TripRepositoryImpl → FakeTripDataSource
```

- **ViewModels creados:** `TripViewModel`, `ItineraryViewModel`, `AuthViewModel`, `PreferencesViewModel`
- **Repositories:** `TripRepository` (interfaz) + `TripRepositoryImpl`, `UserPreferencesRepository` (interfaz) + `UserPreferencesRepositoryImpl`
- **Inyección de dependencias:** `AppModule` con Hilt (`@Binds` para vincular interfaces con implementaciones)

### Operaciones CRUD

| Entidad   | Create | Read | Update | Delete | Motivo si falta |
|-----------|--------|------|--------|--------|-----------------|
| Trip      | Sí     | Sí   | No     | Sí     | Editar viaje descartado (ver Desviaciones) |
| Activity  | Sí     | Sí   | Sí     | Sí     | CRUD completo |

### Validación de datos

La validación se implementa en dos niveles:
1. **Capa UI:** Los botones de crear/actualizar se deshabilitan si no hay todos los campos rellenados correctamente, evitando que el usuario envíe datos inválidos.
2. **Capa Utils:** Funciones de validación reutilizables en `TripUtils` y `ItineraryUtils`, usadas también por los ViewModels como segunda barrera de seguridad.

Validaciones implementadas:
- Nombre del viaje no puede estar vacío
- La fecha de inicio debe ser anterior o igual a la fecha de fin
- El número de personas debe ser al menos 1
- El título y la hora de una actividad no pueden estar vacíos
- El coste de una actividad no puede ser negativo
- El día de una actividad debe estar dentro del rango del viaje

### Logging

Se implementaron logs con `android.util.Log` en todas las capas de la arquitectura, visibles en Logcat:

| Nivel   | Uso                                                  | Ejemplo |
|---------|------------------------------------------------------|---------|
| `Log.d` | Estado interno y flujo normal                        | `"Trips refreshed: 3 trips loaded"` |
| `Log.i` | Operaciones completadas exitosamente                 | `"Trip created: id=4, name='Beach Trip'"` |
| `Log.w` | Validaciones fallidas o entidades no encontradas     | `"createTrip aborted: validation failed"` |
| `Log.e` | Fallos inesperados o errores graves                  | `"addActivity: trip id=99 not found"` |

### Tests unitarios

Se escribieron **80 tests unitarios** distribuidos en 5 clases de test:

| Clase de test           | Tests | Qué valida |
|-------------------------|-------|------------|
| `TripUtilsTest`         | 22    | Validación de nombre, fechas, personas |
| `ItineraryUtilsTest`    | 21    | Validación de título, hora, coste, día dentro del rango |
| `TripRepositoryTest`    | 16    | CRUD de viajes, propiedades computed (daysCount, totalCost) |
| `ItineraryRepositoryTest` | 20  | CRUD de actividades, ciclo de vida completo, datos de muestra |
| `ExampleUnitTest`       | 1     | Test de ejemplo por defecto de Android |

Los tests usan **JUnit 4** con `@Before` para inicialización, `@Test` para cada caso, y assertions (`assertEquals`, `assertTrue`, `assertFalse`, `assertNull`, `assertNotNull`). Se configuró `unitTests.isReturnDefaultValues = true` en Gradle para que las llamadas a `android.util.Log` no lanzasen excepciones en el entorno de tests JVM.
