package pro.yakuraion.treecomposenavigation.viewmodeltree

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pro.yakuraion.treecomposenavigation.viewmodeltree.screen1.VMChildScreen1ViewModel
import pro.yakuraion.treecomposenavigation.viewmodeltree.screen2.VMChildScreen2ViewModel
import pro.yakuraion.treecomposenavigation.viewmodeltree.screen2.ViewModelParcelableParameter
import pro.yakuraion.treecomposenavigation.viewmodeltree.screen2.ViewModelSerializableParameter
import pro.yakuraion.treecomposenavigation.viewmodeltree.screen3.VMChildScreen3ViewModel

val viewModelModule = module {
    viewModel { VMChildScreen1ViewModel() }
    viewModel {
            (
                arg1: Long,
                arg2: Float?,
                arg3: Char,
                arg4: ViewModelSerializableParameter,
                arg5: ViewModelParcelableParameter,
            ),
        ->
        VMChildScreen2ViewModel(arg1, arg2, arg3, arg4, arg5)
    }
    viewModel { VMChildScreen3ViewModel() }
}
