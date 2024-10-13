package app.papes.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import app.papes.presentation.home.homeNavigationRoute
import app.papes.presentation.home.homeScreen

@Composable
fun PapesNavHost(
    navController: NavHostController,
    startDestination: String = homeNavigationRoute,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        homeScreen()
    }
}