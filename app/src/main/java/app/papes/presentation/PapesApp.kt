package app.papes.presentation

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import app.papes.presentation.navigation.PapesNavHost
import app.papes.presentation.ui.components.PapesBottomBar
import app.papes.presentation.ui.components.PapesTopAppBar

@SuppressLint("RestrictedApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PapesApp(
    appState: PapesAppState
) {
    val scrollBehavior = BottomAppBarDefaults.exitAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            val topLevelDestination = appState.currentTopLevelDestination
            PapesTopAppBar(
                title =
                if (topLevelDestination != null) {
                    stringResource(topLevelDestination.title)
                } else {
                    ""
                },
                navigationIcon = {
                    if (appState.navController.previousBackStackEntry != null
                    ) {
                        IconButton(
                            onClick = { appState.navController.popBackStack() }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = null
                            )
                        }
                    } else {
                        null
                    }
                }
            )
        },
        bottomBar = {
            AnimatedVisibility(
                visible = appState.currentTopLevelDestination != null,
                enter = slideInVertically(
                    initialOffsetY = { it },
                ),
                exit = slideOutVertically(
                    targetOffsetY = { it },
                )
            ) {
                PapesBottomBar(
                    destinations = appState.topLevelDestinations,
                    onNavigateToDestination = appState::navigateToTopLevelDestination,
                    currentDestination = appState.currentDestination,
                    scrollBehavior = scrollBehavior
                )
            }
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