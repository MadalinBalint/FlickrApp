package com.mendelin.flickerapp.data.repository

import com.mendelin.flickerapp.data.FlickrDataSource
import com.mendelin.flickerapp.domain.models.PhotosSearchResponse
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class FlickrRepository @Inject constructor(private val service: FlickrDataSource) {
    fun searchPhotos(tags: String, page: Int): Single<PhotosSearchResponse> =
        service.searchPhotos(tags, page)
}