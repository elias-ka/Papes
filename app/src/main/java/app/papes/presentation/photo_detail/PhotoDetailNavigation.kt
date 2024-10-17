package app.papes.presentation.photo_detail

import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import app.papes.domain.Photo
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.reflect.typeOf

@Serializable
data class PhotoDetailRoute(val photo: Photo)

fun NavController.navigateToPhotoDetail(
    photo: Photo,
    navOptions: NavOptions? = null
) = navigate(route = PhotoDetailRoute(photo), navOptions)

fun NavGraphBuilder.photoDetailScreen() {
    composable<PhotoDetailRoute>(
        typeMap = mapOf(typeOf<Photo>() to PhotoNavType)
    ) { backStackEntry ->
        val params = backStackEntry.toRoute<PhotoDetailRoute>()
        PhotoDetailScreen(
            photo = params.photo,
        )
    }
}

val PhotoNavType = object : NavType<Photo>(
    isNullableAllowed = false
) {
    override fun put(bundle: Bundle, key: String, value: Photo) {
        bundle.putParcelable(key, value)
    }

    override fun get(bundle: Bundle, key: String): Photo {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, Photo::class.java)!!
        } else {
            @Suppress("DEPRECATION")
            bundle.getParcelable(key)!!
        }
    }

    override fun serializeAsValue(value: Photo): String {
        return Uri.encode(Json.encodeToString(Photo.serializer(), value))
    }

    override fun parseValue(value: String): Photo {
        return Json.decodeFromString<Photo>(value)
    }
}