package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.SendToMobile
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ColorAfrimoney
import com.example.ui.theme.ColorBank
import com.example.ui.theme.ColorQMoney
import com.example.ui.theme.ColorWave
import com.example.ui.theme.ProBorderHighlight
import com.example.ui.theme.ProCardBorderGradient
import com.example.ui.theme.ProEmeraldPrimary
import com.example.ui.theme.ProSurfaceDark
import com.example.ui.theme.ProSurfaceElevated
import com.example.ui.theme.ProSurfaceVariant
import com.example.ui.theme.ProTextMuted
import com.example.ui.theme.ProTextPrimary
import com.example.ui.theme.ProTextSecondary

data class ProviderOption(
    val id: String,
    val name: String,
    val tagline: String,
    val brandColor: Color,
    val icon: ImageVector
)

@Composable
fun PaymentMethodSelector(
    selectedProvider: String,
    phoneNumber: String,
    onSelectProvider: (String) -> Unit,
    onPhoneChanged: (String) -> Unit
) {
    val providers = listOf(
        ProviderOption("Wave", "Wave", "Zero fee push", ColorWave, Icons.Default.SendToMobile),
        ProviderOption("QMoney", "QMoney", "QCell Wallet", ColorQMoney, Icons.Default.Smartphone),
        ProviderOption("Afrimoney", "Afrimoney", "Africell Wallet", ColorAfrimoney, Icons.Default.AccountBalanceWallet),
        ProviderOption("Bank Card", "Bank / Card", "EcoBank/GTBank", ColorBank, Icons.Default.AccountBalance)
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(10.dp, RoundedCornerShape(18.dp), spotColor = Color(0x3810B981))
            .testTag("payment_method_card"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = ProSurfaceDark
        ),
        border = BorderStroke(1.dp, ProCardBorderGradient)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(ColorWave.copy(alpha = 0.15f))
                        .border(1.dp, ColorWave.copy(alpha = 0.35f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PhoneAndroid,
                        contentDescription = "Payment Provider",
                        tint = ColorWave,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "Payment Method",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = ProTextPrimary
                        )
                    )
                    Text(
                        text = "Instant mobile money payment authorization",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = ProTextMuted
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2x2 Grid of Providers
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ProviderTile(
                        provider = providers[0],
                        isSelected = selectedProvider == providers[0].id,
                        modifier = Modifier.weight(1f),
                        onClick = { onSelectProvider(providers[0].id) }
                    )
                    ProviderTile(
                        provider = providers[1],
                        isSelected = selectedProvider == providers[1].id,
                        modifier = Modifier.weight(1f),
                        onClick = { onSelectProvider(providers[1].id) }
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ProviderTile(
                        provider = providers[2],
                        isSelected = selectedProvider == providers[2].id,
                        modifier = Modifier.weight(1f),
                        onClick = { onSelectProvider(providers[2].id) }
                    )
                    ProviderTile(
                        provider = providers[3],
                        isSelected = selectedProvider == providers[3].id,
                        modifier = Modifier.weight(1f),
                        onClick = { onSelectProvider(providers[3].id) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Phone Number Input with Gambia +220 prefix
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = onPhoneChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("payer_phone_field"),
                label = { Text("$selectedProvider Mobile Number", color = ProTextSecondary) },
                placeholder = { Text("7XXXXXX or 9XXXXXX", color = ProTextMuted) },
                prefix = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "🇬🇲 +220 ",
                            fontWeight = FontWeight.Bold,
                            color = ProTextPrimary
                        )
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = ProSurfaceElevated,
                    unfocusedContainerColor = ProSurfaceElevated,
                    focusedTextColor = ProTextPrimary,
                    unfocusedTextColor = ProTextPrimary,
                    focusedBorderColor = ProEmeraldPrimary,
                    unfocusedBorderColor = ProBorderHighlight,
                    focusedLabelColor = ProEmeraldPrimary
                ),
                supportingText = {
                    Text(
                        text = "You will receive an instant payment authorization prompt on this phone.",
                        fontSize = 11.sp,
                        color = ProTextMuted
                    )
                }
            )
        }
    }
}

@Composable
private fun ProviderTile(
    provider: ProviderOption,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .shadow(if (isSelected) 6.dp else 0.dp, RoundedCornerShape(14.dp), spotColor = provider.brandColor)
            .clip(RoundedCornerShape(14.dp))
            .background(
                if (isSelected) provider.brandColor.copy(alpha = 0.16f)
                else ProSurfaceElevated
            )
            .border(
                border = BorderStroke(
                    width = if (isSelected) 2.dp else 1.dp,
                    color = if (isSelected) provider.brandColor else ProBorderHighlight
                ),
                shape = RoundedCornerShape(14.dp)
            )
            .clickable(onClick = onClick)
            .padding(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(provider.brandColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = provider.icon,
                        contentDescription = provider.name,
                        tint = Color.White,
                        modifier = Modifier.size(19.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = provider.name,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = ProTextPrimary
                        )
                    )
                    Text(
                        text = provider.tagline,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp,
                            color = ProTextMuted
                        )
                    )
                }
            }

            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Selected",
                    tint = provider.brandColor,
                    modifier = Modifier.size(19.dp)
                )
            }
        }
    }
}
