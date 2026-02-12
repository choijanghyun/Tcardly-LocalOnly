package com.tcardly.feature.subscription.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.tcardly.feature.subscription.screen.PaywallScreen
import com.tcardly.feature.subscription.screen.SubscriptionManageScreen

const val PAYWALL_ROUTE = "paywall"
const val SUBSCRIPTION_MANAGE_ROUTE = "subscription-manage"

fun NavController.navigateToPaywall(source: String? = null) {
    navigate(PAYWALL_ROUTE)
}

fun NavController.navigateToSubscriptionManage() {
    navigate(SUBSCRIPTION_MANAGE_ROUTE)
}

fun NavGraphBuilder.paywallScreen(
    onNavigateBack: () -> Unit,
    onSubscribed: () -> Unit
) {
    composable(PAYWALL_ROUTE) {
        PaywallScreen(onNavigateBack = onNavigateBack, onSubscribed = onSubscribed)
    }
}

fun NavGraphBuilder.subscriptionManageScreen(
    onNavigateBack: () -> Unit,
    onNavigateToPaywall: () -> Unit
) {
    composable(SUBSCRIPTION_MANAGE_ROUTE) {
        SubscriptionManageScreen(onNavigateBack = onNavigateBack, onNavigateToPaywall = onNavigateToPaywall)
    }
}
