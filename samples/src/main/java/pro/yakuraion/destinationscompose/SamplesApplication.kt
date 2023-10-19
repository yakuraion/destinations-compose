package pro.yakuraion.destinationscompose

import android.app.Application
import org.koin.core.context.startKoin
import pro.yakuraion.destinationscompose.viewmodeltree.viewModelModule

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
