package com.test.labmedia.di.components

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.test.labmedia.di.modules.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


@Component(modules = [ViewModelModule::class])
@Singleton
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun withApplication(application: Application): Builder

        fun build(): AppComponent
    }

    fun viewModelFactory(): ViewModelProvider.Factory
}