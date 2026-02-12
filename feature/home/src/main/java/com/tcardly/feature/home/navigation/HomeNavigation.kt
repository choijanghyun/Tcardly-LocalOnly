package com.tcardly.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.tcardly.feature.home.screen.HomeScreen

const val HOME_ROUTE = "home"

fun NavGraphBuilder.homeScreen(
    onNavigateToProfile: () -> Unit,
    onNavigateToCardDetail: (Long) -> Unit,
    onNavigateToCompany: (String) -> Unit,
    onNavigateToScan: () -> Unit,
    onNavigateToPaywall: () -> Unit
) {
    composable(HOME_ROUTE) {
        HomeScreen(
            onNavigateToProfile = onNavigateToProfile,
            onNavigateToCardDetail = onNavigateToCardDetail,
            onNavigateToCompany = onNavigateToCompany,
            onNavigateToScan = onNavigateToScan,
            onNavigateToPaywall = onNavigateToPaywall
        )
    }
}
