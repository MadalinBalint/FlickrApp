package com.mendelin.flickerapp.domain.repository

import com.mendelin.flickerapp.data.dto.PhotosSearchDto
import io.reactivex.rxjava3.core.Single

interface FlickrRepository {
    fun searchPhotos(tags: String, page: Int): Single<PhotosSearchDto>
}