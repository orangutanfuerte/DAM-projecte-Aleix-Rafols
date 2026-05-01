# Disseny Arquitectònic de Travel like a Sigma

## Arquitectura General

Travel like a Sigma implementa una arquitectura **MVVM (Model-View-ViewModel)** amb **Hilt** com a sistema d'injecció de dependències, seguint el patró de repositori per desacoblar les capes de dades i presentació.

```
UI Screens  (Jetpack Compose)
    ↓  events via lambdas
ViewModels  (TripViewModel · ItineraryViewModel · AuthViewModel · ProfileViewModel)
    ↓  suspend functions / Flow
Repository interfaces  (TripRepository · UserRepository · AccessLogRepository)
    ↓
Implementations  (RoomTripRepositoryImpl · UserRepositoryImpl · AccessLogRepositoryImpl)
    ↓                        ↓
Room DAOs             Firebase Auth / Firestore
(SQLite local)        (autenticació i dades d'usuari remotes)
```

### Responsabilitats per capa

| Capa | Responsabilitat |
|---|---|
| **UI (Screens)** | Renderitzar l'estat, propagar events cap al ViewModel |
| **ViewModel** | Mantenir l'estat de la UI, validar entrades, coordinar repositoris |
| **Repository (interfície)** | Contracte de dades independent de la implementació |
| **Repository (impl)** | Accés a Room o Firebase; mapatge entre entitats i domini |
| **Room DAOs** | Queries SQL via anotacions Room |
| **Firebase** | Autenticació d'usuaris (registre, login, verificació d'email) |

---

## Esquema de la Base de Dades Room (versió 3)

La base de dades (`travel_sigma_db`) conté quatre taules. La versió canvia amb cada migració; l'estratègia actual és `fallbackToDestructiveMigration` (veure secció Migració).

### Taula `trips`

| Columna | Tipus | Restriccions | Notes |
|---|---|---|---|
| `id` | INTEGER | PK, autoGenerate | |
| `name` | TEXT | NOT NULL | |
| `destinationId` | INTEGER | NOT NULL | Referència lògica a `sampleDestinations` |
| `hotelId` | INTEGER | NOT NULL | Referència lògica a `sampleHotels` |
| `startDate` | TEXT | NOT NULL | Format ISO-8601: `yyyy-MM-dd` |
| `endDate` | TEXT | NOT NULL | Format ISO-8601: `yyyy-MM-dd` |
| `persons` | INTEGER | NOT NULL | |
| `heroColor` | INTEGER | NOT NULL | Color ARGB emmagatzemat com a Long |
| `userId` | TEXT | NOT NULL | Firebase UID de l'usuari propietari |

### Taula `itinerary_activities`

| Columna | Tipus | Restriccions | Notes |
|---|---|---|---|
| `id` | INTEGER | PK, autoGenerate | |
| `tripId` | INTEGER | NOT NULL, FK → `trips.id` CASCADE DELETE | Índex creat automàticament |
| `time` | TEXT | NOT NULL | Format `HH:mm` |
| `title` | TEXT | NOT NULL | |
| `subtitle` | TEXT | NOT NULL | |
| `cost` | REAL | NOT NULL | |
| `tag` | TEXT | NULLABLE | `ActivityType.name` (`FOOD`, `SIGHTSEEING`, `TRANSIT`, `OTHERS`) o null |
| `date` | TEXT | NOT NULL | Format ISO-8601: `yyyy-MM-dd` |

### Taula `users`

| Columna | Tipus | Restriccions | Notes |
|---|---|---|---|
| `userId` | TEXT | PK | Firebase UID |
| `name` | TEXT | NOT NULL | |
| `email` | TEXT | NOT NULL | |
| `username` | TEXT | NOT NULL | Únic per usuari |
| `dateOfBirth` | TEXT | NOT NULL | |
| `phone` | TEXT | NOT NULL | Pot ser buit (`""`) |
| `address` | TEXT | NOT NULL | Pot ser buit |
| `country` | TEXT | NOT NULL | Pot ser buit |
| `acceptsReceiveEmails` | INTEGER | NOT NULL | Boolean: 0 o 1 |
| `language` | TEXT | NOT NULL | Codi d'idioma (`en`, `es`, `ca`) |
| `theme` | TEXT | NOT NULL | `system` / `light` / `dark` |
| `notificationsEnabled` | INTEGER | NOT NULL | Boolean: 0 o 1 |

### Taula `access_log`

| Columna | Tipus | Restriccions | Notes |
|---|---|---|---|
| `id` | INTEGER | PK, autoGenerate | |
| `userId` | TEXT | NOT NULL | Firebase UID |
| `action` | TEXT | NOT NULL | `LOGIN` o `LOGOUT` |
| `dateTime` | TEXT | NOT NULL | Format ISO-8601 datetime |

---

## Estratègia de Migració

La base de dades utilitza `fallbackToDestructiveMigration(dropAllTables = true)`: quan la versió de la BD incrementa, totes les taules s'eliminen i es recreen des de zero.

Les dades de viatges inicials es reinjecten automàticament via `TripRepository.seedIfEmpty()` en el primer login de cada usuari, de manera que la pèrdua de dades per migració és recuperable sense intervenció manual.

---

## Serveis Externs

| Servei | Ús |
|---|---|
| **Firebase Authentication** | Registre, login, verificació d'email, logout, reset de contrasenya |
| **Room (SQLite local)** | Persistència de viatges, activitats, perfil d'usuari i logs d'accés |
| **Cloud Firestore** | No s'utilitza activament (les dades es desen a Room) |

---

## Patró Repositori i Hilt

Cada repositori s'exposa com a interfície al paquet `domain/` i la implementació concreta s'injecta via Hilt (`@Binds` a `RepositoryModule`). Això permet canviar la font de dades (ex: de Room a Firestore) sense tocar els ViewModels ni la UI.

```
domain/TripRepository        ← interfície
data/repository/
  RoomTripRepositoryImpl     ← implementació activa (injectada per Hilt)
  TripRepositoryImpl         ← implementació fake en memòria (usada als tests unitaris)
```
