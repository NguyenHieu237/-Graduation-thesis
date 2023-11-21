package com.apero.realistic.base.ext

import android.content.Context
import android.widget.TextView
import androidx.core.content.ContextCompat

fun TextView.setTextColor(context: Context, colorId: Int) {
    this.setTextColor(
        ContextCompat.getColor(
            context,
            colorId
        )
    )
}