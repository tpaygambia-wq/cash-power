package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// ==========================================
// MODERN ELECTRIC FINTECH PALETTE
// ==========================================

// Deep Modern Nocturne Slate Backgrounds
val ModernBg = Color(0xFF080D11)
val ModernSurface = Color(0xFF0F171D)
val ModernSurfaceElevated = Color(0xFF152028)
val ModernSurfaceHighlight = Color(0xFF1B2A34)
val ModernSurfaceBorder = Color(0xFF223542)

// Ultra-Modern Electric Mint & Emerald Accents
val ElectricMint = Color(0xFF00E599)
val ElectricMintGlow = Color(0xFF57FFBF)
val ElectricMintDeep = Color(0xFF00A86B)
val ElectricMintContainer = Color(0xFF052B1E)
val OnElectricMint = Color(0xFF05170F)

// High-Energy Cyan & Ice Accents
val ElectricCyan = Color(0xFF00D2FF)
val ElectricCyanContainer = Color(0xFF062C38)
val OnElectricCyan = Color(0xFF001E26)

// Luminous Sun Gold Accents
val SunGold = Color(0xFFFFB800)
val SunGoldGlow = Color(0xFFFFD54F)
val SunGoldContainer = Color(0xFF3B2800)

// Crisp Modern Typography
val TextPrimary = Color(0xFFFFFFFF)
val TextSecondary = Color(0xFFA6B8C2)
val TextMuted = Color(0xFF677E8C)

// Rim Lights & Borders
val RimLightMint = Color(0x3800E599)
val RimLightCyan = Color(0x3300D2FF)
val BorderGlass = Color(0x2400E599)
val DividerModern = Color(0x1F2A3F4D)

// Provider Accent Colors
val ColorWave = Color(0xFF00A3FF)
val ColorQMoney = Color(0xFFFF4757)
val ColorAfrimoney = Color(0xFFA855F7)
val ColorBank = Color(0xFF2ED573)

// ==========================================
// MODERN FINTECH GRADIENTS
// ==========================================

val ModernPrimaryButtonGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFF00BA74), Color(0xFF00E599), Color(0xFF00D2A6))
)

val ModernPrimaryButtonDisabledGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFF0E221B), Color(0xFF132B22))
)

val ModernHeaderGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFF060B0E), Color(0xFF0D171E))
)

val ModernCardRimGradient = Brush.verticalGradient(
    colors = listOf(Color(0x3D00E599), Color(0x0A00E599))
)

val ModernProBadgeGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFFFFB800), Color(0xFFFF8800))
)

val ModernCyanMintGradient = Brush.horizontalGradient(
    colors = listOf(ElectricCyan, ElectricMint)
)

// ==========================================
// COMPATIBILITY ALIASES
// ==========================================
val GambiaGreenPrimary = ElectricMint
val GambiaGreenPrimaryContainer = ElectricMintContainer
val OnGambiaGreenPrimary = OnElectricMint
val OnGambiaGreenPrimaryContainer = ElectricMintGlow

val ProEmeraldPrimary = ElectricMint
val ProEmeraldGlow = ElectricMintGlow
val ProEmeraldDeep = ElectricMintDeep
val ProEmeraldContainer = ElectricMintContainer
val OnProEmerald = OnElectricMint

val EnergyAmber = SunGold
val EnergyAmberGlow = SunGoldGlow
val EnergyAmberContainer = SunGoldContainer

val ProBorderHighlight = RimLightMint
val ProBorderSubtle = BorderGlass
val ProDivider = DividerModern
val ProSurfaceDark = ModernSurface
val ProSurfaceElevated = ModernSurfaceElevated
val ProSurfaceVariant = ModernSurfaceHighlight
val ProTextPrimary = TextPrimary
val ProTextSecondary = TextSecondary
val ProTextMuted = TextMuted

val ProCardBorderGradient = ModernCardRimGradient
val ProEmeraldButtonGradient = ModernPrimaryButtonGradient
val ProEmeraldButtonDisabledGradient = ModernPrimaryButtonDisabledGradient
val ProTopBarGradient = ModernHeaderGradient
val ProGoldBadgeGradient = ModernProBadgeGradient
