package app.papes.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.papes.presentation.navigation.PapesNavHost
import app.papes.presentation.ui.components.PapesBottomBar
import app.papes.presentation.ui.components.PapesTopAppBar

@Composable
fun PapesApp(
    appState: PapesAppState
) {
    Scaffold(
        topBar = {
            val destination = appState.currentTopLevelDestination
            if (destination != null) {
                PapesTopAppBar(
                    title = destination.title,
                )
            }
        },
        bottomBar = {
            PapesBottomBar(
                destinations = appState.topLevelDestinations,
                onNavigateToDestination = appState::navigateToTopLevelDestination,
                currentDestination = appState.currentDestination
            )
        }
    ) { innerPadding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(Modifier.fillMaxSize()) {
                PapesNavHost(
                    navController = appState.navController,
                )
            }
        }
    }
}