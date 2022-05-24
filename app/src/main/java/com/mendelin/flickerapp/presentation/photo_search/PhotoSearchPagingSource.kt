package com.mendelin.flickerapp.presentation.photo_search

import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.mendelin.flickerapp.domain.model.FlickrPhoto
import com.mendelin.flickerapp.domain.use_case.PhotoPagingDataUseCase
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import javax.inject.Named

class PhotoSearchPagingSource @Inject constructor(private val tags: String, @Named("PhotoPagingDataUseCase") private val useCase: PhotoPagingDataUseCase) : RxPagingSource<Int, FlickrPhoto>() {
    override fun getRefreshKey(state: PagingState<Int, FlickrPhoto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, FlickrPhoto>> {
        val currentPage = params.key ?: 1

        return useCase(tags, currentPage)
    }
}