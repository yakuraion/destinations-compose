package io.github.yakuraion.destinationscompose.viewmodeltree.di

import io.github.yakuraion.destinationscompose.viewmodeltree.model.SomeUseCase
import io.github.yakuraion.destinationscompose.viewmodeltree.screen1.ViewModel1
import io.github.yakuraion.destinationscompose.viewmodeltree.screen2.ViewModel2
import io.github.yakuraion.destinationscompose.viewmodeltree.screen3.ViewModel3
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.named
import org.koin.core.module.dsl.withOptions
import org.koin.dsl.module

val viewModelModule = module {
    single { "DI string" } withOptions { named("diString") }

    factoryOf(::SomeUseCase)

    viewModelOf(::ViewModel1)
    viewModelOf(::ViewModel2)
    viewModelOf(::ViewModel3)

//    viewModel { ViewModel1() }
//    viewModel {
//            (
//                arg1: Long,
//                arg2: Float?,
//                arg4: ViewModelSerializableParameter,
//                arg5: ViewModelParcelableParameter,
//            ),
//        ->
//        VMChildScreen2ViewModel(arg1, arg2, get(named("diString")), arg4, arg5)
//    }
//    viewModel { ViewModel3() }
}
