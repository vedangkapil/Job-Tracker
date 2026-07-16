package com.Vedang.careerflow.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = CareerBlueLight,
    onPrimary = Ink950,
    primaryContainer = CareerBlueContainer,
    onPrimaryContainer = CareerBlueContainerLight,
    secondary = Mint,
    onSecondary = Ink950,
    secondaryContainer = MintContainer,
    onSecondaryContainer = MintContainerLight,
    background = Ink950,
    onBackground = Cloud50,
    surface = Ink900,
    onSurface = Cloud50,
    surfaceVariant = Ink800,
    onSurfaceVariant = ColorSlate,
    outline = Ink700,
    error = ErrorDark,
    onError = Ink950
)

private val LightColorScheme = lightColorScheme(
    primary = CareerBlue,
    onPrimary = Cloud50,
    primaryContainer = CareerBlueContainerLight,
    onPrimaryContainer = Ink950,
    secondary = Color(0xFF126B58),
    onSecondary = Cloud50,
    secondaryContainer = MintContainerLight,
    onSecondaryContainer = Color(0xFF06382E),
    background = Cloud50,
    onBackground = Ink950,
    surface = Color.White,
    onSurface = Ink950,
    surfaceVariant = Cloud100,
    onSurfaceVariant = Slate500,
    outline = Color(0xFFCBD5E1),
    error = ErrorLight,
    onError = Cloud50
)

@Composable
fun CareerFlowTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = CareerFlowShapes,
        content = content
    )
}
