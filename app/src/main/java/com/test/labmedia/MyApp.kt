package com.test.labmedia

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.test.labmedia.di.components.AppComponent
import com.test.labmedia.di.components.DaggerAppComponent
import androidx.lifecycle.ViewModelProvider


class MyApp: Application() {

    private lateinit var appComponent: AppComponent
    private lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        appComponent = DaggerAppComponent
            .builder()
            .withApplication(this)
            .build()

        viewModelFactory = appComponent.viewModelFactory()
    }

    fun getViewModelFactory(): ViewModelProvider.Factory {
        return viewModelFactory
    }
}