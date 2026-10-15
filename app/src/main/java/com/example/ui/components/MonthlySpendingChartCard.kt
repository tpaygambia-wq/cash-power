package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.data.model.PurchaseReceipt
import com.example.ui.theme.BorderGlass
import com.example.ui.theme.DividerModern
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.ElectricMint
import com.example.ui.theme.ElectricMintContainer
import com.example.ui.theme.ElectricMintGlow
import com.example.ui.theme.ModernCardRimGradient
import com.example.ui.theme.ModernSurface
import com.example.ui.theme.ModernSurfaceElevated
import com.example.ui.theme.ModernSurfaceHighlight
import com.example.ui.theme.SunGold
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class MonthSpending(
    val monthName: String,
    val amount: Int,
    val unitsKwh: Double,
    val isCurrentMonth: Boolean = false
)

@Composable
fun MonthlySpendingChartCard(
    purchaseHistory: List<PurchaseReceipt>,
    modifier: Modifier = Modifier
) {
    // Generate/aggregate monthly spending data based on transactions
    val monthlyData = remember(purchaseHistory) {
        calculateMonthlySpending(purchaseHistory)
    }

    val maxAmount = remember(monthlyData) {
        monthlyData.maxOfOrNull { it.amount }?.coerceAtLeast(1000) ?: 3000
    }

    val avgMonthlySpend = remember(monthlyData) {
        if (monthlyData.isNotEmpty()) {
            monthlyData.map { it.amount }.average().toInt()
        } else 2150
    }

    val avgMonthlyUnits = remember(monthlyData) {
        if (monthlyData.isNotEmpty()) {
            monthlyData.map { it.unitsKwh }.average()
        } else 212.0
    }

    var selectedMonthIndex by remember { mutableStateOf(monthlyData.lastIndex.coerceAtLeast(0)) }
    val selectedItem = monthlyData.getOrNull(selectedMonthIndex) ?: monthlyData.last()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(10.dp, RoundedCornerShape(18.dp), spotColor = Color(0x3800E599))
            .testTag("monthly_spending_chart_card"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = ModernSurface
        ),
        border = BorderStroke(1.dp, ModernCardRimGradient)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(ElectricMintContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.BarChart,
                            contentDescription = null,
                            tint = ElectricMint,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Average Monthly Spending",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                        Text(
                            text = "NAWEC Electricity Consumption Trends",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TextMuted
                            )
                        )
                    }
                }

                // Trend badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(ElectricMintContainer)
                        .border(1.dp, BorderGlass, RoundedCornerShape(6.dp))
                        .padding(horizontal = 7.dp, vertical = 2.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.TrendingUp,
                            contentDescription = null,
                            tint = ElectricMint,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "Stable",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = ElectricMintGlow
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Big Stat & Selected Month Callout
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = "AVG SPEND",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted,
                        letterSpacing = 0.5.sp
                    )
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "$avgMonthlySpend",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Black,
                                color = TextPrimary
                            )
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "GMD / mo",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = ElectricMintGlow,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                }

                // Interactive Selected Month Inspector Box
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(ModernSurfaceElevated)
                        .border(1.dp, BorderGlass, RoundedCornerShape(10.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "${selectedItem.monthName}: ${selectedItem.amount} GMD",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = ElectricCyan
                        )
                        Text(
                            text = "~ ${selectedItem.unitsKwh.toInt()} kWh units",
                            fontSize = 10.sp,
                            color = TextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Modern Bar Chart Canvas Layout
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(ModernSurfaceElevated)
                    .border(1.dp, BorderGlass, RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 12.dp)
            ) {
                // Dashed Average Reference Line
                val avgFraction = (avgMonthlySpend.toFloat() / maxAmount.toFloat()).coerceIn(0.1f, 0.9f)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomStart)
                        .padding(bottom = (avgFraction * 80).dp)
                        .height(1.dp)
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    Color.Transparent,
                                    SunGold.copy(alpha = 0.45f),
                                    Color.Transparent
                                )
                            )
                        )
                )

                // The 6 Monthly Bars
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    monthlyData.forEachIndexed { index, data ->
                        val isSelected = index == selectedMonthIndex
                        val targetFraction = (data.amount.toFloat() / maxAmount.toFloat()).coerceIn(0.15f, 1f)
                        val animatedFraction by animateFloatAsState(
                            targetValue = targetFraction,
                            animationSpec = tween(durationMillis = 600),
                            label = "barHeight"
                        )

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { selectedMonthIndex = index }
                        ) {
                            // Value text over selected bar
                            if (isSelected) {
                                Text(
                                    text = "${data.amount}",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ElectricMintGlow
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                            } else {
                                Spacer(modifier = Modifier.height(14.dp))
                            }

                            // The Bar
                            Box(
                                modifier = Modifier
                                    .width(if (isSelected) 22.dp else 16.dp)
                                    .height((animatedFraction * 75).dp)
                                    .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                    .background(
                                        when {
                                            isSelected -> Brush.verticalGradient(
                                                listOf(ElectricCyan, ElectricMint)
                                            )
                                            data.isCurrentMonth -> Brush.verticalGradient(
                                                listOf(ElectricMint, Color(0xFF006B44))
                                            )
                                            else -> Brush.verticalGradient(
                                                listOf(Color(0xFF1E3747), Color(0xFF13222C))
                                            )
                                        }
                                    )
                                    .border(
                                        width = if (isSelected) 1.5.dp else 1.dp,
                                        color = if (isSelected) ElectricCyan else BorderGlass,
                                        shape = RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp)
                                    )
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            // Month Label
                            Text(
                                text = data.monthName,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) ElectricMintGlow else TextMuted
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = DividerModern)
            Spacer(modifier = Modifier.height(10.dp))

            // Footer Summary Metrics
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Est. Monthly Power: ",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                    Text(
                        text = "~ ${avgMonthlyUnits.toInt()} kWh",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ElectricMintGlow
                    )
                }

                Text(
                    text = "Tariff Rate: ~10.14 GMD/kWh",
                    fontSize = 11.sp,
                    color = TextSecondary
                )
            }
        }
    }
}

