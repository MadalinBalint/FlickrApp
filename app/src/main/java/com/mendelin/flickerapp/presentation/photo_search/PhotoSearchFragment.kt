package com.mendelin.flickerapp.presentation.photo_search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.mendelin.flickerapp.R
import com.mendelin.flickerapp.common.Utils.setUiState
import com.mendelin.flickerapp.databinding.FragmentPhotoSearchBinding
import com.mendelin.flickerapp.presentation.custom_view.MarginItemVerticalDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class PhotoSearchFragment : Fragment() {
    private val photoSearchViewModel: PhotoSearchViewModel by activityViewModels()
    private var binding: FragmentPhotoSearchBinding? = null
    private lateinit var photoSearchAdapter: PhotoSearchAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPhotoSearchBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        photoSearchAdapter = PhotoSearchAdapter()

        binding?.recyclerPhotoSearch?.apply {
            adapter = photoSearchAdapter
            layoutManager = LinearLayoutManager(requireActivity())
            isNestedScrollingEnabled = false

            addItemDecoration(
                MarginItemVerticalDecoration(
                    resources.getDimension(R.dimen.recyclerview_margin_horizontal).toInt(),
                    resources.getDimension(R.dimen.recyclerview_margin_horizontal).toInt()
                )
            )
        }

        binding?.swipePhotoSearch?.setOnRefreshListener {
            photoSearchAdapter.refresh()
            binding?.swipePhotoSearch?.isRefreshing = false
        }

        fetchSearchResults("")
    }

    private fun observeViewModel() {
        photoSearchViewModel.getErrorFilter().observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                Snackbar.make(binding?.frameLayout!!, it, Snackbar.LENGTH_LONG)
                    .setAction("Retry") {
                        photoSearchAdapter.refresh()
                    }
                    .show()
                photoSearchViewModel.onErrorHandled()
            }
        }

        photoSearchViewModel.getLoadingObservable().observe(viewLifecycleOwner) {
            binding?.progressPhotoSearch?.visibility = if (it == true) View.VISIBLE else View.INVISIBLE
        }

        lifecycleScope.launch {
            photoSearchAdapter.loadStateFlow.collectLatest { state ->
                photoSearchViewModel.setUiState(state)
            }
        }

        photoSearchViewModel.getSearchTags().observe(viewLifecycleOwner) { tags ->
            if (!tags.isNullOrEmpty()) {
                Timber.d("Tag = $tags")

                photoSearchAdapter.refresh()
                binding?.recyclerPhotoSearch?.scrollToPosition(0)
                fetchSearchResults(tags)
            }
        }
    }

    private fun fetchSearchResults(tags: String) {
        lifecycleScope.launch {
            photoSearchViewModel.getSearchResultsPagingData(tags).collectLatest { pagingData ->
                photoSearchAdapter.submitData(pagingData)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}