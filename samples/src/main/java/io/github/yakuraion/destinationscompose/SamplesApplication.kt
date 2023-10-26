package io.github.yakuraion.destinationscompose

import android.app.Application
import io.github.yakuraion.destinationscompose.viewmodeltree.di.viewModelModule
import org.koin.core.context.startKoin

class SamplesApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(
                viewModelModule,
            )
        }
    }
}
