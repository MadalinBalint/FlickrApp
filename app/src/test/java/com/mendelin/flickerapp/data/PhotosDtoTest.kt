package com.mendelin.flickerapp.data

import com.mendelin.flickerapp.data.dto.PhotoDto
import com.mendelin.flickerapp.data.dto.PhotosDto
import org.junit.Assert
import org.junit.Test

class PhotosDtoTest {
    @Test
    fun testPhotosDto() {
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

        val dto = PhotosDto(
            1,
            100,
            10,
            1003,
            listOf(photo)
        )

        Assert.assertEquals(dto.page, 1)
        Assert.assertEquals(dto.pages, 100)
        Assert.assertEquals(dto.perpage, 10)
        Assert.assertEquals(dto.total, 1003)
        Assert.assertEquals(dto.photo.size, 1)
    }
}