package com.tcardly.feature.scan.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.tcardly.feature.scan.screen.ManualInputScreen
import com.tcardly.feature.scan.screen.ScanResultScreen
import com.tcardly.feature.scan.screen.ScanScreen
import java.net.URLDecoder
import java.net.URLEncoder

const val SCAN_ROUTE = "scan"
const val SCAN_RESULT_ROUTE = "scan-result/{ocrJson}"
const val MANUAL_INPUT_ROUTE = "manual-input"

fun NavController.navigateToScanResult(ocrJson: String) {
    val encoded = URLEncoder.encode(ocrJson, "UTF-8")
    navigate("scan-result/$encoded")
}

fun NavController.navigateToManualInput() {
    navigate(MANUAL_INPUT_ROUTE)
}

fun NavGraphBuilder.scanScreen(
    onNavigateToScanResult: (String) -> Unit,
    onNavigateToManualInput: () -> Unit,
    onNavigateBack: () -> Unit
) {
    composable(SCAN_ROUTE) {
        ScanScreen(
            onNavigateToScanResult = onNavigateToScanResult,
            onNavigateToManualInput = onNavigateToManualInput,
            onNavigateBack = onNavigateBack
        )
    }
}

fun NavGraphBuilder.scanResultScreen(
    onNavigateToHome: () -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToCompany: (String) -> Unit
) {
    composable(
        route = SCAN_RESULT_ROUTE,
        arguments = listOf(navArgument("ocrJson") { type = NavType.StringType })
    ) { backStackEntry ->
        val ocrJson = backStackEntry.arguments?.getString("ocrJson")?.let {
            URLDecoder.decode(it, "UTF-8")
        } ?: ""
        ScanResultScreen(
            ocrJson = ocrJson,
            onNavigateToHome = onNavigateToHome,
            onNavigateBack = onNavigateBack,
            onNavigateToCompany = onNavigateToCompany
        )
    }
}

fun NavGraphBuilder.manualInputScreen(
    onNavigateToHome: () -> Unit,
    onNavigateBack: () -> Unit
) {
    composable(MANUAL_INPUT_ROUTE) {
        ManualInputScreen(
            onNavigateToHome = onNavigateToHome,
            onNavigateBack = onNavigateBack
        )
    }
}
