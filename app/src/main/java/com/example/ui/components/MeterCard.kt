package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.ElectricMeter
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MeterInfo
import com.example.ui.theme.EnergyAmber
import com.example.ui.theme.ProBorderHighlight
import com.example.ui.theme.ProCardBorderGradient
import com.example.ui.theme.ProEmeraldButtonDisabledGradient
import com.example.ui.theme.ProEmeraldButtonGradient
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
fun MeterCard(
    meterInput: String,
    verifiedMeter: MeterInfo?,
    isVerifying: Boolean,
    quickDemoMeters: List<String>,
    onMeterChanged: (String) -> Unit,
    onVerifyClick: () -> Unit,
    onClearClick: () -> Unit,
    onSaveFavoriteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(10.dp, RoundedCornerShape(18.dp), spotColor = Color(0x3810B981))
            .testTag("meter_card"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = ProSurfaceDark
        ),
        border = BorderStroke(1.dp, ProCardBorderGradient)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(
                                if (verifiedMeter != null) ProEmeraldContainer
                                else ProSurfaceVariant
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ElectricMeter,
                            contentDescription = "Meter Icon",
                            tint = if (verifiedMeter != null) ProEmeraldGlow else ProTextSecondary,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "NAWEC Meter Number",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = ProTextPrimary
                            )
                        )
                        Text(
                            text = "Standard 11-Digit STS Prepaid Meter",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = ProTextMuted
                            )
                        )
                    }
                }

                if (verifiedMeter != null) {
                    Row {
                        IconButton(
                            onClick = onSaveFavoriteClick,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.BookmarkBorder,
                                contentDescription = "Save Meter",
                                tint = ProEmeraldGlow
                            )
                        }
                        IconButton(
                            onClick = onClearClick,
                            modifier = Modifier.size(36.dp).testTag("clear_meter_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = "Clear Meter",
                                tint = Color(0xFFF87171)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (verifiedMeter == null) {
                val cleanMeter = meterInput.filter { it.isDigit() }
                val isMeterValid = cleanMeter.length == 11
                val hasInput = cleanMeter.isNotEmpty()
                val isPartiallyEntered = hasInput && cleanMeter.length < 11
                val isVerifyEnabled = isMeterValid && !isVerifying

                // Meter Input Form with Dark Surface and Emerald Rim
                OutlinedTextField(
                    value = meterInput,
                    onValueChange = onMeterChanged,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("meter_input_field"),
                    label = { Text("Enter 11-digit Meter Number", color = ProTextSecondary) },
                    placeholder = { Text("e.g. 04219985523", color = ProTextMuted) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = if (isMeterValid) ImeAction.Done else ImeAction.None
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { if (isVerifyEnabled) onVerifyClick() }
                    ),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = ProSurfaceElevated,
                        unfocusedContainerColor = ProSurfaceElevated,
                        focusedTextColor = ProTextPrimary,
                        unfocusedTextColor = ProTextPrimary,
                        focusedBorderColor = if (isMeterValid) ProEmeraldPrimary else EnergyAmber,
                        unfocusedBorderColor = if (isMeterValid) ProEmeraldPrimary.copy(alpha = 0.5f) else ProBorderHighlight,
                        focusedLabelColor = if (isMeterValid) ProEmeraldPrimary else EnergyAmber
                    ),
                    supportingText = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            when {
                                isMeterValid -> {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = ProEmeraldGlow,
                                            modifier = Modifier.size(13.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "Standard NAWEC 11-digit format verified",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = ProEmeraldGlow
                                        )
                                    }
                                }
                                isPartiallyEntered -> {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Info,
                                            contentDescription = null,
                                            tint = EnergyAmber,
                                            modifier = Modifier.size(13.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "NAWEC format: 11 digits (${11 - cleanMeter.length} more needed)",
                                            fontSize = 11.sp,
                                            color = EnergyAmber
                                        )
                                    }
                                }
                                else -> {
                                    Text(
                                        text = "Enter standard 11-digit NAWEC meter number",
                                        fontSize = 11.sp,
                                        color = ProTextMuted
                                    )
                                }
                            }
                            Text(
                                text = "${cleanMeter.length}/11",
                                fontSize = 11.sp,
                                fontWeight = if (isMeterValid) FontWeight.Bold else FontWeight.Normal,
                                color = when {
                                    isMeterValid -> ProEmeraldGlow
                                    isPartiallyEntered -> EnergyAmber
                                    else -> ProTextMuted
                                }
                            )
                        }
                    },
                    trailingIcon = {
                        if (isVerifying) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.5.dp,
                                color = ProEmeraldPrimary
                            )
                        } else {
                            IconButton(
                                onClick = { if (isVerifyEnabled) onVerifyClick() },
                                enabled = isVerifyEnabled,
                                modifier = Modifier.testTag("verify_meter_button")
                            ) {
                                val buttonGradient = if (isVerifyEnabled) ProEmeraldButtonGradient else ProEmeraldButtonDisabledGradient
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .shadow(if (isVerifyEnabled) 6.dp else 0.dp, CircleShape, spotColor = ProEmeraldPrimary)
                                        .clip(CircleShape)
                                        .background(buttonGradient),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = "Verify Meter",
                                        tint = if (isVerifyEnabled) Color.White else ProTextMuted,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Quick Demo Pill buttons
                Text(
                    text = "Quick Sample Meters (Tap to try):",
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
                    quickDemoMeters.forEach { meterNo ->
                        FilterChip(
                            selected = meterInput == meterNo,
                            onClick = {
                                onMeterChanged(meterNo)
                                onVerifyClick()
                            },
                            label = { Text(meterNo, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = ProSurfaceElevated,
                                labelColor = ProTextSecondary,
                                selectedContainerColor = ProEmeraldContainer,
                                selectedLabelColor = ProEmeraldGlow
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = meterInput == meterNo,
                                borderColor = ProBorderHighlight,
                                selectedBorderColor = ProEmeraldGlow
                            )
                        )
                    }
                }
            } else {
                // Verified Meter Display Card with subtle gradient highlight
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
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Verified",
                                    tint = ProEmeraldGlow,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "NAWEC Verified Meter",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ProEmeraldGlow
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(EnergyAmber.copy(alpha = 0.2f))
                                    .border(1.dp, EnergyAmber.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = verifiedMeter.balanceKwh,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EnergyAmber
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = verifiedMeter.ownerName,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = ProTextPrimary
                            )
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Meter Number: ${verifiedMeter.meterNumber}",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = ProEmeraldGlow
                            )
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Area",
                                tint = ProTextSecondary,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = verifiedMeter.area,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = ProTextSecondary
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = "Tariff Plan: ${verifiedMeter.tariffCategory}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = ProTextMuted
                            )
                        )
                    }
                }
            }
        }
    }
}
