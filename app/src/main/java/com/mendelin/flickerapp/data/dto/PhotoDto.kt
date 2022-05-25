package com.mendelin.flickerapp.data.dto

import androidx.annotation.Keep
import com.mendelin.flickerapp.domain.model.FlickrPhoto

@Keep
data class PhotoDto(
    val id: String,
    val owner: String,
    val secret: String,
    val server: String,
    val farm: Int,
    val title: String,
    val ispublic: Int,
    val isfriend: Int,
    val isfamily: Int
)

fun PhotoDto.toFlickrPhoto(): FlickrPhoto =
    FlickrPhoto(id, title, "https://live.staticflickr.com/${server}/${id}_${secret}.jpg")