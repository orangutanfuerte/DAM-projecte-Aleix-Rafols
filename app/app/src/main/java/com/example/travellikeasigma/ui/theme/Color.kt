package com.example.travellikeasigma.ui.theme

import androidx.compose.ui.graphics.Color

// ─── Light scheme ────────────────────────────────────────────
// Primary — Ocean Blue (#1565C0 seed)
val md_theme_light_primary = Color(0xFF1565C0)
val md_theme_light_onPrimary = Color(0xFFFFFFFF)
val md_theme_light_primaryContainer = Color(0xFFD4E3FF)
val md_theme_light_onPrimaryContainer = Color(0xFF001C3B)

// Secondary — Sunset Orange (#EF6C00 seed)
val md_theme_light_secondary = Color(0xFFEF6C00)
val md_theme_light_onSecondary = Color(0xFFFFFFFF)
val md_theme_light_secondaryContainer = Color(0xFFFFDCC2)
val md_theme_light_onSecondaryContainer = Color(0xFF2E1500)

// Tertiary — Fresh Green (#2E7D32 seed)
val md_theme_light_tertiary = Color(0xFF2E7D32)
val md_theme_light_onTertiary = Color(0xFFFFFFFF)
val md_theme_light_tertiaryContainer = Color(0xFFC8E6C9)
val md_theme_light_onTertiaryContainer = Color(0xFF002106)

// Error — Terracotta Red
val md_theme_light_error = Color(0xFFBA1A1A)
val md_theme_light_onError = Color(0xFFFFFFFF)
val md_theme_light_errorContainer = Color(0xFFFFDAD6)
val md_theme_light_onErrorContainer = Color(0xFF410002)

// Surfaces — cool-tinted neutrals
val md_theme_light_background = Color(0xFFFDFBFF)
val md_theme_light_onBackground = Color(0xFF1A1C1E)
val md_theme_light_surface = Color(0xFFFDFBFF)
val md_theme_light_onSurface = Color(0xFF1A1C1E)
val md_theme_light_surfaceVariant = Color(0xFFE0E2EC)
val md_theme_light_onSurfaceVariant = Color(0xFF44474E)
val md_theme_light_outline = Color(0xFF74777F)
val md_theme_light_outlineVariant = Color(0xFFC4C6D0)

// Surface containers — cool-tinted grays for Card, NavBar, TopAppBar, etc.
val md_theme_light_surfaceTint = Color(0xFF1565C0)
val md_theme_light_surfaceBright = Color(0xFFFDFBFF)
val md_theme_light_surfaceDim = Color(0xFFDBD9DE)
val md_theme_light_surfaceContainer = Color(0xFFF0EFF4)
val md_theme_light_surfaceContainerHigh = Color(0xFFEAE9EE)
val md_theme_light_surfaceContainerHighest = Color(0xFFE4E2E8)
val md_theme_light_surfaceContainerLow = Color(0xFFF6F4F9)
val md_theme_light_surfaceContainerLowest = Color(0xFFFFFFFF)

// Inverse & scrim
val md_theme_light_inverseSurface = Color(0xFF2F3033)
val md_theme_light_inverseOnSurface = Color(0xFFF1F0F4)
val md_theme_light_inversePrimary = Color(0xFFA6C8FF)
val md_theme_light_scrim = Color(0xFF000000)

// ─── Dark scheme ─────────────────────────────────────────────
// Primary
val md_theme_dark_primary = Color(0xFFA6C8FF)
val md_theme_dark_onPrimary = Color(0xFF003060)
val md_theme_dark_primaryContainer = Color(0xFF3D84CC)
val md_theme_dark_onPrimaryContainer = Color(0xFFFFFFFF)

// Secondary
val md_theme_dark_secondary = Color(0xFFFFB77C)
val md_theme_dark_onSecondary = Color(0xFF4E2600)
val md_theme_dark_secondaryContainer = Color(0xFFD4853A)
val md_theme_dark_onSecondaryContainer = Color(0xFFFFFFFF)

// Tertiary
val md_theme_dark_tertiary = Color(0xFFA5D6A7)
val md_theme_dark_onTertiary = Color(0xFF00390C)
val md_theme_dark_tertiaryContainer = Color(0xFF4A9650)
val md_theme_dark_onTertiaryContainer = Color(0xFFFFFFFF)

// Error
val md_theme_dark_error = Color(0xFFFFB4AB)
val md_theme_dark_onError = Color(0xFF690005)
val md_theme_dark_errorContainer = Color(0xFF93000A)
val md_theme_dark_onErrorContainer = Color(0xFFFFDAD6)

// Surfaces — cool dark neutrals
val md_theme_dark_background = Color(0xFF1A1C1E)
val md_theme_dark_onBackground = Color(0xFFE3E2E6)
val md_theme_dark_surface = Color(0xFF1A1C1E)
val md_theme_dark_onSurface = Color(0xFFE3E2E6)
val md_theme_dark_surfaceVariant = Color(0xFF44474E)
val md_theme_dark_onSurfaceVariant = Color(0xFFC4C6D0)
val md_theme_dark_outline = Color(0xFF8E9099)
val md_theme_dark_outlineVariant = Color(0xFF44474E)

// Surface containers — cool dark grays
val md_theme_dark_surfaceTint = Color(0xFFA6C8FF)
val md_theme_dark_surfaceBright = Color(0xFF383A3E)
val md_theme_dark_surfaceDim = Color(0xFF1A1C1E)
val md_theme_dark_surfaceContainer = Color(0xFF1E2022)
val md_theme_dark_surfaceContainerHigh = Color(0xFF292A2D)
val md_theme_dark_surfaceContainerHighest = Color(0xFF343538)
val md_theme_dark_surfaceContainerLow = Color(0xFF1A1C1E)
val md_theme_dark_surfaceContainerLowest = Color(0xFF0F1012)

// Inverse & scrim
val md_theme_dark_inverseSurface = Color(0xFFE3E2E6)
val md_theme_dark_inverseOnSurface = Color(0xFF2F3033)
val md_theme_dark_inversePrimary = Color(0xFF1565C0)
val md_theme_dark_scrim = Color(0xFF000000)

// ─── Semantic colors ─────────────────────────────────────────

// Activity tag colors (ItineraryScreen)
val FoodTagBackground = Color(0xFFFFF3E0)
val FoodTagText = Color(0xFFE65100)
val SightseeingTagBackground = Color(0xFFE8F5E9)
val SightseeingTagText = Color(0xFF2E7D32)
val TransitTagBackground = Color(0xFFE3F2FD)
val TransitTagText = Color(0xFF1565C0)

// Map placeholder (PlacesScreen)
val MapPlaceholderBackground = Color(0xFFBBDEFB)
val MapPlaceholderIcon = Color(0xFF1976D2)

// Hero card palette — used for new trip colors
val heroColors = listOf(
    Color(0xFF7851A8), // Purple
    Color(0xFF3A6EA5), // Ocean Blue
    Color(0xFF4A7C59), // Forest Green
    Color(0xFFC45B28), // Burnt Orange
    Color(0xFF9E4B6B), // Raspberry
    Color(0xFF3D7D8A), // Teal
    Color(0xFF8A6A2E), // Amber
    Color(0xFF5B6EA5), // Slate Blue
)
