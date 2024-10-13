package app.papes.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import app.papes.data.remote.CuratedPhotosPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val curatedPhotosPagingSource: CuratedPhotosPagingSource
) : ViewModel() {

    val curatedPhotosFlow = Pager(PagingConfig(pageSize = 80)) {
        curatedPhotosPagingSource
    }.flow.cachedIn(viewModelScope)
}