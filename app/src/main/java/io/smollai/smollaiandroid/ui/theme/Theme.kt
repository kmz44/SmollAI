/*
 * Copyright (C) 2024 smollai IA
 * Modern theme for teens with clean, single source of truth
 */

package io.smollai.smollaiandroid.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// smollai Light Theme
private val smollaiLightColorScheme = lightColorScheme(
    primary = SmollAIPrimary,
    onPrimary = smollaiTextOnPrimary,
    primaryContainer = SmollAIPrimary.copy(alpha = 0.1f),
    onPrimaryContainer = SmollAIPrimaryDark,
    
    secondary = SmollAISecondary,
    onSecondary = smollaiTextOnPrimary,
    secondaryContainer = SmollAISecondary.copy(alpha = 0.1f),
    onSecondaryContainer = SmollAISecondaryDark,
    
    tertiary = smollaiAccent,
    onTertiary = smollaiTextOnPrimary,
    tertiaryContainer = smollaiAccent.copy(alpha = 0.1f),
    onTertiaryContainer = smollaiAccentDark,
    
    error = smollaiError,
    onError = smollaiTextOnPrimary,
    errorContainer = smollaiError.copy(alpha = 0.1f),
    onErrorContainer = smollaiError,
    
    background = SmollAIBackgroundLight,
    onBackground = smollaiTextPrimary,
    
    surface = SmollAISurfaceLight,
    onSurface = smollaiTextPrimary,
    surfaceVariant = SmollAISurfaceVariantLight,
    onSurfaceVariant = smollaiTextSecondary,
    
    outline = smollaiTextTertiary,
    outlineVariant = smollaiTextTertiary.copy(alpha = 0.3f)
)

// smollai Dark Theme
private val smollaiDarkColorScheme = darkColorScheme(
    primary = SmollAIPrimary,
    onPrimary = smollaiTextOnPrimary,
    primaryContainer = SmollAIPrimaryDark,
    onPrimaryContainer = SmollAIPrimary.copy(alpha = 0.8f),
    
    secondary = SmollAISecondary,
    onSecondary = smollaiTextOnPrimary,
    secondaryContainer = SmollAISecondaryDark,
    onSecondaryContainer = SmollAISecondary.copy(alpha = 0.8f),
    
    tertiary = smollaiAccent,
    onTertiary = smollaiTextOnPrimary,
    tertiaryContainer = smollaiAccentDark,
    onTertiaryContainer = smollaiAccent.copy(alpha = 0.8f),
    
    error = smollaiError,
    onError = smollaiTextOnPrimary,
    errorContainer = smollaiError.copy(alpha = 0.2f),
    onErrorContainer = smollaiError,
    
    background = SmollAIBackgroundDark,
    onBackground = smollaiTextOnDark,
    
    surface = SmollAISurfaceDark,
    onSurface = smollaiTextOnDark,
    surfaceVariant = SmollAISurfaceVariantDark,
    onSurfaceVariant = smollaiTextSecondaryDark,
    
    outline = smollaiTextSecondaryDark,
    outlineVariant = smollaiTextTertiaryDark
)

/**
 * Main smollai theme function - Single source of truth
 */
@Composable
fun SmollAITheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        smollaiDarkColorScheme
    } else {
        smollaiLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = smollaiTypography,
        content = content
    )
}

/**
 * Legacy theme function for backward compatibility
 */
@Composable
fun smollaiAndroidTheme(content: @Composable () -> Unit) {
    SmollAITheme(content = content)
}
