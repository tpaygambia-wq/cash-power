package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ModernElectricColorScheme = darkColorScheme(
    primary = ElectricMint,
    onPrimary = OnElectricMint,
    primaryContainer = ElectricMintContainer,
    onPrimaryContainer = ElectricMintGlow,
    secondary = SunGold,
    onSecondary = Color(0xFF261900),
    secondaryContainer = SunGoldContainer,
    onSecondaryContainer = SunGoldGlow,
    tertiary = ElectricCyan,
    onTertiary = OnElectricCyan,
    tertiaryContainer = ElectricCyanContainer,
    onTertiaryContainer = Color(0xFFBCEFFF),
    background = ModernBg,
    onBackground = TextPrimary,
    surface = ModernSurface,
    onSurface = TextPrimary,
    surfaceVariant = ModernSurfaceHighlight,
    onSurfaceVariant = TextSecondary,
    outline = RimLightMint,
    outlineVariant = BorderGlass
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = ModernElectricColorScheme,
        typography = Typography,
        content = content
    )
}
