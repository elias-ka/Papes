package app.papes.data.remote

import app.papes.data.mappers.toPhoto
import app.papes.domain.Photo

class CuratedPhotosPagingSource(
    private val api: PexelsApi,
    private val seenIds: MutableSet<Int>
) : BasePagingSource<Photo>(
    onFetchPage = { page ->
        val response = api.getCuratedPhotos(page = page)
        response.photos.fold(mutableListOf<Photo>()) { acc, photo ->
            if (photo.id !in seenIds) {
                seenIds.add(photo.id)
                acc.add(photo.toPhoto())
            }
            acc
        }
    }
)
