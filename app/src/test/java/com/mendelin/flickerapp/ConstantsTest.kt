package com.mendelin.flickerapp

import org.junit.Assert
import org.junit.Test

class ConstantsTest {
    @Test
    fun testConstants() {
        Assert.assertEquals(BuildConfig.BASE_URL, "https://www.flickr.com/services/rest/")
        Assert.assertEquals(BuildConfig.ENDPOINT_SEARCH_PHOTOS, "flickr.photos.search")
        Assert.assertEquals(BuildConfig.FLICKR_API_KEY, "87d371889f3c66a6756fb6437efa5880")
    }
}