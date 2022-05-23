package com.mendelin.flickerapp.domain.models

data class PhotosItem(
    val page: Int,
    val pages: Int,
    val perpage: Int,
    val total: Int,
    val photo: List<PhotoItem>,
)
