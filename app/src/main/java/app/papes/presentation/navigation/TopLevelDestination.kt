package app.papes.presentation.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.ui.graphics.vector.ImageVector
import app.papes.R
import app.papes.presentation.home.HomeRoute
import app.papes.presentation.info.InfoRoute
import kotlin.reflect.KClass

enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    @StringRes val label: Int,
    @StringRes val title: Int,
    val route: KClass<*>
) {
    HOME(
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        label = R.string.destination_home_label,
        title = R.string.app_name,
        route = HomeRoute::class
    ),
    INFO(
        selectedIcon = Icons.Filled.Info,
        unselectedIcon = Icons.Outlined.Info,
        label = R.string.destination_info_label,
        title = R.string.destination_info_title,
        route = InfoRoute::class
    )
}