package app.papes.presentation.info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.papes.R

@Composable
fun InfoRoute() {
    InfoScreen()
}

@Composable
fun InfoScreen(
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .padding(32.dp)
            .fillMaxSize()
    ) {
        AppDescriptionRow()
        Spacer(modifier = Modifier.height(16.dp))
        GitHubLinkRow()
        Spacer(modifier = Modifier.height(16.dp))
        PexelsCreditRow()
    }
}

@Composable
fun AppDescriptionRow(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
    ) {
        Text(stringResource(R.string.info_app_description))
    }
}

@Composable
fun GitHubLinkRow(
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current
    val url = stringResource(R.string.info_github_url)
    Row(
        modifier = modifier
    ) {
        FilledTonalButton(
            onClick = {
                uriHandler.openUri(url)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.info_github_title))
        }
    }
}


@Composable
fun PexelsCreditRow(
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current
    val url = stringResource(R.string.info_pexels_url)

    Row(
        modifier = modifier
    ) {
        FilledTonalButton(
            onClick = {
                uriHandler.openUri(url)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.info_pexels_credit_title))
        }
    }
}
