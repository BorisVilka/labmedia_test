package com.test.labmedia.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.labmedia.di.ViewModelFactory
import com.test.labmedia.di.ViewModelKey
import android.app.Application
import com.google.gson.Gson
import com.test.labmedia.data.RepositoryImpl
import com.test.labmedia.data.source.NetworkDataSource
import com.test.labmedia.domain.repository.Repository
import com.test.labmedia.domain.use_case.LoadQuestionsUseCase
import com.test.labmedia.domain.use_case.LoadTestUseCase
import com.test.labmedia.service.API
import com.test.labmedia.service.Client
import com.test.labmedia.ui.viewmodels.InformationViewModel
import com.test.labmedia.ui.viewmodels.QuizViewModel
import com.test.labmedia.ui.viewmodels.ResultViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(InformationViewModel::class)
    abstract fun bindInformationViewModel(viewModel: InformationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(QuizViewModel::class)
    abstract fun bindQuizViewModel(viewModel: QuizViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ResultViewModel::class)
    abstract fun bindResultViewModel(viewModel: ResultViewModel): ViewModel

    companion object {

        @Provides
        fun provideGson(): Gson {
            return Gson()
        }

        @Provides
        fun provideApi(): API {
            return Client.getInstance().create(API::class.java)
        }

        @Provides
        fun provideNetworkDataSource(api: API): NetworkDataSource {
            return NetworkDataSource(api)
        }

        @Provides
        fun provideRepository(
            application: Application,
            networkDataSource: NetworkDataSource,
            gson: Gson
        ): Repository {
            return RepositoryImpl(application, networkDataSource, gson)
        }

        @Provides
        fun provideLoadTestUseCase(repository: Repository): LoadTestUseCase {
            return LoadTestUseCase(repository)
        }

        @Provides
        fun provideLoadQuestionsUseCase(repository: Repository): LoadQuestionsUseCase {
            return LoadQuestionsUseCase(repository)
        }
    }
}
