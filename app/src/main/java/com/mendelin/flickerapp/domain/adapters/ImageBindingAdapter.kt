package com.mendelin.flickerapp.domain.adapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.mendelin.flickerapp.R
import timber.log.Timber

@BindingAdapter("imageUrl")
fun ImageView.setImageUrl(imageUrl: String?) {
    Timber.d(imageUrl)
    if (!imageUrl.isNullOrEmpty()) {
        val circularProgressDrawable = CircularProgressDrawable(context).apply {
            strokeWidth = 6f
            centerRadius = 50f
            start()
        }

        Glide.with(context)
            .applyDefaultRequestOptions(
                RequestOptions()
                    .format(DecodeFormat.PREFER_RGB_565)
                    .disallowHardwareConfig()
            )
            .load(imageUrl)
            .placeholder(circularProgressDrawable)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .error(R.drawable.ic_photo)
            .into(this)
    }
}