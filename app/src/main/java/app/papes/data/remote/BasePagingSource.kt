package app.papes.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import okio.IOException
import retrofit2.HttpException

/**
 * A base class for creating a PagingSource that fetches data from a remote source.
 *
 * @param V The type of data being loaded.
 * @param totalPages The total number of pages available. If null, paging will continue indefinitely.
 * @param onFetchPage A suspend function that fetches a page of data given a page number.
 */
open class BasePagingSource<V : Any>(
    private val totalPages: Int? = null,
    private val onFetchPage: suspend (Int) -> List<V>
) : PagingSource<Int, V>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, V> {
        return try {
            val pageNumber = params.key ?: 1
            val response = onFetchPage(pageNumber)
            LoadResult.Page(
                data = response,
                prevKey = null,
                nextKey = if (totalPages != null && pageNumber == totalPages) null else pageNumber + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, V>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}