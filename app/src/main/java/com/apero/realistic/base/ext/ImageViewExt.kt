package com.apero.realistic.base.ext

import android.content.Context
import android.widget.ImageView
import androidx.core.content.ContextCompat

fun ImageView.setColorFilter(context: Context, colorId: Int) {
    this.setColorFilter(
        ContextCompat.getColor(
            context,
            colorId
        ), android.graphics.PorterDuff.Mode.SRC_IN
    )
}