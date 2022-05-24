package com.mendelin.flickerapp.domain.use_case

import com.mendelin.flickerapp.data.dto.PhotosSearchDto
import com.mendelin.flickerapp.domain.repository.FlickrRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class PhotoPagingDataUseCase @Inject constructor(private val repository: FlickrRepository) {
    operator fun invoke(tags: String, currentPage: Int): Single<PhotosSearchDto> =
        repository.searchPhotos(tags, currentPage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}