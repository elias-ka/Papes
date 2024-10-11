package app.papes.ui.components

import androidx.annotation.StringRes
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PapesTopAppBar(
    @StringRes title: Int,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(id = title),
                fontWeight = FontWeight.Bold
            )
        },
    )
}