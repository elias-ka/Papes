package app.papes.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import app.papes.presentation.home.navigateToHome
import app.papes.presentation.info.navigateToInfo
import app.papes.presentation.navigation.TopLevelDestination

@Composable
fun rememberPapesAppState(
    navController: NavHostController = rememberNavController()
): PapesAppState {
    return remember(navController) {
        PapesAppState(navController = navController)
    }
}

@Stable
class PapesAppState(
    val navController: NavHostController
) {
    val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    val topLevelDestinations = TopLevelDestination.entries

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() {
            return topLevelDestinations.firstOrNull {
                currentDestination?.hasRoute(route = it.route) == true
            }
        }


    fun navigateToTopLevelDestination(destination: TopLevelDestination) {
        val topLevelNavOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
        when (destination) {
            TopLevelDestination.HOME -> navController.navigateToHome(topLevelNavOptions)
            TopLevelDestination.INFO -> navController.navigateToInfo(topLevelNavOptions)
        }
    }
}