/**
 * Calculates a rolling 6-month spending chart based on existing transactions,
 * supplemented with realistic seasonal Gambian electricity averages so the chart
 * is never empty and immediately visualizes realistic spending trends.
 */
private fun calculateMonthlySpending(history: List<PurchaseReceipt>): List<MonthSpending> {
    val monthFormat = SimpleDateFormat("MMM", Locale.US)
    val calendar = Calendar.getInstance()

    // Build the last 6 months (e.g. May, Jun, Jul, Aug, Sep, Oct)
    val months = mutableListOf<MonthSpending>()
    val currentMonthIndex = 5

    // Base seasonal spending profile for The Gambia (Hot season July-Oct higher AC/fans)
    val seasonalBaseAmounts = listOf(1850, 2100, 2400, 2800, 2550, 2200)

    for (i in 5 downTo 0) {
        val cal = Calendar.getInstance()
        cal.add(Calendar.MONTH, -i)
        val monthLabel = monthFormat.format(cal.time)

        // Sum any real transaction matching this month
        val realSpendInMonth = history.filter { receipt ->
            val rCal = Calendar.getInstance().apply { timeInMillis = receipt.timestamp }
            rCal.get(Calendar.MONTH) == cal.get(Calendar.MONTH) &&
                    rCal.get(Calendar.YEAR) == cal.get(Calendar.YEAR)
        }.sumOf { it.totalCharged }

        val amount = if (realSpendInMonth > 0) {
            realSpendInMonth
        } else {
            seasonalBaseAmounts[5 - i]
        }

        val units = amount / 10.14

        months.add(
            MonthSpending(
                monthName = monthLabel,
                amount = amount,
                unitsKwh = Math.round(units * 10.0) / 10.0,
                isCurrentMonth = i == 0
            )
        )
    }

    return months
}
