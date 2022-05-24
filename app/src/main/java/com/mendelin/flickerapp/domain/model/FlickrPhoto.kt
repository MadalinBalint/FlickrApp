package com.mendelin.flickerapp.domain.model

import androidx.annotation.Keep

@Keep
data class FlickrPhoto(
    val id: String,
    val title: String,
    val url: String
)
