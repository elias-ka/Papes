package app.papes.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import app.papes.presentation.navigation.PapesNavHost
import app.papes.presentation.ui.components.PapesBottomBar
import app.papes.presentation.ui.components.PapesTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PapesApp(
    appState: PapesAppState
) {
    val scrollBehavior = BottomAppBarDefaults.exitAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
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
                currentDestination = appState.currentDestination,
                scrollBehavior = scrollBehavior
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