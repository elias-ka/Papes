package app.papes.presentation.photo_detail

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import app.papes.R
import app.papes.domain.Photo
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import java.io.FileNotFoundException
import java.io.OutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoDetailScreen(
    photo: Photo,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var bitmap: Bitmap? = null

    Column(
        modifier = modifier.padding(
            top = 0.dp,
            bottom = 32.dp,
            start = 32.dp,
            end = 32.dp
        )
    ) {
        PinchToZoomImage(
            imageUrl = photo.large,
            contentDescription = photo.photographer,
            onSuccess = { state ->
                bitmap = state.result.drawable.toBitmap()
            }
        )
        Spacer(Modifier.height(8.dp))
        AuthorCreditRow(
            author = photo.photographer,
            authorUrl = photo.photographerUrl,
        )
        Spacer(Modifier.height(16.dp))
        ActionButtonsRow(
            onDownloadClick = {
                bitmap?.let {
                    persistPhoto(context, it, photo)
                    Toast.makeText(
                        context,
                        context.getString(R.string.image_saved),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            onSetAsWallpaperClick = {
                bitmap?.let {
                    setAsWallpaper(context, it, photo)
                }
            }
        )
    }
}

@Composable
fun PinchToZoomImage(
    imageUrl: String,
    contentDescription: String,
    onSuccess: ((AsyncImagePainter.State.Success) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    BoxWithConstraints(
        modifier = modifier.fillMaxWidth()
    ) {
        val state = rememberTransformableState { zoomChange, panChange, _ ->
            scale = (scale * zoomChange).coerceIn(1f, 5f)

            val extraWidth = (scale - 1) * this.constraints.maxWidth
            val extraHeight = (scale - 1) * this.constraints.maxHeight

            val maxX = extraWidth / 2
            val maxY = extraHeight / 2

            offset = Offset(
                x = (offset.x + panChange.x).coerceIn(-maxX, maxX),
                y = (offset.y + panChange.y).coerceIn(-maxY, maxY)
            )
        }
        AsyncImage(
            model = imageUrl,
            contentDescription = contentDescription,
            onSuccess = onSuccess,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(32.dp))
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    translationX = offset.x
                    translationY = offset.y
                }
                .transformable(state)
        )
    }
}

@Composable
fun AuthorCreditRow(
    author: String,
    authorUrl: String,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Text(
            fontSize = 12.sp,
            text = buildAnnotatedString {
                append(stringResource(R.string.photo_by))
                append(" ")
                withLink(
                    LinkAnnotation.Url(
                        authorUrl,
                        TextLinkStyles(style = SpanStyle(textDecoration = TextDecoration.Underline))
                    ),
                ) {
                    append(author)
                }
                append(" ")
                append(stringResource(R.string.on_pexels))
            })
    }
}

@Composable
fun ActionButtonsRow(
    onDownloadClick: () -> Unit,
    onSetAsWallpaperClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier.fillMaxWidth()
    ) {
        ActionButton(
            onClick = onDownloadClick,
            icon = Icons.Default.Download,
            text = stringResource(R.string.image_save),
            colors = ButtonDefaults.filledTonalButtonColors()
        )
        ActionButton(
            onClick = onSetAsWallpaperClick,
            icon = Icons.Default.Wallpaper,
            text = stringResource(R.string.image_set_as_wallpaper)
        )
    }
}

@Composable
fun ActionButton(
    onClick: () -> Unit,
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier,
    colors: ButtonColors = ButtonDefaults.buttonColors()
) {
    Button(
        onClick = onClick,
        modifier = modifier.size(150.dp, 50.dp),
        colors = colors
    ) {
        Icon(icon, contentDescription = null)
        Spacer(Modifier.width(8.dp))
        Text(text)
    }
}

// FIXME: This wallpaper setting logic doesn't belong here,
//        but it doesn't belong in viewmodel either so I don't know where it should go.
//        Maybe have like an event channel and bubble the events up to the MainActivity?

fun filenameFromPhoto(photo: Photo): String {
    return String.format("%s_%s.jpg", photo.photographer.replace(" ", "_"), photo.id)
}

fun setAsWallpaper(context: Context, bitmap: Bitmap, photo: Photo) {
    val imageUri = persistPhoto(context, bitmap, photo)
    val intent = Intent(Intent.ACTION_ATTACH_DATA).apply {
        addCategory(Intent.CATEGORY_DEFAULT)
        setDataAndType(imageUri, "image/jpeg")
        putExtra("mimeType", "image/jpeg")
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
    }
    context.startActivity(
        Intent.createChooser(
            intent,
            context.getString(R.string.image_set_as_wallpaper_intent_title)
        )
    )
}

fun persistPhoto(
    context: Context,
    bitmap: Bitmap,
    photo: Photo
): Uri {
    val filename = filenameFromPhoto(photo)
    val (outputStream, imageUri) = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        getOutputStreamForNewerAndroid(context, filename)
    } else {
        getOutputStreamForOlderAndroid(filename)
    }
    outputStream?.use {
        writeBitmapToStream(bitmap, it)
    }

    return imageUri
}

private fun getOutputStreamForNewerAndroid(
    context: Context,
    filename: String
): Pair<OutputStream?, Uri> {
    val resolver = context.contentResolver
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
    }

    val imageUri =
        resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

    return imageUri?.let {
        try {
            Pair(resolver.openOutputStream(it), it)
        } catch (e: FileNotFoundException) {
            Log.e("PhotoDetailScreen", "Error creating file", e)
            Pair(null, it)
        }
    } ?: run {
        Pair(null, Uri.EMPTY)
    }
}

private fun getOutputStreamForOlderAndroid(filename: String): Pair<OutputStream?, Uri> {
    return try {
        val downloadsDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val imageFile = downloadsDir.resolve(filename)
        if (!imageFile.exists()) {
            imageFile.createNewFile()
        }
        Pair(imageFile.outputStream(), Uri.fromFile(imageFile))
    } catch (e: Exception) {
        Log.e("PhotoDetailScreen", "Error creating file", e)
        Pair(null, Uri.EMPTY)
    }
}

private fun writeBitmapToStream(bitmap: Bitmap, outputStream: OutputStream) {
    try {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    } catch (e: Exception) {
        Log.e("PhotoDetailScreen", "Error writing bitmap to stream", e)
    }
}