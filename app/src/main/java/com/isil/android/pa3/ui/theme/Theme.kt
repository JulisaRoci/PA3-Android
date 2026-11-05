package com.isil.android.pa3.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = IOSColors.Blue,
    onPrimary = Color.White,
    secondary = IOSColors.Indigo,
    onSecondary = Color.White,
    tertiary = IOSColors.Pink,
    background = IOSColors.GroupedBackground,
    onBackground = IOSColors.Label,
    surface = Color.White,
    onSurface = IOSColors.Label,
    surfaceVariant = IOSColors.Gray6,
    onSurfaceVariant = IOSColors.SecondaryLabel,
    outline = IOSColors.Separator,
    error = IOSColors.Red,
    onError = Color.White
)

private val DarkColorScheme = darkColorScheme(
    primary = IOSColors.BlueDark,
    onPrimary = Color.White,
    secondary = IOSColors.Indigo,
    onSecondary = Color.White,
    tertiary = IOSColors.Pink,
    background = IOSColors.GroupedBackgroundDark,
    onBackground = IOSColors.LabelDark,
    surface = IOSColors.SecondaryBackgroundDark,
    onSurface = IOSColors.LabelDark,
    surfaceVariant = IOSColors.SecondaryBackgroundDark,
    onSurfaceVariant = IOSColors.SecondaryLabelDark,
    outline = IOSColors.SeparatorDark,
    error = IOSColors.RedDark,
    onError = Color.White
)

@Composable
fun Grupo1PA3Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}
