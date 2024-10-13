package app.papes.presentation.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

// FIXME: consider using type-safe navigation
const val homeNavigationRoute = "home_route"

fun NavController.navigateToHome(
    navOptions: NavOptions
) = navigate(route = homeNavigationRoute, navOptions)

fun NavGraphBuilder.homeScreen() {
    composable(route = homeNavigationRoute) {
        HomeScreen()
    }
}