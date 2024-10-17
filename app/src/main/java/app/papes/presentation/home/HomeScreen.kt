package app.papes.presentation.home

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import app.papes.domain.Photo
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.flow.Flow

@Composable
fun HomeScreen(
    onPhotoClick: (Photo) -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    HomeScreenContent(homeViewModel.curatedPhotosFlow, onPhotoClick,  modifier)
}

@Composable
fun HomeScreenContent(
    photosFlow: Flow<PagingData<Photo>>,
    onPhotoClick: (Photo) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val photos = photosFlow.collectAsLazyPagingItems()
    val context = LocalContext.current
    LaunchedEffect(key1 = photos.loadState) {
        if (photos.loadState.refresh is LoadState.Error) {
            Toast.makeText(
                context,
                String.format(
                    "Error: %s",
                    (photos.loadState.refresh as LoadState.Error).error.message
                ),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    val state = rememberLazyStaggeredGridState()
    Box(modifier = modifier) {
        if (photos.loadState.refresh is LoadState.Loading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        } else {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                verticalItemSpacing = 4.dp,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                state = state,
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    count = photos.itemCount,
                    key = photos.itemKey { it.id },
                ) {
                    val photo = photos[it] ?: return@items
                    PhotoItem(photo = photo, onClick = onPhotoClick)
                }
            }
        }
    }
}

@Composable
fun PhotoItem(
    photo: Photo,
    onClick: (Photo) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(onClick = { onClick(photo) }, modifier = modifier) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(photo.thumbnail)
                .crossfade(true)
                .build(),
            contentScale = ContentScale.FillWidth,
            contentDescription = photo.alt,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )
    }
}