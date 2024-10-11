package app.papes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import app.papes.ui.PapesApp
import app.papes.ui.rememberPapesAppState
import app.papes.ui.theme.PapesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val appState = rememberPapesAppState()
            PapesTheme {
                PapesApp(
                    appState = appState
                )
            }
        }
    }
}
