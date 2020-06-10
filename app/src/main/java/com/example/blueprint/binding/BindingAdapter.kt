package com.example.blueprint.binding

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter(value = ["imageUrl", "error"], requireAll = true)
fun loadImage(view: ImageView, url: String, error: Drawable) {
    Glide.with(view.context).load(url).error(error).into(view)
}
