package app.papes.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import app.papes.data.remote.CuratedPhotosPagingSource
import app.papes.data.remote.PexelsApi
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val pexelsApi: PexelsApi
) : ViewModel() {
    // Stores the IDs of the photos that have been loaded so far.
    // Prevents the same photo from being loaded multiple times thus
    // avoiding duplicate keys in the UI photo list.
    private val seenIds = mutableSetOf<Int>()


    val curatedPhotosFlow = Pager(PagingConfig(pageSize = 40, prefetchDistance = 160)) {
        CuratedPhotosPagingSource(pexelsApi, seenIds)
    }.flow.cachedIn(viewModelScope)
}