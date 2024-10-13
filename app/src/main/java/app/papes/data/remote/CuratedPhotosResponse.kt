package app.papes.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CuratedPhotosResponse(
    val photos: List<PexelsPhoto>,
    val page: Int,
    @SerialName("per_page") val perPage: Int,
    @SerialName("total_results") val totalResults: Int,
)