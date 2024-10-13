package app.papes.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface PexelsApi {

    @GET("curated")
    suspend fun getCuratedPhotos(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 80
    ): CuratedPhotosResponse

    companion object {
        const val BASE_URL = "https://api.pexels.com/v1/"
    }
}