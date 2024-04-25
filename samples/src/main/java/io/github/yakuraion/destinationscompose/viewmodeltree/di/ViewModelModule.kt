package io.github.yakuraion.destinationscompose.viewmodeltree.di

import io.github.yakuraion.destinationscompose.viewmodeltree.model.SomeUseCase
import io.github.yakuraion.destinationscompose.viewmodeltree.screen1.ViewModel1
import io.github.yakuraion.destinationscompose.viewmodeltree.screen2.ViewModel2
import io.github.yakuraion.destinationscompose.viewmodeltree.screen3.ViewModel3
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.named
import org.koin.core.module.dsl.withOptions
import org.koin.dsl.module

val viewModelModule = module {
    single { "DI string" } withOptions { named("diString") }

    factoryOf(::SomeUseCase)

    viewModelOf(::ViewModel1)
    viewModel { parameters -> ViewModel2(parameters.get(), parameters.get(), parameters.get(), parameters.get(), parameters.get()) }
    viewModelOf(::ViewModel3)
}
