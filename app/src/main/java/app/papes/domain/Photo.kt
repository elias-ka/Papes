package app.papes.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

// FIXME: The Parcelable business should be in presentation layer preferably. I'm too lazy to create
//        another Photo class in the presentation layer just for this.
@Serializable
@Parcelize
data class Photo(
    val id: Int,
    val url: String,
    val photographer: String,
    val photographerUrl: String,
    val thumbnail: String,
    val large: String,
    val alt: String
) : Parcelable