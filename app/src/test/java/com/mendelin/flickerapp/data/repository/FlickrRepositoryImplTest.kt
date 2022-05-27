package com.mendelin.flickerapp.data.repository

import com.mendelin.flickerapp.data.dto.PhotoDto
import com.mendelin.flickerapp.data.dto.PhotosDto
import com.mendelin.flickerapp.data.dto.PhotosSearchDto
import com.mendelin.flickerapp.data.remote.FlickrApi
import com.mendelin.flickerapp.domain.repository.FlickrRepository
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.observers.TestObserver
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito

class FlickrRepositoryImplTest {

    @Test
    fun testRepository() = runBlocking {
        val mockApi = Mockito.mock(FlickrApi::class.java)

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

        Mockito.`when`(mockApi.searchPhotos(tags = "cats", page = 1)).thenReturn(Single.just(dto))
        val flickrRepository: FlickrRepository = FlickrRepositoryImpl(mockApi)

        val photoSearchResponse = flickrRepository.searchPhotos(tags = "cats", page = 1)

        val testObserver = TestObserver<PhotosSearchDto>()

        photoSearchResponse.subscribe(testObserver)

        testObserver.apply {
            assertComplete()
            assertNoErrors()
            assertValueCount(1)
        }

        val response = testObserver.values()[0]

        Assert.assertEquals(response.photos.page, 1)
        Assert.assertEquals(response.photos.pages, 100)
        Assert.assertEquals(response.photos.perpage, 10)
        Assert.assertEquals(response.photos.total, 1003)
        Assert.assertEquals(response.photos.photo.size, 1)
        Assert.assertEquals(response.stat, "ok")
    }
}