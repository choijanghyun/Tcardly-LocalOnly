package com.tcardly.feature.profile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.tcardly.feature.profile.screen.InitialProfileScreen
import com.tcardly.feature.profile.screen.ProfileScreen

const val PROFILE_ROUTE = "profile"
const val INITIAL_PROFILE_ROUTE = "initial-profile"

fun NavController.navigateToProfile() { navigate(PROFILE_ROUTE) }
fun NavController.navigateToInitialProfile() { navigate(INITIAL_PROFILE_ROUTE) }

fun NavGraphBuilder.profileScreen(onNavigateBack: () -> Unit) {
    composable(PROFILE_ROUTE) {
        ProfileScreen(onNavigateBack = onNavigateBack)
    }
}

fun NavGraphBuilder.initialProfileScreen(onNavigateToHome: () -> Unit) {
    composable(INITIAL_PROFILE_ROUTE) {
        InitialProfileScreen(onNavigateToHome = onNavigateToHome)
    }
}
