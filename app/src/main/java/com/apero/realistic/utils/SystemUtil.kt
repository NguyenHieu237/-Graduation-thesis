package com.apero.realistic.utils

import android.os.Build
import android.view.View
import android.view.Window
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

object SystemUtil {
    @Suppress("DEPRECATION")
    fun hideSystemNavigationBar(window: Window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            run {
                val windowInsetController =
                    ViewCompat.getWindowInsetsController(window.decorView)
                if (windowInsetController != null) {
                    windowInsetController.hide(WindowInsetsCompat.Type.navigationBars())
                    if (window.decorView.rootWindowInsets != null) {
                        window.decorView.rootWindowInsets.getInsetsIgnoringVisibility(
                            WindowInsetsCompat.Type.navigationBars()
                        )
                    }
                    window.setDecorFitsSystemWindows(true)
                }
            }
        } else {
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    )
        }
    }
}