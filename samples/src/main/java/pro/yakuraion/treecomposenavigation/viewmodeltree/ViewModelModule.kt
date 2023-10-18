package pro.yakuraion.treecomposenavigation.viewmodeltree

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.named
import org.koin.core.module.dsl.withOptions
import org.koin.core.qualifier.named
import org.koin.dsl.module
import pro.yakuraion.treecomposenavigation.viewmodeltree.screen1.VMChildScreen1ViewModel
import pro.yakuraion.treecomposenavigation.viewmodeltree.screen2.VMChildScreen2ViewModel
import pro.yakuraion.treecomposenavigation.viewmodeltree.screen2.ViewModelParcelableParameter
import pro.yakuraion.treecomposenavigation.viewmodeltree.screen2.ViewModelSerializableParameter
import pro.yakuraion.treecomposenavigation.viewmodeltree.screen3.VMChildScreen3ViewModel

val viewModelModule = module {
    single { "DI string" } withOptions {
        named("diString")
        createdAtStart()
    }

    viewModel { VMChildScreen1ViewModel() }
    viewModel {
            (
                arg1: Long,
                arg2: Float?,
                arg4: ViewModelSerializableParameter,
                arg5: ViewModelParcelableParameter,
            ),
        ->
        VMChildScreen2ViewModel(arg1, arg2, get(named("diString")), arg4, arg5)
    }
    viewModel { VMChildScreen3ViewModel() }
}
