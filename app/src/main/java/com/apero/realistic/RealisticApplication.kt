package com.apero.realistic

import android.app.Application

class RealisticApplication: Application() {
    companion object {
        lateinit var shared: RealisticApplication
    }

    override fun onCreate() {
        super.onCreate()
        shared = this
    }
}