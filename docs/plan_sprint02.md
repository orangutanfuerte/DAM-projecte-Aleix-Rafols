# Sprint 02 – Planning Document

## 1. Sprint Goal

En este sprint el equipo se centrará en implementar la lógica principal de la aplicación **Travel Planner**. El objetivo es desarrollar las funcionalidades que permiten gestionar los viajes y sus itinerarios, incluyendo operaciones **CRUD (crear, leer, actualizar y eliminar)** para los distintos elementos del sistema.

Durante el sprint se implementarán las pantallas necesarias para visualizar los viajes, consultar sus datos y modificar la información asociada. También se trabajará en la estructura de arquitectura de la aplicación mediante **MVVM**, creando los **ViewModels**, **Repositories** y simulaciones de base de datos necesarias para separar correctamente la lógica de negocio de la interfaz.

Además, se incorporarán mecanismos de **validación de datos**, **gestión de errores**, **logs** y **configuraciones persistentes** mediante SharedPreferences, así como soporte básico para **multilenguaje**.

Finalmente, el equipo realizará **tests unitarios** para comprobar el correcto funcionamiento de las funcionalidades implementadas y documentará el progreso realizado, integrando mejoras y feedback obtenido durante el **Sprint I**.

---

# 2. Sprint Backlog

| ID | Tarea | Responsable | Estimación (h) | Prioridad |
|-----|------|------|------|------|
| T1.0 | Crear las pantallas principales de gestión de viajes (estructura CRUD) | Aleix Ràfols | 3 | Alta |
| T1.1 | Implementar pantalla para mostrar la lista de viajes | Aleix Ràfols | 2 | Alta |
| T1.2 | Implementar pantalla de detalle de un viaje | Aleix Ràfols | 2 | Alta |
| T1.3 | Implementar funcionalidad para eliminar viajes | Aleix Ràfols | 1 | Alta |
| T1.4 | Crear pantalla para editar itinerarios | Aleix Ràfols | 2 | Alta |
| T1.5 | Revisar y ajustar el modelo de datos del itinerario | Aleix Ràfols | 1 | Alta |
| T1.6 | Evaluar e implementar pantalla de edición de viaje | Aleix Ràfols | 1.5 | Media |
| T1.7 | Implementar soporte multilenguaje en la aplicación | Aleix Ràfols | 2 | Media |
| T1.8 | Implementar almacenamiento de preferencias con SharedPreferences | Aleix Ràfols | 1.5 | Media |
| T1.9 | Implementar validaciones de datos en formularios | Aleix Ràfols | 1.5 | Alta |
| T2.0 | Integrar dependencias necesarias para la arquitectura MVVM | Aleix Ràfols | 1 | Alta |
| T2.1 | Crear ViewModel para Trip | Aleix Ràfols | 2 | Alta |
| T2.2 | Crear ViewModel para Itinerary | Aleix Ràfols | 2 | Alta |
| T2.3 | Crear interfaces Repository para Trip e Itinerary | Aleix Ràfols | 1.5 | Alta |
| T2.4 | Implementar RepositoryImplementation para Trip e Itinerary | Aleix Ràfols | 2 | Alta |
| T2.5 | Crear Fake Databases para pruebas y desarrollo | Aleix Ràfols | 2 | Media |
| T2.6 | Implementar sistema de logs y gestión de errores | Aleix Ràfols | 1.5 | Media |
| T2.7 | Mostrar errores relevantes al usuario en la interfaz | Aleix Ràfols | 1 | Media |
| T3.0 | Implementar tests unitarios para ViewModels | Aleix Ràfols | 2 | Media |
| T3.1 | Implementar tests unitarios para Repositories | Aleix Ràfols | 2 | Media |
| T4.0 | Evaluar si eliminar la funcionalidad de Packing List | Aleix Ràfols | 0.5 | Baja |
| T4.1 | Evaluar si eliminar el indicador de clima (Weather Indicator) | Aleix Ràfols | 0.5 | Baja |

---

# 3. Definition of Done (DoD)

Una tarea se considerará completada cuando cumpla las siguientes condiciones:

- [ ] La funcionalidad está implementada y funciona correctamente en la aplicación.
- [ ] El código compila sin errores ni advertencias críticas.
- [ ] La funcionalidad sigue la arquitectura MVVM definida para el proyecto.
- [ ] El código está organizado en las carpetas correctas del proyecto.
- [ ] Se han añadido validaciones básicas de datos cuando sea necesario.
- [ ] Los posibles errores se gestionan adecuadamente y, cuando corresponde, se muestran al usuario.
- [ ] La funcionalidad ha sido probada manualmente.
- [ ] Se han creado tests unitarios cuando aplica.
- [ ] El código es legible y sigue un estilo coherente con el resto del proyecto.
- [ ] Hacer una release final con el estado del proyecto completo para la part del sprint

---

# 4. Riesgos identificados

- Falta de claridad o comprensión de algunos requisitos funcionales de la aplicación.
- Diseñar una **Home Screen** demasiado compleja o demasiado simple, lo que podría afectar a la usabilidad.
- Falta de experiencia con **Kotlin** y **Jetpack Compose**.
- Dudas sobre la organización de archivos dentro de la estructura del proyecto.
- Mala planificación o estimación del tiempo necesario para completar las tareas.
- Problemas al implementar correctamente la arquitectura **MVVM**.
- Dificultades al implementar tests unitarios si la arquitectura no está bien separada.

---

⚠ Este documento no puede modificarse después del 30% del sprint.  
Fecha límite modificación: **13/03/2026**