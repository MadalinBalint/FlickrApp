package com.mendelin.flickerapp.domain.use_case

import androidx.paging.PagingSource
import com.mendelin.flickerapp.data.dto.PhotosSearchDto
import com.mendelin.flickerapp.data.dto.toFlickrPhoto
import com.mendelin.flickerapp.domain.model.FlickrPhoto
import com.mendelin.flickerapp.domain.repository.FlickrRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named


class PhotoPagingDataUseCaseImpl @Inject constructor(@Named("FlickrRepository") private val repository: FlickrRepository) : PhotoPagingDataUseCase {
    override operator fun invoke(tags: String, currentPage: Int): Single<PagingSource.LoadResult<Int, FlickrPhoto>> =
        repository.searchPhotos(tags, currentPage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                toLoadResult(it, currentPage)
            }
            .onErrorReturn {
                Timber.e(it.localizedMessage)
                PagingSource.LoadResult.Error(Exception(it.localizedMessage))
            }

    private fun toLoadResult(data: PhotosSearchDto, currentPage: Int): PagingSource.LoadResult<Int, FlickrPhoto> {
        return PagingSource.LoadResult.Page(
            data = data.photos.photo.map { it.toFlickrPhoto() },
            prevKey = if (currentPage > 1) currentPage - 1 else null,
            nextKey = if (currentPage < data.photos.pages) currentPage + 1 else null
        )
    }
}