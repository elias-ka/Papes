package app.papes.domain

data class Photo(
    val id: Int,
    val url: String,
    val photographer: String,
    val photographerUrl: String,
    val thumbnail: String,
    val alt: String
)