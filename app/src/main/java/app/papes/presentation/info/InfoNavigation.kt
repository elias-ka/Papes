package app.papes.presentation.info

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object InfoRoute

fun NavController.navigateToInfo(
    navOptions: NavOptions
) = navigate(route = InfoRoute, navOptions)

fun NavGraphBuilder.infoScreen(
) {
    composable<InfoRoute> {
        InfoRoute()
    }
}