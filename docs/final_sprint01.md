# Sprint 01 – Execution & Review

## 1. Resultados obtenidos

El objetivo del sprint era configurar el repositorio de GitHub y el proyecto Android, implementar las bases de la aplicación (HomeScreen, navegación básica y pantallas secundarias), y crear el diagrama de clases y el modelo de dominio. Todo ello se ha completado satisfactoriamente.

---

## 2. Tareas completadas

| ID   | Tarea                                              | Completada | Comentarios |
|------|----------------------------------------------------|------------|-------------|
| T1.1 | Decidir nombre y logo                              | Sí         | Sin problemas |
| T1.2 | Decidir versión de Android                         | Sí         | API 26+ |
| T1.3 | Inicializar proyecto Android                       | Sí         | Sin problemas |
| T2.1 | Preparar estructura de ficheros GitHub             | Sí         | Sin problemas |
| T2.2 | Escribir CONTRIBUTING.md y README.md               | Sí         | Sin problemas |
| T2.3 | Elegir licencia                                    | Sí         | MIT |
| T2.4 | Configurar .gitignore                              | Sí         | Sin problemas |
| T2.5 | Repositorio público                                | Sí         | Sin problemas |
| T2.6 | Crear branches                                     | Sí         | Estrategia feature/fix definida |
| T2.7 | Primera release (v0.1.0)                           | Sí         | Publicada en GitHub |
| T3.1 | Decidir funciones de la app y ver ejemplos         | Sí         | Sin problemas |
| T3.2 | Investigar/repasar MVC                             | No         | Se traslada al sprint 02 |
| T3.3 | Diagrama de clases (design.md)                     | Sí         | Incluye modelo de dominio |
| T3.4 | Crear clases en Android Studio (sin funciones)     | Sí         | Sin problemas |
| T3.5 | Splash Screen e icono                              | Sí         | Sin problemas |
| T3.6 | Pantallas de preferencias, about y terms           | Sí         | Sin problemas |
| T3.7 | Decidir qué necesita HomeScreen                    | Sí         | Sin problemas |
| T3.8 | Programar HomeScreen                               | Sí         | Con datos de muestra |
| T3.9 | Navegación                                         | Sí         | BottomNavBar + stack screens |
| T4.0 | Sprint Log Final                                   | Sí         | Este documento |

---

## 3. Desviaciones

- **T3.2** (Investigar MVC) quedó pendiente por falta de tiempo y porque no era bloqueante para las demás tareas. Se incorpora al sprint 02.
- Se programaron estructuras base de pantallas adicionales (Itinerary, Packing, Photos, Places) sin funcionalidad real, ya que surgieron de forma natural al implementar la navegación. Son bases vacías que se desarrollarán en sprints posteriores.

---

## 4. Retrospectiva

### Qué funcionó bien
- La configuración del repositorio y el proyecto fue rápida y sin incidencias.
- La navegación con Jetpack Compose resultó más sencilla de lo esperado una vez definida la estructura.
- Separar rutas en `Destinations.kt` y pantallas en ficheros individuales facilitó el trabajo.

### Qué no funcionó
- La estructura de paquetes quedó con inconsistencias (p. ej. `ui.screen` en singular, `model` en lugar de `domain`) que habrá que mantener o corregir en el futuro.
- No se planificó bien el tiempo para la tarea de MVC, dejándola para el último momento.

### Qué mejoraremos en el próximo sprint
- Diseñar mejor la estructura de código y paquetes antes de empezar a programar.
- Estimar con más margen las tareas de investigación/aprendizaje.

---

## 5. Autoevaluación del equipo (0-10)

**Nota: 8**

**Justificación:** Se han completado prácticamente todas las tareas planificadas y el resultado es una app funcional con navegación, splash screen, HomeScreen con datos de muestra y pantallas secundarias. La única tarea pendiente (MVC) no era bloqueante. Se baja un punto por las inconsistencias en los paquetes y otro por la falta de planificación en la tarea de investigación.
