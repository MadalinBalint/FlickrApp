package com.mendelin.flickerapp.data.dto

import androidx.annotation.Keep

@Keep
data class PhotosSearchDto(
    val photos: PhotosDto,
    val stat: String
)
