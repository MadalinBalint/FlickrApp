package com.mendelin.flickerapp.data.repository

import com.mendelin.flickerapp.data.remote.FlickrApi
import com.mendelin.flickerapp.data.dto.PhotosSearchDto
import com.mendelin.flickerapp.domain.repository.FlickrRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class FlickrRepositoryImpl @Inject constructor(private val service: FlickrApi) : FlickrRepository {
    override fun searchPhotos(tags: String, page: Int): Single<PhotosSearchDto> =
        service.searchPhotos(tags, page)
}