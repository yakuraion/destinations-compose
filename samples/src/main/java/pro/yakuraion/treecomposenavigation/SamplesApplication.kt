package pro.yakuraion.treecomposenavigation

import android.app.Application
import org.koin.core.context.startKoin
import pro.yakuraion.treecomposenavigation.viewmodeltree.viewModelModule

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
