package com.mendelin.flickerapp.data.dto

import org.junit.Assert
import org.junit.Test

class PhotoDtoTest {

    @Test
    fun testPhotoDto() {
        val dto = PhotoDto(
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

        Assert.assertEquals(dto.id, "52099532512")
        Assert.assertEquals(dto.owner, "195621551@N06")
        Assert.assertEquals(dto.secret, "288563ce07")

        val flickrPhoto = dto.toFlickrPhoto()
        Assert.assertEquals(flickrPhoto.id, "52099532512")
        Assert.assertEquals(flickrPhoto.title, "IMG-20211001-WA0033")
        Assert.assertEquals(flickrPhoto.url, "https://live.staticflickr.com/65535/52099532512_288563ce07.jpg")
    }
}