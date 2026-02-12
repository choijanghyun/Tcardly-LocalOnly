package com.tcardly.feature.contacts.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.tcardly.feature.contacts.screen.ContactListScreen

const val CONTACTS_ROUTE = "contacts"

fun NavGraphBuilder.contactsScreen(
    onNavigateToCardDetail: (Long) -> Unit,
    onNavigateToCompany: (String) -> Unit,
    onNavigateToScan: () -> Unit,
    onNavigateToManualInput: () -> Unit
) {
    composable(CONTACTS_ROUTE) {
        ContactListScreen(
            onNavigateToCardDetail = onNavigateToCardDetail,
            onNavigateToCompany = onNavigateToCompany,
            onNavigateToScan = onNavigateToScan,
            onNavigateToManualInput = onNavigateToManualInput
        )
    }
}
