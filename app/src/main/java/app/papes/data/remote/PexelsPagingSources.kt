package app.papes.data.remote

import app.papes.data.mappers.toPhoto
import app.papes.domain.Photo

class CuratedPhotosPagingSource(
    private val api: PexelsApi
) : BasePagingSource<Photo>(
    onFetchPage = { page ->
        val response = api.getCuratedPhotos(page = page, perPage = 80)
        response.photos.map { it.toPhoto() }
    }
)
