package com.apero.realistic.utils

import android.os.Handler

object RunAble {
    fun runAfter(delay: Long, process: () -> Unit) {
        Handler().postDelayed({
            process()
        }, delay)
    }
}