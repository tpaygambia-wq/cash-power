package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.CashPowerUiState
import com.example.ui.CashPowerViewModel
import com.example.ui.components.AmountCard
import com.example.ui.components.MeterCard
import com.example.ui.components.PaymentMethodSelector
import com.example.ui.dialogs.SaveMeterDialog
import com.example.ui.dialogs.TokenReceiptDialog
import com.example.ui.theme.EnergyAmber
import com.example.ui.theme.GambiaGreenPrimary

@Composable
fun HomeScreen(
    uiState: CashPowerUiState,
    viewModel: CashPowerViewModel
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Pro Live Grid Telemetry & Trust Banner
            ProGridTelemetryBanner()

            // SECTION 1: Meter Configuration Card
            MeterCard(
                meterInput = uiState.meterInput,
                verifiedMeter = uiState.verifiedMeter,
                isVerifying = uiState.isVerifying,
                quickDemoMeters = uiState.quickDemoMeters,
                onMeterChanged = { viewModel.onMeterInputChanged(it) },
                onVerifyClick = { viewModel.verifyCurrentMeter() },
                onClearClick = { viewModel.clearActiveMeter() },
                onSaveFavoriteClick = { viewModel.openSaveMeterDialog() }
            )

            // PRO SECTION: Energy Analytics (Visible when meter verified)
            if (uiState.verifiedMeter != null) {
                com.example.ui.components.ProEnergyAnalyticsCard(verifiedMeter = uiState.verifiedMeter)
            }

            // SECTION 2: Purchase Amount & Live Fee Calculation Block
            AmountCard(
                amountInput = uiState.amountInput,
                calculatedFee = uiState.calculatedFee,
                totalCost = uiState.totalCost,
                estimatedUnits = uiState.estimatedUnits,
                onAmountChanged = { viewModel.onAmountInputChanged(it) },
                onSelectPreset = { viewModel.selectPresetAmount(it) }
            )

            // SECTION 3: Payment Provider & Phone
            PaymentMethodSelector(
                selectedProvider = uiState.selectedProvider,
                phoneNumber = uiState.phoneNumber,
                onSelectProvider = { viewModel.selectProvider(it) },
                onPhoneChanged = { viewModel.onPhoneInputChanged(it) }
            )

            // Monthly Spending Visualization Card
            com.example.ui.components.MonthlySpendingChartCard(purchaseHistory = uiState.purchaseHistory)

            // Primary Execution Button with Modern Fintech Gradient & Glow
            val canPurchase = uiState.verifiedMeter != null &&
                    (uiState.amountInput.toIntOrNull() ?: 0) >= 500 &&
                    !uiState.isPurchasing

            val buttonGradient = if (canPurchase) com.example.ui.theme.ModernPrimaryButtonGradient else com.example.ui.theme.ModernPrimaryButtonDisabledGradient

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(if (canPurchase) 12.dp else 0.dp, RoundedCornerShape(16.dp), spotColor = com.example.ui.theme.ElectricMint)
                    .clip(RoundedCornerShape(16.dp))
                    .background(buttonGradient)
                    .clickable(enabled = canPurchase) { viewModel.processPurchase() }
                    .testTag("purchase_token_button"),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.FlashOn,
                        contentDescription = null,
                        tint = if (canPurchase) Color(0xFF04190F) else com.example.ui.theme.TextMuted,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (uiState.verifiedMeter == null) "Verify Meter to Continue"
                        else "Pay ${uiState.totalCost} GMD • Generate STS Token",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = if (canPurchase) Color(0xFF04190F) else com.example.ui.theme.TextMuted,
                        letterSpacing = 0.3.sp
                    )
                }
            }

            // Bottom Security Note
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.VerifiedUser,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Official STS 20-Digit Standard • NAWEC Gambia Approved",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }

        // Fullscreen Loading Spinner Overlay with Transaction Stages
        AnimatedVisibility(
            visible = uiState.isPurchasing,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.7f))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {},
                contentAlignment = Alignment.Center
            ) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .padding(24.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            color = GambiaGreenPrimary,
                            strokeWidth = 4.dp,
                            modifier = Modifier.size(54.dp)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = "Processing Cash Power",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = uiState.purchaseProgressText,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Please check your phone if an authorization prompt appears.",
                            fontSize = 11.sp,
                            color = EnergyAmber,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }

    // Success Receipt Dialog
    uiState.activeReceipt?.let { receipt ->
        TokenReceiptDialog(
            receipt = receipt,
            onDismiss = { viewModel.dismissReceiptDialog() },
            onShowMessage = { viewModel.showSnackbar(it) }
        )
    }

    // Save Meter Dialog
    if (uiState.showSaveMeterDialog && uiState.verifiedMeter != null) {
        SaveMeterDialog(
            meter = uiState.verifiedMeter,
            onDismiss = { viewModel.dismissSaveMeterDialog() },
            onConfirmSave = { nickname -> viewModel.saveCurrentMeter(nickname) }
        )
    }
}

@Composable
private fun ProGridTelemetryBanner() {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = com.example.ui.theme.ModernSurfaceElevated
        ),
        border = BorderStroke(1.dp, com.example.ui.theme.BorderGlass)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Pulsing Electric Mint Live Radar indicator
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(com.example.ui.theme.ElectricMint)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "NAWEC National Grid Online",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = com.example.ui.theme.TextPrimary
                            )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "• 50Hz Stable",
                            fontSize = 10.sp,
                            color = com.example.ui.theme.ElectricMint,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "Greater Banjul, West Coast & Brikama Substation Interconnected",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = com.example.ui.theme.TextMuted,
                            fontSize = 10.sp
                        )
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(com.example.ui.theme.ModernSurfaceHighlight)
                    .border(1.dp, com.example.ui.theme.BorderGlass, RoundedCornerShape(6.dp))
                    .padding(horizontal = 7.dp, vertical = 3.dp)
            ) {
                Text(
                    text = "PRO KMS",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Black,
                    color = com.example.ui.theme.ElectricCyan
                )
            }
        }
    }
}
