package com.isil.android.pa3

import android.app.Application
import com.isil.android.pa3.di.AppContainer

class PA3Application : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
