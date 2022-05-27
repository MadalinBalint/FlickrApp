package com.mendelin.flickerapp.data.dto

import org.junit.Assert
import org.junit.Test

class PhotosSearchDtoTest {

    @Test
    fun testPhotosSearchDto() {
        val photo = PhotoDto(
            id = "52099532512",
            owner = "195621551@N06",
            secret = "288563ce07",
            server = "65535",
            farm = 66,
            title = "IMG-20211001-WA0033",
            ispublic = 1,
            isfriend = 0,
            isfamily = 0
        )

        val photos = PhotosDto(
            1,
            100,
            10,
            1003,
            listOf(photo)
        )

        val dto = PhotosSearchDto(
            photos,
            "ok",
        )

        Assert.assertEquals(dto.photos.page, 1)
        Assert.assertEquals(dto.photos.pages, 100)
        Assert.assertEquals(dto.photos.perpage, 10)
        Assert.assertEquals(dto.photos.total, 1003)
        Assert.assertEquals(dto.photos.photo.size, 1)
        Assert.assertEquals(dto.stat, "ok")
    }
}