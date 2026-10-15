package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WbIncandescent
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.EnergyAmber
import com.example.ui.theme.ProBorderHighlight
import com.example.ui.theme.ProCardBorderGradient
import com.example.ui.theme.ProDivider
import com.example.ui.theme.ProEmeraldContainer
import com.example.ui.theme.ProEmeraldGlow
import com.example.ui.theme.ProEmeraldPrimary
import com.example.ui.theme.ProSurfaceDark
import com.example.ui.theme.ProSurfaceElevated
import com.example.ui.theme.ProSurfaceVariant
import com.example.ui.theme.ProTextMuted
import com.example.ui.theme.ProTextPrimary
import com.example.ui.theme.ProTextSecondary

@Composable
fun AmountCard(
    amountInput: String,
    calculatedFee: Int,
    totalCost: Int,
    estimatedUnits: Double,
    onAmountChanged: (String) -> Unit,
    onSelectPreset: (Int) -> Unit
) {
    val amountInt = amountInput.toIntOrNull() ?: 0
    val isUnderMinimum = amountInt in 1..499
    val presets = listOf(500, 1000, 1500, 2000, 5000)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(10.dp, RoundedCornerShape(18.dp), spotColor = Color(0x3810B981))
            .testTag("amount_card"),
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
                        .background(EnergyAmber.copy(alpha = 0.15f))
                        .border(1.dp, EnergyAmber.copy(alpha = 0.35f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Payments,
                        contentDescription = "Payment Amount",
                        tint = EnergyAmber,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "Purchase Amount (GMD)",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = ProTextPrimary
                        )
                    )
                    Text(
                        text = "Min. 500 GMD • Fee: 15 GMD (<1000) or 20 GMD (≥1000)",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = ProTextMuted
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Amount Input with Pro Surface
            OutlinedTextField(
                value = amountInput,
                onValueChange = onAmountChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("amount_input_field"),
                label = { Text("Amount in Gambian Dalasi", color = ProTextSecondary) },
                placeholder = { Text("e.g. 1000", color = ProTextMuted) },
                prefix = {
                    Text(
                        text = "GMD ",
                        fontWeight = FontWeight.ExtraBold,
                        color = ProEmeraldGlow
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(14.dp),
                isError = isUnderMinimum,
                supportingText = if (isUnderMinimum) {
                    {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Minimum purchase amount is 500 GMD")
                        }
                    }
                } else null,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = ProSurfaceElevated,
                    unfocusedContainerColor = ProSurfaceElevated,
                    focusedTextColor = ProTextPrimary,
                    unfocusedTextColor = ProTextPrimary,
                    focusedBorderColor = ProEmeraldPrimary,
                    unfocusedBorderColor = ProBorderHighlight,
                    focusedLabelColor = ProEmeraldPrimary
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Quick Preset Chips
            Text(
                text = "Quick Presets:",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = ProTextMuted
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                presets.forEach { preset ->
                    val isSelected = amountInput == preset.toString()
                    FilterChip(
                        selected = isSelected,
                        onClick = { onSelectPreset(preset) },
                        label = { Text("$preset GMD", fontWeight = FontWeight.SemiBold, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = ProSurfaceElevated,
                            labelColor = ProTextSecondary,
                            selectedContainerColor = EnergyAmber.copy(alpha = 0.22f),
                            selectedLabelColor = EnergyAmber
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = ProBorderHighlight,
                            selectedBorderColor = EnergyAmber
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Live Calculation Breakdown Box with Dark Glass Depth
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(ProSurfaceElevated)
                    .border(1.dp, ProBorderHighlight, RoundedCornerShape(14.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Cash Power Value",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = ProTextSecondary
                            )
                        )
                        Text(
                            text = "${amountInt} GMD",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = ProTextPrimary
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Service / NAWEC Fee",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = ProTextSecondary
                                )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(
                                        if (amountInt < 1000) ProEmeraldContainer
                                        else EnergyAmber.copy(alpha = 0.2f)
                                    )
                                    .padding(horizontal = 5.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = if (amountInt < 1000) "Tier 1: <1000" else "Tier 2: ≥1000",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (amountInt < 1000) ProEmeraldGlow else EnergyAmber
                                )
                            }
                        }
                        Text(
                            text = "+$calculatedFee GMD",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = ProTextPrimary
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.WbIncandescent,
                                contentDescription = null,
                                tint = EnergyAmber,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Est. Electricity Units",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = ProTextSecondary
                                )
                            )
                        }
                        Text(
                            text = "~ $estimatedUnits kWh",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = ProEmeraldGlow
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = ProDivider)
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total Charged:",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = ProTextPrimary
                            )
                        )
                        Text(
                            text = "$totalCost GMD",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Black,
                                fontSize = 18.sp,
                                color = ProEmeraldGlow
                            )
                        )
                    }
                }
            }
        }
    }
}
