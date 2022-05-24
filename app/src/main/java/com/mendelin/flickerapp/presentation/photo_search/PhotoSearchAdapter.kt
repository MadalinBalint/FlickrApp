package com.mendelin.flickerapp.presentation.photo_search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mendelin.flickerapp.ItemPhotoBinding
import com.mendelin.flickerapp.domain.model.FlickrPhoto

class PhotoSearchAdapter : PagingDataAdapter<FlickrPhoto, PhotoSearchAdapter.PhotoSearchViewHolder>(PhotoSearchDiffCallBack()) {
    inner class PhotoSearchViewHolder(var binding: ItemPhotoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: FlickrPhoto) {
            binding.property = photo
            binding.executePendingBindings()
        }
    }

    class PhotoSearchDiffCallBack : DiffUtil.ItemCallback<FlickrPhoto>() {
        override fun areItemsTheSame(oldItem: FlickrPhoto, newItem: FlickrPhoto): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: FlickrPhoto, newItem: FlickrPhoto): Boolean {
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