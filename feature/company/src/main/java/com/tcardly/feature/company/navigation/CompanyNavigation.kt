package com.tcardly.feature.company.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.tcardly.feature.company.screen.AiAnalysisScreen
import com.tcardly.feature.company.screen.AiChatScreen
import com.tcardly.feature.company.screen.CompanyDetailScreen
import java.net.URLDecoder
import java.net.URLEncoder

const val COMPANY_DETAIL_ROUTE = "company/{companyName}"
const val AI_ANALYSIS_ROUTE = "ai-analysis/{companyName}"
const val AI_CHAT_ROUTE = "ai-chat/{companyName}"

fun NavController.navigateToCompanyDetail(companyName: String) {
    navigate("company/${URLEncoder.encode(companyName, "UTF-8")}")
}

fun NavController.navigateToAiAnalysis(companyName: String) {
    navigate("ai-analysis/${URLEncoder.encode(companyName, "UTF-8")}")
}

fun NavController.navigateToAiChat(companyName: String) {
    navigate("ai-chat/${URLEncoder.encode(companyName, "UTF-8")}")
}

fun NavGraphBuilder.companyDetailScreen(
    onNavigateBack: () -> Unit,
    onNavigateToAiAnalysis: (String) -> Unit,
    onNavigateToAiChat: (String) -> Unit
) {
    composable(
        route = COMPANY_DETAIL_ROUTE,
        arguments = listOf(navArgument("companyName") { type = NavType.StringType })
    ) { entry ->
        val companyName = entry.arguments?.getString("companyName")?.let {
            URLDecoder.decode(it, "UTF-8")
        } ?: ""
        CompanyDetailScreen(
            companyName = companyName,
            onNavigateBack = onNavigateBack,
            onNavigateToAiAnalysis = onNavigateToAiAnalysis,
            onNavigateToAiChat = onNavigateToAiChat
        )
    }
}

fun NavGraphBuilder.aiAnalysisScreen(
    onNavigateBack: () -> Unit,
    onNavigateToAiChat: (String) -> Unit,
    onNavigateToPaywall: () -> Unit
) {
    composable(
        route = AI_ANALYSIS_ROUTE,
        arguments = listOf(navArgument("companyName") { type = NavType.StringType })
    ) { entry ->
        val companyName = entry.arguments?.getString("companyName")?.let {
            URLDecoder.decode(it, "UTF-8")
        } ?: ""
        AiAnalysisScreen(
            companyName = companyName,
            onNavigateBack = onNavigateBack,
            onNavigateToAiChat = onNavigateToAiChat,
            onNavigateToPaywall = onNavigateToPaywall
        )
    }
}

fun NavGraphBuilder.aiChatScreen(
    onNavigateBack: () -> Unit
) {
    composable(
        route = AI_CHAT_ROUTE,
        arguments = listOf(navArgument("companyName") { type = NavType.StringType })
    ) { entry ->
        val companyName = entry.arguments?.getString("companyName")?.let {
            URLDecoder.decode(it, "UTF-8")
        } ?: ""
        AiChatScreen(
            companyName = companyName,
            onNavigateBack = onNavigateBack
        )
    }
}
