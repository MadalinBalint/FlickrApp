package com.mendelin.flickerapp.presentation.photo_search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mendelin.flickerapp.BuildConfig
import com.mendelin.flickerapp.base.BaseViewModel
import com.mendelin.flickerapp.data.repository.FlickrRepositoryImpl
import com.mendelin.flickerapp.domain.models.PhotoItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PhotoSearchViewModel @Inject constructor(private val repository: FlickrRepositoryImpl) : BaseViewModel() {
    private var tags = MutableLiveData<String>("")

    fun setSearchTags(value: String) {
        tags.postValue(value)
    }

    fun getSearchResultsPagingData(tag: String): Flow<PagingData<PhotoItem>> {
        return Pager(
            config = PagingConfig(pageSize = BuildConfig.FLICKR_PER_PAGE),
            pagingSourceFactory = {
                PhotoSearchPagingSource(tag, repository)
            }
        ).flow.cachedIn(viewModelScope)
    }

    fun getSearchTags(): LiveData<String> = tags
}