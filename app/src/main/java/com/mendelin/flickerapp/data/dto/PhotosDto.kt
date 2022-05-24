package com.mendelin.flickerapp.data.dto

data class PhotosDto(
    val page: Int,
    val pages: Int,
    val perpage: Int,
    val total: Int,
    val photo: List<PhotoDto>,
)
