package com.mendelin.flickerapp.presentation.photo_search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mendelin.flickerapp.ItemPhotoBinding
import com.mendelin.flickerapp.domain.models.PhotoItem

class PhotoSearchAdapter : PagingDataAdapter<PhotoItem, PhotoSearchAdapter.PhotoSearchViewHolder>(PhotoSearchDiffCallBack()) {
    inner class PhotoSearchViewHolder(var binding: ItemPhotoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: PhotoItem) {
            binding.property = photo
            binding.executePendingBindings()
        }
    }

    class PhotoSearchDiffCallBack : DiffUtil.ItemCallback<PhotoItem>() {
        override fun areItemsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: PhotoSearchViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoSearchViewHolder {
        return PhotoSearchViewHolder(ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }
}