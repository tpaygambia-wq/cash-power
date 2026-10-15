package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.ElectricMint
import com.example.ui.theme.ElectricMintGlow
import com.example.ui.theme.ModernCyanMintGradient
import com.example.ui.theme.ModernHeaderGradient
import com.example.ui.theme.ModernProBadgeGradient
import com.example.ui.theme.SunGold
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    onProInfoClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(ModernHeaderGradient)
    ) {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                titleContentColor = TextPrimary
            ),
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Elevated Glowing Power Emblem with Cyan-Mint Gradient
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .shadow(10.dp, CircleShape, spotColor = ElectricMint)
                            .clip(CircleShape)
                            .background(ModernCyanMintGradient),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = "Cash Power Bolt",
                            tint = Color(0xFF04120B),
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Cash Power",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    color = TextPrimary,
                                    fontSize = 18.sp,
                                    letterSpacing = 0.3.sp
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))

                            // Luminous Modern Pro Badge
                            Box(
                                modifier = Modifier
                                    .shadow(6.dp, RoundedCornerShape(6.dp), spotColor = SunGold)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(ModernProBadgeGradient)
                                    .border(
                                        width = 1.dp,
                                        color = Color(0xFFFFE599),
                                        shape = RoundedCornerShape(6.dp)
                                    )
                                    .padding(horizontal = 7.dp, vertical = 2.dp)
                                    .testTag("pro_badge")
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.WorkspacePremium,
                                        contentDescription = null,
                                        tint = Color(0xFF261900),
                                        modifier = Modifier.size(11.dp)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = "PRO v2.0",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color(0xFF261900),
                                        letterSpacing = 0.5.sp
                                    )
                                }
                            }
                        }

                        Text(
                            text = "NAWEC STS Gateway • Instant Token Dispatch",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextMuted
                        )
                    }
                }
            },
            actions = {
                IconButton(
                    onClick = onProInfoClick,
                    modifier = Modifier.testTag("pro_info_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Pro Edition Info",
                        tint = TextPrimary.copy(alpha = 0.85f)
                    )
                }
            }
        )

        // Bottom crisp glowing cyan-mint border line
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(1.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color.Transparent,
                            ElectricMint.copy(alpha = 0.5f),
                            ElectricCyan.copy(alpha = 0.5f),
                            Color.Transparent
                        )
                    )
                )
        )
    }
}
