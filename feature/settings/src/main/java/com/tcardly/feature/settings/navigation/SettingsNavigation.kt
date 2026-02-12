package com.tcardly.feature.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.tcardly.feature.settings.screen.GroupTagManageScreen
import com.tcardly.feature.settings.screen.MenuScreen
import com.tcardly.feature.settings.screen.SettingsScreen

const val MENU_ROUTE = "menu"
const val GROUP_TAG_MANAGE_ROUTE = "group-tag-manage"
const val SETTINGS_ROUTE = "settings"

fun NavController.navigateToGroupTagManage() { navigate(GROUP_TAG_MANAGE_ROUTE) }
fun NavController.navigateToSettings() { navigate(SETTINGS_ROUTE) }

fun NavGraphBuilder.menuScreen(
    onNavigateToProfile: () -> Unit,
    onNavigateToSubscriptionManage: () -> Unit,
    onNavigateToGroupTagManage: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToPaywall: () -> Unit
) {
    composable(MENU_ROUTE) {
        MenuScreen(
            onNavigateToProfile = onNavigateToProfile,
            onNavigateToSubscriptionManage = onNavigateToSubscriptionManage,
            onNavigateToGroupTagManage = onNavigateToGroupTagManage,
            onNavigateToSettings = onNavigateToSettings,
            onNavigateToPaywall = onNavigateToPaywall
        )
    }
}

fun NavGraphBuilder.groupTagManageScreen(onNavigateBack: () -> Unit) {
    composable(GROUP_TAG_MANAGE_ROUTE) {
        GroupTagManageScreen(onNavigateBack = onNavigateBack)
    }
}

fun NavGraphBuilder.settingsScreen(onNavigateBack: () -> Unit) {
    composable(SETTINGS_ROUTE) {
        SettingsScreen(onNavigateBack = onNavigateBack)
    }
}
