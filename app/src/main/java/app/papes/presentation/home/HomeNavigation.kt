package app.papes.presentation.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import app.papes.domain.Photo
import kotlinx.serialization.Serializable

@Serializable
object HomeRoute

fun NavController.navigateToHome(
    navOptions: NavOptions
) = navigate(route = HomeRoute, navOptions)

fun NavGraphBuilder.homeScreen(
    onPhotoClick: (Photo) -> Unit
) {
    composable<HomeRoute> {
        HomeScreen(onPhotoClick)
    }
}