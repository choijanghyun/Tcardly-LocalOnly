package com.tcardly.app.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.tcardly.feature.company.navigation.aiAnalysisScreen
import com.tcardly.feature.company.navigation.aiChatScreen
import com.tcardly.feature.company.navigation.companyDetailScreen
import com.tcardly.feature.company.navigation.navigateToAiAnalysis
import com.tcardly.feature.company.navigation.navigateToAiChat
import com.tcardly.feature.company.navigation.navigateToCompanyDetail
import com.tcardly.feature.contacts.navigation.CONTACTS_ROUTE
import com.tcardly.feature.contacts.navigation.contactsScreen
import com.tcardly.feature.home.navigation.HOME_ROUTE
import com.tcardly.feature.home.navigation.homeScreen
import com.tcardly.feature.profile.navigation.initialProfileScreen
import com.tcardly.feature.profile.navigation.navigateToInitialProfile
import com.tcardly.feature.profile.navigation.navigateToProfile
import com.tcardly.feature.profile.navigation.profileScreen
import com.tcardly.feature.scan.navigation.manualInputScreen
import com.tcardly.feature.scan.navigation.navigateToManualInput
import com.tcardly.feature.scan.navigation.navigateToScanResult
import com.tcardly.feature.scan.navigation.scanResultScreen
import com.tcardly.feature.scan.navigation.scanScreen
import com.tcardly.feature.settings.navigation.menuScreen
import com.tcardly.feature.settings.navigation.navigateToGroupTagManage
import com.tcardly.feature.settings.navigation.groupTagManageScreen
import com.tcardly.feature.settings.navigation.navigateToSettings
import com.tcardly.feature.settings.navigation.settingsScreen
import com.tcardly.feature.subscription.navigation.navigateToPaywall
import com.tcardly.feature.subscription.navigation.navigateToSubscriptionManage
import com.tcardly.feature.subscription.navigation.paywallScreen
import com.tcardly.feature.subscription.navigation.subscriptionManageScreen

@Composable
fun MainNavGraph(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
    NavHost(
        navController = navController,
        startDestination = HOME_ROUTE
    ) {
        // ── Bottom Navigation Tabs ──

        homeScreen(
            onNavigateToProfile = { navController.navigateToProfile() },
            onNavigateToCardDetail = { cardId ->
                navController.navigate("card-detail/$cardId")
            },
            onNavigateToCompany = { companyName ->
                navController.navigateToCompanyDetail(companyName)
            },
            onNavigateToScan = { navController.navigate("scan") },
            onNavigateToPaywall = { navController.navigateToPaywall() }
        )

        scanScreen(
            onNavigateToScanResult = { ocrJson ->
                navController.navigateToScanResult(ocrJson)
            },
            onNavigateToManualInput = { navController.navigateToManualInput() },
            onNavigateBack = { navController.popBackStack() }
        )

        contactsScreen(
            onNavigateToCardDetail = { cardId ->
                navController.navigate("card-detail/$cardId")
            },
            onNavigateToCompany = { companyName ->
                navController.navigateToCompanyDetail(companyName)
            },
            onNavigateToScan = { navController.navigate("scan") },
            onNavigateToManualInput = { navController.navigateToManualInput() }
        )

        menuScreen(
            onNavigateToProfile = { navController.navigateToProfile() },
            onNavigateToSubscriptionManage = { navController.navigateToSubscriptionManage() },
            onNavigateToGroupTagManage = { navController.navigateToGroupTagManage() },
            onNavigateToSettings = { navController.navigateToSettings() },
            onNavigateToPaywall = { navController.navigateToPaywall() }
        )

        // ── Scan Flow ──

        scanResultScreen(
            onNavigateToHome = {
                navController.navigate(HOME_ROUTE) {
                    popUpTo(HOME_ROUTE) { inclusive = true }
                }
            },
            onNavigateBack = { navController.popBackStack() },
            onNavigateToCompany = { companyName ->
                navController.navigateToCompanyDetail(companyName)
            }
        )

        manualInputScreen(
            onNavigateToHome = {
                navController.navigate(HOME_ROUTE) {
                    popUpTo(HOME_ROUTE) { inclusive = true }
                }
            },
            onNavigateBack = { navController.popBackStack() }
        )

        // ── Card Detail ──

        composable(
            route = "card-detail/{cardId}",
            arguments = listOf(navArgument("cardId") { type = NavType.LongType })
        ) {
            // CardDetailScreen은 contacts 모듈에 포함
            com.tcardly.feature.contacts.screen.CardDetailScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToCompany = { companyName ->
                    navController.navigateToCompanyDetail(companyName)
                },
                onNavigateToAiAnalysis = { companyName ->
                    navController.navigateToAiAnalysis(companyName)
                }
            )
        }

        // ── Profile ──

        profileScreen(
            onNavigateBack = { navController.popBackStack() }
        )

        initialProfileScreen(
            onNavigateToHome = {
                navController.navigate(HOME_ROUTE) {
                    popUpTo(0) { inclusive = true }
                }
            }
        )

        // ── Company / AI ──

        companyDetailScreen(
            onNavigateBack = { navController.popBackStack() },
            onNavigateToAiAnalysis = { companyName ->
                navController.navigateToAiAnalysis(companyName)
            },
            onNavigateToAiChat = { companyName ->
                navController.navigateToAiChat(companyName)
            }
        )

        aiAnalysisScreen(
            onNavigateBack = { navController.popBackStack() },
            onNavigateToAiChat = { companyName ->
                navController.navigateToAiChat(companyName)
            },
            onNavigateToPaywall = { navController.navigateToPaywall() }
        )

        aiChatScreen(
            onNavigateBack = { navController.popBackStack() }
        )

        // ── Subscription ──

        paywallScreen(
            onNavigateBack = { navController.popBackStack() },
            onSubscribed = { navController.popBackStack() }
        )

        subscriptionManageScreen(
            onNavigateBack = { navController.popBackStack() },
            onNavigateToPaywall = { navController.navigateToPaywall() }
        )

        // ── Settings ──

        groupTagManageScreen(
            onNavigateBack = { navController.popBackStack() }
        )

        settingsScreen(
            onNavigateBack = { navController.popBackStack() }
        )
    }
}
