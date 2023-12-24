package com.example.comics

import android.app.Application
import com.example.comics.repository.IRepository
import com.example.comics.repository.Repository
import com.example.comics.view.ViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class Application: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@Application)
            modules(
                applicationModule
            )
        }
    }
}

val applicationModule = module {
    factory<IRepository> { Repository() }
    viewModel { ViewModel(get()) }
}