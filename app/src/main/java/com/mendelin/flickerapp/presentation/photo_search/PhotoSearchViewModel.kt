package com.mendelin.flickerapp.presentation.photo_search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.mendelin.flickerapp.BuildConfig
import com.mendelin.flickerapp.domain.model.FlickrPhoto
import com.mendelin.flickerapp.domain.use_case.PhotoPagingDataUseCaseImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PhotoSearchViewModel @Inject constructor(private val useCase: PhotoPagingDataUseCaseImpl) : ViewModel() {
    private var tags = MutableLiveData<String>("")
    val error = MutableLiveData<String>()
    val isLoading = MutableLiveData(false)

    fun getErrorFilter(): LiveData<String> = error
    fun getLoadingObservable(): LiveData<Boolean> = isLoading

    fun onErrorHandled() {
        error.value = ""
    }

    fun setSearchTags(value: String) {
        tags.postValue(value)
    }

    fun getSearchResultsPagingData(tag: String): Flow<PagingData<FlickrPhoto>> {
        return Pager(
            config = PagingConfig(pageSize = BuildConfig.FLICKR_PER_PAGE),
            pagingSourceFactory = {
                PhotoSearchPagingSource(tag, useCase)
            }
        ).flow.cachedIn(viewModelScope)
    }

    fun getSearchTags(): LiveData<String> = tags

    fun setUiState(state: CombinedLoadStates) {
        isLoading.value = state.refresh is LoadState.Loading

        val errorState = state.refresh as? LoadState.Error
            ?: state.source.append as? LoadState.Error
            ?: state.source.prepend as? LoadState.Error
            ?: state.append as? LoadState.Error
            ?: state.prepend as? LoadState.Error

        errorState?.let {
            error.value = it.error.message
        }
    }
}