package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.ElectricBolt
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.History
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.AppTab
import com.example.ui.theme.ProBorderHighlight
import com.example.ui.theme.ProEmeraldContainer
import com.example.ui.theme.ProEmeraldGlow
import com.example.ui.theme.ProEmeraldPrimary
import com.example.ui.theme.ProSurfaceDark
import com.example.ui.theme.ProTextMuted

@Composable
fun BottomNavBar(
    currentTab: AppTab,
    onTabSelected: (AppTab) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(ProSurfaceDark)
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        // Subtle top emerald edge highlight
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color.Transparent,
                            ProBorderHighlight,
                            Color.Transparent
                        )
                    )
                )
        )

        NavigationBar(
            modifier = Modifier.testTag("bottom_nav_bar"),
            containerColor = Color.Transparent
        ) {
            NavigationBarItem(
                selected = currentTab == AppTab.BUY,
                onClick = { onTabSelected(AppTab.BUY) },
                icon = {
                    Icon(
                        imageVector = if (currentTab == AppTab.BUY) Icons.Filled.ElectricBolt else Icons.Outlined.ElectricBolt,
                        contentDescription = "Buy Cash Power"
                    )
                },
                label = { Text("Buy Token") },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    selectedTextColor = ProEmeraldGlow,
                    unselectedIconColor = ProTextMuted,
                    unselectedTextColor = ProTextMuted,
                    indicatorColor = ProEmeraldContainer
                ),
                modifier = Modifier.testTag("nav_tab_buy")
            )

            NavigationBarItem(
                selected = currentTab == AppTab.SAVED_METERS,
                onClick = { onTabSelected(AppTab.SAVED_METERS) },
                icon = {
                    Icon(
                        imageVector = if (currentTab == AppTab.SAVED_METERS) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                        contentDescription = "Saved Meters"
                    )
                },
                label = { Text("Meters") },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    selectedTextColor = ProEmeraldGlow,
                    unselectedIconColor = ProTextMuted,
                    unselectedTextColor = ProTextMuted,
                    indicatorColor = ProEmeraldContainer
                ),
                modifier = Modifier.testTag("nav_tab_meters")
            )

            NavigationBarItem(
                selected = currentTab == AppTab.HISTORY,
                onClick = { onTabSelected(AppTab.HISTORY) },
                icon = {
                    Icon(
                        imageVector = if (currentTab == AppTab.HISTORY) Icons.Filled.History else Icons.Outlined.History,
                        contentDescription = "Purchase History"
                    )
                },
                label = { Text("History") },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    selectedTextColor = ProEmeraldGlow,
                    unselectedIconColor = ProTextMuted,
                    unselectedTextColor = ProTextMuted,
                    indicatorColor = ProEmeraldContainer
                ),
                modifier = Modifier.testTag("nav_tab_history")
            )

            NavigationBarItem(
                selected = currentTab == AppTab.GUIDE,
                onClick = { onTabSelected(AppTab.GUIDE) },
                icon = {
                    Icon(
                        imageVector = if (currentTab == AppTab.GUIDE) Icons.Filled.HelpOutline else Icons.Outlined.HelpOutline,
                        contentDescription = "Keypad Guide"
                    )
                },
                label = { Text("CIU Guide") },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    selectedTextColor = ProEmeraldGlow,
                    unselectedIconColor = ProTextMuted,
                    unselectedTextColor = ProTextMuted,
                    indicatorColor = ProEmeraldContainer
                ),
                modifier = Modifier.testTag("nav_tab_guide")
            )
        }
    }
}
