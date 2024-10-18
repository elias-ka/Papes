package app.papes.data.mappers

import app.papes.data.remote.PexelsPhoto
import app.papes.domain.Photo

fun PexelsPhoto.toPhoto() =
    Photo(
        id = id,
        url = url,
        photographer = photographer,
        photographerUrl = photographerUrl,
        thumbnail = src.medium,
        original = src.original,
        alt = alt
    )