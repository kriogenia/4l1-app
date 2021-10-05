package dev.sotoestevez.allforone.util.extensions

import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("android:tint")
fun AppCompatImageView.setImageTint(@ColorInt color: Int) {
    setColorFilter(color)
}