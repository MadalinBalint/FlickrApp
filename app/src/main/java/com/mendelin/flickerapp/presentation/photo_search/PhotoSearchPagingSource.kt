package com.mendelin.flickerapp.presentation.photo_search

import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.mendelin.flickerapp.data.repository.FlickrRepositoryImpl
import com.mendelin.flickerapp.domain.models.PhotoItem
import com.mendelin.flickerapp.domain.models.PhotosSearchResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class PhotoSearchPagingSource @Inject constructor(private val tags: String, private val repository: FlickrRepositoryImpl) : RxPagingSource<Int, PhotoItem>() {
    override fun getRefreshKey(state: PagingState<Int, PhotoItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, PhotoItem>> {
        val currentPage = params.key ?: 1

        return repository.searchPhotos(tags, currentPage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                toLoadResult(it, currentPage)
            }
            .onErrorReturn {
                Timber.e(it.localizedMessage)
                LoadResult.Error(Exception(it.localizedMessage))
            }
    }

    private fun toLoadResult(data: PhotosSearchResponse, currentPage: Int): LoadResult<Int, PhotoItem> {
        return LoadResult.Page(
            data = data.photos.photo,
            prevKey = if (currentPage > 1) currentPage - 1 else null,
            nextKey = if (currentPage < data.photos.pages) currentPage + 1 else null
        )
    }
}