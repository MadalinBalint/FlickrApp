package com.mendelin.flickerapp.domain.use_case

import androidx.paging.PagingSource
import com.mendelin.flickerapp.domain.model.FlickrPhoto
import io.reactivex.rxjava3.core.Single

interface PhotoPagingDataUseCase {
    operator fun invoke(tags: String, currentPage: Int): Single<PagingSource.LoadResult<Int, FlickrPhoto>>
}