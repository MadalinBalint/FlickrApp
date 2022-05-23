package com.mendelin.flickerapp.domain.models

import androidx.annotation.Keep

@Keep
data class PhotosSearchResponse(
    val photos: PhotosItem,
    val stat: String
)
