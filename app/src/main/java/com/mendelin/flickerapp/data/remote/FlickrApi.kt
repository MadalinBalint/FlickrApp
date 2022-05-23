package com.mendelin.flickerapp.data.remote


import com.mendelin.flickerapp.BuildConfig
import com.mendelin.flickerapp.domain.models.PhotosSearchResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrApi {
    @GET(BuildConfig.BASE_URL)
    fun searchPhotos(
        @Query(BuildConfig.QUERY_TAGS) tags: String,
        @Query(BuildConfig.QUERY_PAGE) page: Int,
        @Query(BuildConfig.QUERY_METHOD) method: String = BuildConfig.ENDPOINT_SEARCH_PHOTOS,
        @Query(BuildConfig.QUERY_MEDIA) media: String = "photos"
    ): Single<PhotosSearchResponse>
}