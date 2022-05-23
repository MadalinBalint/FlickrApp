package com.mendelin.flickerapp.common

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.mendelin.flickerapp.base.BaseViewModel

object Utils {
    fun BaseViewModel.setUiState(state: CombinedLoadStates) {
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