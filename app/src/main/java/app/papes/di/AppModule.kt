package app.papes.di

import app.papes.BuildConfig
import app.papes.data.remote.CuratedPhotosPagingSource
import app.papes.data.remote.PexelsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    @IntoSet
    fun provideAuthInterceptor(): Interceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()

        if (chain.request().url.host == PexelsApi.BASE_URL.toHttpUrlOrNull()?.host
            && chain.request().header("Authorization") == null
        ) {
            request.addHeader("Authorization", BuildConfig.PEXELS_API_KEY)
        }
        chain.proceed(request.build())
    }

    @Provides
    @Singleton
    @IntoSet
    fun provideHttpLoggingInterceptor(): Interceptor = HttpLoggingInterceptor().apply {
        if (BuildConfig.DEBUG) level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    @Singleton
    fun provideJsonParser(): Json {
        return Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        interceptors: Set<@JvmSuppressWildcards Interceptor>
    ): OkHttpClient = OkHttpClient.Builder().apply {
        interceptors.forEach(::addInterceptor)
        callTimeout(timeout = 60, unit = TimeUnit.SECONDS)
    }.build()

    @Provides
    @Singleton
    fun providePexelsApi(httpClient: OkHttpClient, jsonParser: Json): PexelsApi {
        return Retrofit.Builder().apply {
            baseUrl(PexelsApi.BASE_URL)
                .addConverterFactory(jsonParser.asConverterFactory("application/json".toMediaType()))
            client(httpClient)
        }.build().create<PexelsApi>(PexelsApi::class.java)
    }
}
