package com.mendelin.flickerapp.presentation.photo_search

import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.mendelin.flickerapp.data.dto.PhotoDto
import com.mendelin.flickerapp.data.dto.PhotosSearchDto
import com.mendelin.flickerapp.domain.use_case.PhotoPagingDataUseCase
import io.reactivex.rxjava3.core.Single
import timber.log.Timber
import javax.inject.Inject

class PhotoSearchPagingSource @Inject constructor(private val tags: String, private val useCase: PhotoPagingDataUseCase) : RxPagingSource<Int, PhotoDto>() {
    override fun getRefreshKey(state: PagingState<Int, PhotoDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, PhotoDto>> {
        val currentPage = params.key ?: 1

        return useCase(tags, currentPage)
            .map {
                toLoadResult(it, currentPage)
            }
            .onErrorReturn {
                Timber.e(it.localizedMessage)
                LoadResult.Error(Exception(it.localizedMessage))
            }
    }

    private fun toLoadResult(data: PhotosSearchDto, currentPage: Int): LoadResult<Int, PhotoDto> {
        return LoadResult.Page(
            data = data.photos.photo,
            prevKey = if (currentPage > 1) currentPage - 1 else null,
            nextKey = if (currentPage < data.photos.pages) currentPage + 1 else null
        )
    }
}